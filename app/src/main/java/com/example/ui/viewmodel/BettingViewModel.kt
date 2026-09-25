package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.BetOrderEntity
import com.example.data.db.Converters
import com.example.data.db.UserProfileEntity
import com.example.data.db.WalletTransactionEntity
import com.example.data.model.BetSelection
import com.example.data.model.BetType
import com.example.data.model.Match
import com.example.data.model.MatchStatus
import com.example.data.repository.BettingRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

enum class NavTab {
    SPORTS, LIVE, ROCKET, ORDERS, ME
}

class BettingViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = BettingRepository(database)

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allOrders: StateFlow<List<BetOrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val walletTransactions: StateFlow<List<WalletTransactionEntity>> = repository.walletTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentTab = MutableStateFlow(NavTab.SPORTS)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    private val _selectedSport = MutableStateFlow("All")
    val selectedSport: StateFlow<String> = _selectedSport.asStateFlow()

    private val _selectedTimeFilter = MutableStateFlow("All")
    val selectedTimeFilter: StateFlow<String> = _selectedTimeFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isBalanceHidden = MutableStateFlow(false)
    val isBalanceHidden: StateFlow<Boolean> = _isBalanceHidden.asStateFlow()

    // Filtered matches
    val filteredMatches: StateFlow<List<Match>> = combine(
        repository.matches,
        _selectedSport,
        _selectedTimeFilter,
        _searchQuery,
        _currentTab
    ) { matches, sport, timeFilter, query, tab ->
        var list = matches
        if (tab == NavTab.LIVE) {
            list = list.filter { it.isLive }
        }
        if (sport != "All") {
            list = list.filter { it.sport.equals(sport, ignoreCase = true) }
        }
        when (timeFilter) {
            "Live" -> list = list.filter { it.isLive }
            "Today" -> list = list.filter { it.matchTime.contains("Today", ignoreCase = true) || it.isLive }
            "Upcoming" -> list = list.filter { !it.isLive }
        }
        if (query.isNotBlank()) {
            list = list.filter {
                it.homeTeam.contains(query, ignoreCase = true) ||
                it.awayTeam.contains(query, ignoreCase = true) ||
                it.league.contains(query, ignoreCase = true)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Betslip selections
    private val _selections = MutableStateFlow<List<BetSelection>>(emptyList())
    val selections: StateFlow<List<BetSelection>> = _selections.asStateFlow()

    private val _isBetslipOpen = MutableStateFlow(false)
    val isBetslipOpen: StateFlow<Boolean> = _isBetslipOpen.asStateFlow()

    private val _stakeInput = MutableStateFlow("50")
    val stakeInput: StateFlow<String> = _stakeInput.asStateFlow()

    private val _betType = MutableStateFlow(BetType.MULTIPLE)
    val betType: StateFlow<BetType> = _betType.asStateFlow()

    private val _acceptOddsChanges = MutableStateFlow(true)
    val acceptOddsChanges: StateFlow<Boolean> = _acceptOddsChanges.asStateFlow()

    // Dialogs & Sheets
    private val _detailedMatch = MutableStateFlow<Match?>(null)
    val detailedMatch: StateFlow<Match?> = _detailedMatch.asStateFlow()

    private val _isDepositOpen = MutableStateFlow(false)
    val isDepositOpen: StateFlow<Boolean> = _isDepositOpen.asStateFlow()

    private val _isWithdrawOpen = MutableStateFlow(false)
    val isWithdrawOpen: StateFlow<Boolean> = _isWithdrawOpen.asStateFlow()

    private val _isAuthOpen = MutableStateFlow(false)
    val isAuthOpen: StateFlow<Boolean> = _isAuthOpen.asStateFlow()

    private val _showWelcomeDepositPrompt = MutableStateFlow(false)
    val showWelcomeDepositPrompt: StateFlow<Boolean> = _showWelcomeDepositPrompt.asStateFlow()

    private val _authMode = MutableStateFlow("LOGIN") // LOGIN or REGISTER
    val authMode: StateFlow<String> = _authMode.asStateFlow()

    private val _bookingCodeDialog = MutableStateFlow<String?>(null)
    val bookingCodeDialog: StateFlow<String?> = _bookingCodeDialog.asStateFlow()

    private val _isLoadCodeOpen = MutableStateFlow(false)
    val isLoadCodeOpen: StateFlow<Boolean> = _isLoadCodeOpen.asStateFlow()

    private val _ticketSuccessOrder = MutableStateFlow<BetOrderEntity?>(null)
    val ticketSuccessOrder: StateFlow<BetOrderEntity?> = _ticketSuccessOrder.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()

    // Orders Filter
    private val _ordersFilter = MutableStateFlow("ALL") // ALL, OPEN, SETTLED, WON, CASHED_OUT
    val ordersFilter: StateFlow<String> = _ordersFilter.asStateFlow()

    // Global Rocket (Instant Crash Game)
    private val _rocketMultiplier = MutableStateFlow(1.00f)
    val rocketMultiplier: StateFlow<Float> = _rocketMultiplier.asStateFlow()

    private val _isRocketRunning = MutableStateFlow(false)
    val isRocketRunning: StateFlow<Boolean> = _isRocketRunning.asStateFlow()

    private val _isRocketCrashed = MutableStateFlow(false)
    val isRocketCrashed: StateFlow<Boolean> = _isRocketCrashed.asStateFlow()

    private val _rocketBetPlaced = MutableStateFlow(false)
    val rocketBetPlaced: StateFlow<Boolean> = _rocketBetPlaced.asStateFlow()

    private val _rocketBetAmount = MutableStateFlow(20.0)
    val rocketBetAmount: StateFlow<Double> = _rocketBetAmount.asStateFlow()

    private val _rocketCashedOut = MutableStateFlow(false)
    val rocketCashedOut: StateFlow<Boolean> = _rocketCashedOut.asStateFlow()

    private val _rocketWinAmount = MutableStateFlow(0.0)
    val rocketWinAmount: StateFlow<Double> = _rocketWinAmount.asStateFlow()

    private var rocketJob: Job? = null

    // Odds calculations
    val totalOdds: StateFlow<Double> = combine(_selections, _betType) { sels, type ->
        if (sels.isEmpty()) return@combine 1.00
        var product = 1.0
        for (s in sels) {
            product *= s.odds
        }
        String.format(Locale.US, "%.2f", product).toDouble()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.00)

    val bonusBoostPercent: StateFlow<Int> = combine(_selections) { sels ->
        when {
            sels.size >= 5 -> 15
            sels.size >= 4 -> 10
            sels.size >= 3 -> 5
            else -> 0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val possibleWin: StateFlow<Double> = combine(_stakeInput, totalOdds, bonusBoostPercent) { stakeStr, odds, bonusPct ->
        val stake = stakeStr.toDoubleOrNull() ?: 0.0
        val baseWin = stake * odds
        val bonus = baseWin * (bonusPct / 100.0)
        String.format(Locale.US, "%.2f", baseWin + bonus).toDouble()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun setTab(tab: NavTab) {
        _currentTab.value = tab
    }

    fun setSelectedSport(sport: String) {
        _selectedSport.value = sport
    }

    fun setSelectedTimeFilter(filter: String) {
        _selectedTimeFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleBalanceVisibility() {
        _isBalanceHidden.value = !_isBalanceHidden.value
    }

    fun openMatchDetail(match: Match) {
        _detailedMatch.value = match
    }

    fun closeMatchDetail() {
        _detailedMatch.value = null
    }

    fun toggleSelection(
        match: Match,
        marketId: String,
        marketName: String,
        outcomeId: String,
        outcomeLabel: String,
        odds: Double
    ) {
        val current = _selections.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.matchId == match.id && it.marketId == marketId }

        if (existingIndex != -1) {
            val existing = current[existingIndex]
            if (existing.outcomeId == outcomeId) {
                // Clicked again -> remove
                current.removeAt(existingIndex)
            } else {
                // Change outcome in same market
                current[existingIndex] = existing.copy(
                    outcomeId = outcomeId,
                    outcomeLabel = outcomeLabel,
                    odds = odds
                )
            }
        } else {
            // New selection
            current.add(
                BetSelection(
                    matchId = match.id,
                    homeTeam = match.homeTeam,
                    awayTeam = match.awayTeam,
                    league = match.league,
                    marketId = marketId,
                    marketName = marketName,
                    outcomeId = outcomeId,
                    outcomeLabel = outcomeLabel,
                    odds = odds,
                    liveScoreWhenPlaced = if (match.homeScore != null) "${match.homeScore}-${match.awayScore}" else "Upcoming"
                )
            )
        }
        _selections.value = current
    }

    fun isSelected(matchId: String, marketId: String, outcomeId: String): Boolean {
        return _selections.value.any { it.matchId == matchId && it.marketId == marketId && it.outcomeId == outcomeId }
    }

    fun removeSelection(matchId: String, marketId: String) {
        _selections.value = _selections.value.filterNot { it.matchId == matchId && it.marketId == marketId }
    }

    fun clearBetslip() {
        _selections.value = emptyList()
    }

    fun setBetslipOpen(open: Boolean) {
        _isBetslipOpen.value = open
    }

    fun setStakeInput(stake: String) {
        _stakeInput.value = stake
    }

    fun addStakeAmount(add: Double) {
        val cur = _stakeInput.value.toDoubleOrNull() ?: 0.0
        _stakeInput.value = String.format(Locale.US, "%.0f", cur + add)
    }

    fun setBetType(type: BetType) {
        _betType.value = type
    }

    fun setAcceptOddsChanges(accept: Boolean) {
        _acceptOddsChanges.value = accept
    }

    fun placeBet() {
        val stake = _stakeInput.value.toDoubleOrNull() ?: 0.0
        if (stake <= 0) {
            _snackBarMessage.value = "Please enter a valid stake amount"
            return
        }
        if (_selections.value.isEmpty()) {
            _snackBarMessage.value = "Your betslip is empty"
            return
        }

        viewModelScope.launch {
            val typeStr = if (_selections.value.size == 1) "Single" else "Multiple (${_selections.value.size} Legs)"
            val result = repository.placeBet(_selections.value, stake, typeStr)
            result.onSuccess { order ->
                _ticketSuccessOrder.value = order
                _selections.value = emptyList()
                _isBetslipOpen.value = false
            }.onFailure { err ->
                _snackBarMessage.value = err.message ?: "Failed to place bet"
            }
        }
    }

    fun cashOut(ticketId: String, amount: Double) {
        viewModelScope.launch {
            val res = repository.cashOutOrder(ticketId, amount)
            res.onSuccess {
                _snackBarMessage.value = "Cash Out Successful! +$$amount added to wallet"
            }.onFailure {
                _snackBarMessage.value = it.message ?: "Cash Out failed"
            }
        }
    }

    fun bookABet() {
        if (_selections.value.isEmpty()) {
            _snackBarMessage.value = "Select matches to create a booking code"
            return
        }
        val code = "GB-" + (1000..9999).random() + ('A'..'Z').random()
        _bookingCodeDialog.value = code
    }

    fun loadBookingCode(code: String) {
        if (code.isBlank()) return
        // Populate standard picks for any code to simulate instant load
        val matches = repository.matches.value
        val picks = mutableListOf<BetSelection>()
        matches.take(3).forEach { match ->
            val mkt = match.markets.firstOrNull() ?: return@forEach
            val out = mkt.outcomes.firstOrNull() ?: return@forEach
            picks.add(
                BetSelection(
                    matchId = match.id,
                    homeTeam = match.homeTeam,
                    awayTeam = match.awayTeam,
                    league = match.league,
                    marketId = mkt.id,
                    marketName = mkt.name,
                    outcomeId = out.id,
                    outcomeLabel = out.label,
                    odds = out.odds
                )
            )
        }
        _selections.value = picks
        _isLoadCodeOpen.value = false
        _isBetslipOpen.value = true
        _snackBarMessage.value = "Code '$code' loaded successfully (${picks.size} selections)!"
    }

    fun openDeposit() {
        _isDepositOpen.value = true
    }

    fun closeDeposit() {
        _isDepositOpen.value = false
    }

    fun deposit(amount: Double, method: String) {
        viewModelScope.launch {
            repository.deposit(amount, method)
            _isDepositOpen.value = false
            _snackBarMessage.value = "Successfully deposited $$amount via $method!"
        }
    }

    fun openWithdraw() {
        _isWithdrawOpen.value = true
    }

    fun closeWithdraw() {
        _isWithdrawOpen.value = false
    }

    fun withdraw(amount: Double, method: String, accountInfo: String) {
        viewModelScope.launch {
            val res = repository.withdraw(amount, method, accountInfo)
            res.onSuccess {
                _isWithdrawOpen.value = false
                _snackBarMessage.value = "Withdrawal of $$amount to $method submitted successfully!"
            }.onFailure {
                _snackBarMessage.value = it.message ?: "Withdrawal failed"
            }
        }
    }

    fun openAuth(mode: String = "LOGIN") {
        _authMode.value = mode
        _isAuthOpen.value = true
    }

    fun closeAuth() {
        _isAuthOpen.value = false
    }

    fun login(phoneOrEmail: String, pass: String) {
        viewModelScope.launch {
            repository.login(phoneOrEmail, pass)
            _isAuthOpen.value = false
            _showWelcomeDepositPrompt.value = true
            _snackBarMessage.value = "Logged in successfully! Welcome back."
        }
    }

    fun register(
        username: String,
        phone: String,
        email: String,
        pass: String,
        country: String = "Ghana",
        currencySymbol: String = "GH₵"
    ) {
        viewModelScope.launch {
            repository.register(username, phone, email, pass, country, currencySymbol)
            _isAuthOpen.value = false
            _showWelcomeDepositPrompt.value = true
            _snackBarMessage.value = "Account created for $country! Balance: ${currencySymbol}0.00."
        }
    }

    fun updateCountryAndCurrency(country: String, currencySymbol: String) {
        viewModelScope.launch {
            repository.updateCountryAndCurrency(country, currencySymbol)
            _snackBarMessage.value = "Country updated to $country ($currencySymbol)"
        }
    }

    fun dismissWelcomeDeposit() {
        _showWelcomeDepositPrompt.value = false
    }

    fun acceptWelcomeDeposit(amount: Double) {
        _showWelcomeDepositPrompt.value = false
        _isDepositOpen.value = true
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _snackBarMessage.value = "Logged out successfully"
        }
    }

    fun setOrdersFilter(filter: String) {
        _ordersFilter.value = filter
    }

    fun closeReceipt() {
        _ticketSuccessOrder.value = null
    }

    fun clearSnackBar() {
        _snackBarMessage.value = null
    }

    fun closeBookingDialog() {
        _bookingCodeDialog.value = null
    }

    fun openLoadCodeDialog() {
        _isLoadCodeOpen.value = true
    }

    fun closeLoadCodeDialog() {
        _isLoadCodeOpen.value = false
    }

    // Crash Game Logic
    fun startRocketGame() {
        val user = userProfile.value ?: return
        val bet = _rocketBetAmount.value
        if (user.balance < bet) {
            _snackBarMessage.value = "Insufficient balance for Rocket bet"
            return
        }

        viewModelScope.launch {
            repository.withdraw(bet, "Global Rocket", "Game Bet")
            _rocketBetPlaced.value = true
            _rocketCashedOut.value = false
            _isRocketCrashed.value = false
            _isRocketRunning.value = true
            _rocketMultiplier.value = 1.00f

            rocketJob?.cancel()
            rocketJob = launch {
                val crashMultiplier = (110..850).random() / 100f // Random crash between 1.10x and 8.50x
                var current = 1.00f
                while (current < crashMultiplier) {
                    delay(80)
                    current += 0.02f + (current * 0.015f)
                    _rocketMultiplier.value = String.format(Locale.US, "%.2f", current).toFloat()
                }
                // Rocket Crashed!
                _isRocketCrashed.value = true
                _isRocketRunning.value = false
                if (!_rocketCashedOut.value && _rocketBetPlaced.value) {
                    _rocketBetPlaced.value = false
                    _snackBarMessage.value = "Rocket crashed at ${String.format(Locale.US, "%.2f", current)}x!"
                }
            }
        }
    }

    fun cashOutRocket() {
        if (!_isRocketRunning.value || _rocketCashedOut.value || !_rocketBetPlaced.value) return
        val mult = _rocketMultiplier.value
        val bet = _rocketBetAmount.value
        val win = String.format(Locale.US, "%.2f", bet * mult).toDouble()
        _rocketCashedOut.value = true
        _rocketWinAmount.value = win

        viewModelScope.launch {
            repository.deposit(win, "Rocket Win")
            _snackBarMessage.value = "Cashed Out at ${mult}x! Won +$$win"
        }
    }

    fun setRocketBetAmount(amount: Double) {
        _rocketBetAmount.value = amount
    }
}
