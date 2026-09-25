package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.BetOrderEntity
import com.example.data.db.Converters
import com.example.data.db.UserProfileEntity
import com.example.data.db.WalletTransactionEntity
import com.example.data.model.BetSelection
import com.example.data.model.Market
import com.example.data.model.MarketOutcome
import com.example.data.model.Match
import com.example.data.model.MatchStatus
import com.example.data.model.OddsDirection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

class BettingRepository(
    private val database: AppDatabase,
    private val appScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val betOrderDao = database.betOrderDao()
    private val walletDao = database.walletDao()
    private val userDao = database.userDao()

    val allOrders: Flow<List<BetOrderEntity>> = betOrderDao.getAllOrders()
    val openOrders: Flow<List<BetOrderEntity>> = betOrderDao.getOpenOrders()
    val settledOrders: Flow<List<BetOrderEntity>> = betOrderDao.getSettledOrders()
    val walletTransactions: Flow<List<WalletTransactionEntity>> = walletDao.getAllTransactions()
    val userProfile: Flow<UserProfileEntity?> = userDao.getUserProfile()

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    init {
        initializeInitialData()
        startLiveSimulation()
    }

    private fun initializeInitialData() {
        appScope.launch {
            // Seed initial user if absent
            val existing = userDao.getUserProfileOnce()
            if (existing == null) {
                userDao.insertOrUpdateProfile(
                    UserProfileEntity(
                        id = 1,
                        username = "GlobalElite_VIP",
                        phoneNumber = "+233 50 123 4567",
                        email = "elite@globalbetting.com",
                        country = "Ghana",
                        balance = 0.00,
                        bonusBalance = 0.00,
                        vipTier = "VIP Bronze",
                        isLoggedIn = false,
                        currencySymbol = "GH₵"
                    )
                )
                // Seed initial wallet transactions
                walletDao.insertTransaction(
                    WalletTransactionEntity(
                        txId = "TXN-INIT-001",
                        type = "DEPOSIT",
                        amount = 3500.00,
                        description = "Instant Card Deposit (Mastercard *4892)",
                        balanceAfter = 3500.00
                    )
                )
                // Seed initial sample open bet to test Orders page immediately!
                val sampleSelections = listOf(
                    BetSelection(
                        matchId = "m_1",
                        homeTeam = "Real Madrid",
                        awayTeam = "Manchester City",
                        league = "UEFA Champions League",
                        marketId = "mkt_1x2",
                        marketName = "1X2 (Match Winner)",
                        outcomeId = "1",
                        outcomeLabel = "Real Madrid (1)",
                        odds = 2.15,
                        liveScoreWhenPlaced = "1-1",
                        selectionStatus = "PENDING"
                    ),
                    BetSelection(
                        matchId = "m_2",
                        homeTeam = "Arsenal",
                        awayTeam = "Liverpool",
                        league = "Premier League",
                        marketId = "mkt_bts",
                        marketName = "Both Teams to Score",
                        outcomeId = "yes",
                        outcomeLabel = "Yes (GG)",
                        odds = 1.68,
                        liveScoreWhenPlaced = "1-0",
                        selectionStatus = "WON"
                    )
                )
                val sampleTicket = "GB-2026-98124"
                betOrderDao.insertOrder(
                    BetOrderEntity(
                        ticketId = sampleTicket,
                        timestamp = System.currentTimeMillis() - 45 * 60 * 1000,
                        betType = "Multiple (2 Legs)",
                        totalStake = 50.00,
                        totalOdds = 3.61,
                        potentialWin = 180.50,
                        bonusAmount = 9.03,
                        status = "OPEN",
                        cashOutOffer = 112.50,
                        selectionsJson = Converters.selectionsToJson(sampleSelections)
                    )
                )
            }
            _matches.value = createInitialMatches()
        }
    }

    private fun startLiveSimulation() {
        appScope.launch {
            while (true) {
                delay(3000)
                updateLiveMatches()
            }
        }
    }

    private fun updateLiveMatches() {
        val currentList = _matches.value.toMutableList()
        val updated = currentList.map { match ->
            if (match.isLive) {
                val newMinute = (match.liveMinute ?: 45) + 1
                // Slightly randomize pitch radar
                val newBallX = (match.ballPositionPercentX + (Random.nextFloat() - 0.5f) * 0.15f).coerceIn(0.1f, 0.9f)
                val newBallY = (match.ballPositionPercentY + (Random.nextFloat() - 0.5f) * 0.15f).coerceIn(0.15f, 0.85f)
                
                val actions = listOf(
                    "Attacking possession in final third",
                    "Dangerous Attack - Cross into box!",
                    "Corner Kick awarded",
                    "Shot on target saved by goalkeeper!",
                    "Counter attack in transition",
                    "Free kick from 25 yards",
                    "Midfield battle for loose ball"
                )
                val newAction = if (Random.nextInt(10) > 6) actions.random() else match.currentPitchAction
                
                // Occasional odds fluctuation
                val updatedMarkets = match.markets.map { market ->
                    if (Random.nextInt(10) > 7) {
                        val newOutcomes = market.outcomes.map { outcome ->
                            val delta = (Random.nextInt(3) - 1) * 0.05
                            val newOdds = String.format(Locale.US, "%.2f", (outcome.odds + delta).coerceIn(1.10, 15.00)).toDouble()
                            val dir = when {
                                newOdds > outcome.odds -> OddsDirection.UP
                                newOdds < outcome.odds -> OddsDirection.DOWN
                                else -> OddsDirection.NONE
                            }
                            outcome.copy(
                                odds = newOdds,
                                previousOdds = outcome.odds,
                                oddsDirection = dir
                            )
                        }
                        market.copy(outcomes = newOutcomes)
                    } else {
                        market.copy(outcomes = market.outcomes.map { it.copy(oddsDirection = OddsDirection.NONE) })
                    }
                }

                match.copy(
                    liveMinute = if (newMinute > 90) 90 else newMinute,
                    matchTime = "${if (newMinute > 90) 90 else newMinute}'",
                    currentPitchAction = newAction,
                    ballPositionPercentX = newBallX,
                    ballPositionPercentY = newBallY,
                    markets = updatedMarkets
                )
            } else {
                match
            }
        }
        _matches.value = updated
    }

    suspend fun placeBet(
        selections: List<BetSelection>,
        stake: Double,
        betType: String
    ): Result<BetOrderEntity> {
        val user = userDao.getUserProfileOnce() ?: return Result.failure(Exception("User not found"))
        if (user.balance < stake) {
            return Result.failure(Exception("Insufficient balance. Please deposit funds."))
        }

        // Calculate odds and potential win
        var totalOdds = 1.0
        selections.forEach { totalOdds *= it.odds }
        totalOdds = String.format(Locale.US, "%.2f", totalOdds).toDouble()
        
        // Multi-bet bonus boost: 3+ selections get boost
        val bonusRate = when {
            selections.size >= 5 -> 0.15
            selections.size >= 3 -> 0.08
            else -> 0.0
        }
        val bonus = stake * totalOdds * bonusRate
        val potentialWin = String.format(Locale.US, "%.2f", (stake * totalOdds) + bonus).toDouble()

        val ticketId = "GB-" + (100000..999999).random().toString()
        val newBalance = user.balance - stake

        val order = BetOrderEntity(
            ticketId = ticketId,
            timestamp = System.currentTimeMillis(),
            betType = betType,
            totalStake = stake,
            totalOdds = totalOdds,
            potentialWin = potentialWin,
            bonusAmount = bonus,
            status = "OPEN",
            cashOutOffer = String.format(Locale.US, "%.2f", stake * 0.95).toDouble(),
            selectionsJson = Converters.selectionsToJson(selections)
        )

        betOrderDao.insertOrder(order)
        userDao.updateBalance(newBalance)
        walletDao.insertTransaction(
            WalletTransactionEntity(
                txId = "TXN-BET-" + (10000..99999).random(),
                type = "BET_PLACED",
                amount = -stake,
                description = "Bet Placed: Ticket #$ticketId ($betType)",
                balanceAfter = newBalance
            )
        )

        return Result.success(order)
    }

    suspend fun cashOutOrder(ticketId: String, cashOutAmount: Double): Result<Unit> {
        val order = betOrderDao.getOrderByTicketId(ticketId) ?: return Result.failure(Exception("Order not found"))
        if (order.status != "OPEN") {
            return Result.failure(Exception("Ticket already settled"))
        }

        val user = userDao.getUserProfileOnce() ?: return Result.failure(Exception("User not found"))
        val newBalance = user.balance + cashOutAmount

        betOrderDao.cashOutOrder(ticketId, cashOutAmount)
        userDao.updateBalance(newBalance)
        walletDao.insertTransaction(
            WalletTransactionEntity(
                txId = "TXN-CSH-" + (10000..99999).random(),
                type = "CASHOUT",
                amount = cashOutAmount,
                description = "Cash Out: Ticket #$ticketId",
                balanceAfter = newBalance
            )
        )
        return Result.success(Unit)
    }

    suspend fun deposit(amount: Double, method: String): Result<Unit> {
        val user = userDao.getUserProfileOnce() ?: return Result.failure(Exception("User not found"))
        val newBalance = user.balance + amount
        userDao.updateBalance(newBalance)
        walletDao.insertTransaction(
            WalletTransactionEntity(
                txId = "TXN-DEP-" + (10000..99999).random(),
                type = "DEPOSIT",
                amount = amount,
                description = "Deposit via $method",
                balanceAfter = newBalance
            )
        )
        return Result.success(Unit)
    }

    suspend fun withdraw(amount: Double, method: String, accountInfo: String): Result<Unit> {
        val user = userDao.getUserProfileOnce() ?: return Result.failure(Exception("User not found"))
        if (user.balance < amount) {
            return Result.failure(Exception("Insufficient funds for withdrawal"))
        }
        val newBalance = user.balance - amount
        userDao.updateBalance(newBalance)
        walletDao.insertTransaction(
            WalletTransactionEntity(
                txId = "TXN-WTH-" + (10000..99999).random(),
                type = "WITHDRAWAL",
                amount = -amount,
                description = "Withdrawal to $method ($accountInfo)",
                balanceAfter = newBalance
            )
        )
        return Result.success(Unit)
    }

    suspend fun login(phoneOrEmail: String, pass: String): Result<Unit> {
        val user = userDao.getUserProfileOnce() ?: UserProfileEntity()
        userDao.insertOrUpdateProfile(
            user.copy(
                username = if (phoneOrEmail.contains("@")) phoneOrEmail.substringBefore("@") else "User_" + phoneOrEmail.takeLast(4),
                phoneNumber = if (!phoneOrEmail.contains("@")) phoneOrEmail else user.phoneNumber,
                email = if (phoneOrEmail.contains("@")) phoneOrEmail else user.email,
                isLoggedIn = true
            )
        )
        return Result.success(Unit)
    }

    suspend fun register(
        username: String,
        phone: String,
        email: String,
        pass: String,
        country: String = "Ghana",
        currencySymbol: String = "GH₵"
    ): Result<Unit> {
        userDao.insertOrUpdateProfile(
            UserProfileEntity(
                id = 1,
                username = username,
                phoneNumber = phone,
                email = email,
                country = country,
                balance = 0.00,
                bonusBalance = 0.00,
                vipTier = "VIP Bronze",
                isLoggedIn = true,
                currencySymbol = currencySymbol
            )
        )
        return Result.success(Unit)
    }

    suspend fun updateCountryAndCurrency(country: String, currencySymbol: String): Result<Unit> {
        val user = userDao.getUserProfileOnce() ?: return Result.failure(Exception("User not found"))
        userDao.insertOrUpdateProfile(
            user.copy(
                country = country,
                currencySymbol = currencySymbol
            )
        )
        return Result.success(Unit)
    }

    suspend fun logout() {
        val user = userDao.getUserProfileOnce() ?: return
        userDao.insertOrUpdateProfile(user.copy(isLoggedIn = false))
    }

    private fun createInitialMatches(): List<Match> {
        return listOf(
            Match(
                id = "m_1",
                sport = "Football",
                league = "UEFA Champions League",
                country = "Europe",
                homeTeam = "Real Madrid",
                awayTeam = "Manchester City",
                homeScore = 2,
                awayScore = 1,
                matchStatus = MatchStatus.LIVE,
                matchTime = "68'",
                liveMinute = 68,
                isLive = true,
                possessionHome = 48,
                possessionAway = 52,
                attacksHome = 76,
                attacksAway = 84,
                dangerousAttacksHome = 42,
                dangerousAttacksAway = 38,
                shotsOnTargetHome = 6,
                shotsOnTargetAway = 4,
                cornersHome = 5,
                cornersAway = 4,
                yellowCardsHome = 2,
                yellowCardsAway = 1,
                currentPitchAction = "Dangerous Attack by Real Madrid - Vinicius Jr on the wing",
                ballPositionPercentX = 0.72f,
                ballPositionPercentY = 0.35f,
                markets = listOf(
                    Market(
                        id = "mkt_1x2_1",
                        name = "1X2 (Match Result)",
                        outcomes = listOf(
                            MarketOutcome("1", "1 (Home)", 2.15),
                            MarketOutcome("X", "X (Draw)", 3.40),
                            MarketOutcome("2", "2 (Away)", 3.60)
                        )
                    ),
                    Market(
                        id = "mkt_ou_1",
                        name = "Over / Under 3.5 Goals",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("over", "Over 3.5", 1.85),
                            MarketOutcome("under", "Under 3.5", 1.95)
                        )
                    ),
                    Market(
                        id = "mkt_bts_1",
                        name = "Both Teams to Score",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("yes", "Yes (GG)", 1.45),
                            MarketOutcome("no", "No (NG)", 2.60)
                        )
                    ),
                    Market(
                        id = "mkt_dc_1",
                        name = "Double Chance",
                        category = "Main",
                        outcomes = listOf(
                            MarketOutcome("1x", "1X", 1.28),
                            MarketOutcome("12", "12", 1.30),
                            MarketOutcome("x2", "X2", 1.70)
                        )
                    ),
                    Market(
                        id = "mkt_next_goal_1",
                        name = "Next Goal (Goal 4)",
                        category = "Specials",
                        outcomes = listOf(
                            MarketOutcome("home", "Real Madrid", 2.20),
                            MarketOutcome("none", "No Goal", 3.10),
                            MarketOutcome("away", "Man City", 2.40)
                        )
                    )
                )
            ),
            Match(
                id = "m_2",
                sport = "Football",
                league = "Premier League",
                country = "England",
                homeTeam = "Arsenal",
                awayTeam = "Liverpool",
                homeScore = 1,
                awayScore = 0,
                matchStatus = MatchStatus.LIVE,
                matchTime = "34'",
                liveMinute = 34,
                isLive = true,
                possessionHome = 54,
                possessionAway = 46,
                attacksHome = 38,
                attacksAway = 31,
                dangerousAttacksHome = 19,
                dangerousAttacksAway = 14,
                shotsOnTargetHome = 3,
                shotsOnTargetAway = 1,
                cornersHome = 3,
                cornersAway = 1,
                yellowCardsHome = 1,
                yellowCardsAway = 0,
                currentPitchAction = "Liverpool free kick in midfield",
                ballPositionPercentX = 0.44f,
                ballPositionPercentY = 0.60f,
                markets = listOf(
                    Market(
                        id = "mkt_1x2_2",
                        name = "1X2 (Match Result)",
                        outcomes = listOf(
                            MarketOutcome("1", "1 (Home)", 1.75),
                            MarketOutcome("X", "X (Draw)", 3.80),
                            MarketOutcome("2", "2 (Away)", 4.50)
                        )
                    ),
                    Market(
                        id = "mkt_ou_2",
                        name = "Over / Under 2.5 Goals",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("over", "Over 2.5", 1.70),
                            MarketOutcome("under", "Under 2.5", 2.10)
                        )
                    ),
                    Market(
                        id = "mkt_bts_2",
                        name = "Both Teams to Score",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("yes", "Yes (GG)", 1.62),
                            MarketOutcome("no", "No (NG)", 2.20)
                        )
                    ),
                    Market(
                        id = "mkt_dc_2",
                        name = "Double Chance",
                        category = "Main",
                        outcomes = listOf(
                            MarketOutcome("1x", "1X", 1.18),
                            MarketOutcome("12", "12", 1.25),
                            MarketOutcome("x2", "X2", 2.05)
                        )
                    )
                )
            ),
            Match(
                id = "m_3",
                sport = "Football",
                league = "La Liga",
                country = "Spain",
                homeTeam = "Barcelona",
                awayTeam = "Atletico Madrid",
                homeScore = null,
                awayScore = null,
                matchStatus = MatchStatus.UPCOMING,
                matchTime = "20:45 Today",
                isLive = false,
                markets = listOf(
                    Market(
                        id = "mkt_1x2_3",
                        name = "1X2 (Match Result)",
                        outcomes = listOf(
                            MarketOutcome("1", "1 (Home)", 1.95),
                            MarketOutcome("X", "X (Draw)", 3.50),
                            MarketOutcome("2", "2 (Away)", 3.85)
                        )
                    ),
                    Market(
                        id = "mkt_ou_3",
                        name = "Over / Under 2.5 Goals",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("over", "Over 2.5", 1.80),
                            MarketOutcome("under", "Under 2.5", 2.00)
                        )
                    ),
                    Market(
                        id = "mkt_bts_3",
                        name = "Both Teams to Score",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("yes", "Yes (GG)", 1.65),
                            MarketOutcome("no", "No (NG)", 2.15)
                        )
                    )
                )
            ),
            Match(
                id = "m_4",
                sport = "Football",
                league = "Bundesliga",
                country = "Germany",
                homeTeam = "Bayern Munich",
                awayTeam = "Borussia Dortmund",
                homeScore = null,
                awayScore = null,
                matchStatus = MatchStatus.UPCOMING,
                matchTime = "18:30 Tomorrow",
                isLive = false,
                markets = listOf(
                    Market(
                        id = "mkt_1x2_4",
                        name = "1X2 (Match Result)",
                        outcomes = listOf(
                            MarketOutcome("1", "1 (Home)", 1.60),
                            MarketOutcome("X", "X (Draw)", 4.40),
                            MarketOutcome("2", "2 (Away)", 5.20)
                        )
                    ),
                    Market(
                        id = "mkt_ou_4",
                        name = "Over / Under 3.5 Goals",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("over", "Over 3.5", 2.05),
                            MarketOutcome("under", "Under 3.5", 1.72)
                        )
                    )
                )
            ),
            Match(
                id = "m_5",
                sport = "Basketball",
                league = "NBA",
                country = "USA",
                homeTeam = "LA Lakers",
                awayTeam = "Golden State Warriors",
                homeScore = 84,
                awayScore = 82,
                matchStatus = MatchStatus.LIVE,
                matchTime = "Q3 04:12",
                liveMinute = 32,
                isLive = true,
                currentPitchAction = "LeBron James drives to the basket - 2 points scored!",
                ballPositionPercentX = 0.85f,
                ballPositionPercentY = 0.50f,
                markets = listOf(
                    Market(
                        id = "mkt_1x2_5",
                        name = "Moneyline (Winner)",
                        outcomes = listOf(
                            MarketOutcome("1", "Lakers (1)", 1.80),
                            MarketOutcome("2", "Warriors (2)", 2.05)
                        )
                    ),
                    Market(
                        id = "mkt_hc_5",
                        name = "Handicap (+/- 2.5)",
                        category = "Main",
                        outcomes = listOf(
                            MarketOutcome("h1", "Lakers (-2.5)", 1.90),
                            MarketOutcome("h2", "Warriors (+2.5)", 1.90)
                        )
                    ),
                    Market(
                        id = "mkt_tot_5",
                        name = "Total Points O/U 224.5",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("over", "Over 224.5", 1.88),
                            MarketOutcome("under", "Under 224.5", 1.88)
                        )
                    )
                )
            ),
            Match(
                id = "m_6",
                sport = "Tennis",
                league = "ATP Masters 1000",
                country = "International",
                homeTeam = "Carlos Alcaraz",
                awayTeam = "Novak Djokovic",
                homeScore = 1,
                awayScore = 1,
                matchStatus = MatchStatus.LIVE,
                matchTime = "Set 3 (4-3)",
                liveMinute = 125,
                isLive = true,
                currentPitchAction = "Djokovic serving for 4-4 in 3rd set",
                markets = listOf(
                    Market(
                        id = "mkt_1x2_6",
                        name = "Match Winner",
                        outcomes = listOf(
                            MarketOutcome("1", "Alcaraz", 1.92),
                            MarketOutcome("2", "Djokovic", 1.88)
                        )
                    ),
                    Market(
                        id = "mkt_games_6",
                        name = "Total Games O/U 28.5",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("over", "Over 28.5", 1.75),
                            MarketOutcome("under", "Under 28.5", 2.05)
                        )
                    )
                )
            ),
            Match(
                id = "m_7",
                sport = "Cricket",
                league = "ICC T20 Series",
                country = "International",
                homeTeam = "India",
                awayTeam = "Australia",
                homeScore = null,
                awayScore = null,
                matchStatus = MatchStatus.UPCOMING,
                matchTime = "14:00 Today",
                isLive = false,
                markets = listOf(
                    Market(
                        id = "mkt_1x2_7",
                        name = "Match Winner",
                        outcomes = listOf(
                            MarketOutcome("1", "India", 1.70),
                            MarketOutcome("2", "Australia", 2.15)
                        )
                    ),
                    Market(
                        id = "mkt_runs_7",
                        name = "1st Innings Runs O/U 185.5",
                        category = "Goals",
                        outcomes = listOf(
                            MarketOutcome("over", "Over 185.5", 1.85),
                            MarketOutcome("under", "Under 185.5", 1.85)
                        )
                    )
                )
            ),
            Match(
                id = "m_8",
                sport = "eSports",
                league = "Counter-Strike 2 Major",
                country = "Global",
                homeTeam = "Natus Vincere",
                awayTeam = "FaZe Clan",
                homeScore = 1,
                awayScore = 0,
                matchStatus = MatchStatus.LIVE,
                matchTime = "Map 2 (11-9)",
                liveMinute = 40,
                isLive = true,
                currentPitchAction = "Bomb planted on Site A by NAVI",
                markets = listOf(
                    Market(
                        id = "mkt_1x2_8",
                        name = "Map 2 Winner",
                        outcomes = listOf(
                            MarketOutcome("1", "NAVI", 1.55),
                            MarketOutcome("2", "FaZe", 2.45)
                        )
                    )
                )
            )
        )
    }
}
