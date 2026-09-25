package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BetPlacedReceiptDialog
import com.example.ui.components.BookingCodeDialog
import com.example.ui.components.FloatingBetslipBar
import com.example.ui.components.LoadCodeDialog
import com.example.ui.components.MatchDetailDialog
import com.example.ui.components.MatchItemCard
import com.example.ui.components.SearchBarRow
import com.example.ui.components.SportFilterBar
import com.example.ui.components.SportsPromoHero
import com.example.ui.components.TopHeaderBar
import com.example.ui.components.WelcomeDepositDialog
import com.example.ui.navigation.BottomNavBar
import com.example.ui.screens.AuthDialog
import com.example.ui.screens.AuthGateScreen
import com.example.ui.screens.BetslipBottomSheet
import com.example.ui.screens.GlobalRocketScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WalletDialog
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.BettingViewModel
import com.example.ui.viewmodel.NavTab

@Composable
fun MainBettingScreen(viewModel: BettingViewModel = viewModel()) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val walletTxns by viewModel.walletTransactions.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedSport by viewModel.selectedSport.collectAsStateWithLifecycle()
    val selectedTimeFilter by viewModel.selectedTimeFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isBalanceHidden by viewModel.isBalanceHidden.collectAsStateWithLifecycle()
    val matches by viewModel.filteredMatches.collectAsStateWithLifecycle()

    val selections by viewModel.selections.collectAsStateWithLifecycle()
    val isBetslipOpen by viewModel.isBetslipOpen.collectAsStateWithLifecycle()
    val stakeInput by viewModel.stakeInput.collectAsStateWithLifecycle()
    val betType by viewModel.betType.collectAsStateWithLifecycle()
    val acceptOddsChanges by viewModel.acceptOddsChanges.collectAsStateWithLifecycle()
    val totalOdds by viewModel.totalOdds.collectAsStateWithLifecycle()
    val bonusBoostPercent by viewModel.bonusBoostPercent.collectAsStateWithLifecycle()
    val possibleWin by viewModel.possibleWin.collectAsStateWithLifecycle()

    val detailedMatch by viewModel.detailedMatch.collectAsStateWithLifecycle()
    val isDepositOpen by viewModel.isDepositOpen.collectAsStateWithLifecycle()
    val isWithdrawOpen by viewModel.isWithdrawOpen.collectAsStateWithLifecycle()
    val isAuthOpen by viewModel.isAuthOpen.collectAsStateWithLifecycle()
    val authMode by viewModel.authMode.collectAsStateWithLifecycle()
    val bookingCodeDialog by viewModel.bookingCodeDialog.collectAsStateWithLifecycle()
    val isLoadCodeOpen by viewModel.isLoadCodeOpen.collectAsStateWithLifecycle()
    val ticketSuccessOrder by viewModel.ticketSuccessOrder.collectAsStateWithLifecycle()
    val snackBarMsg by viewModel.snackBarMessage.collectAsStateWithLifecycle()
    val ordersFilter by viewModel.ordersFilter.collectAsStateWithLifecycle()
    val showWelcomeDepositPrompt by viewModel.showWelcomeDepositPrompt.collectAsStateWithLifecycle()

    // Rocket game state
    val rocketMultiplier by viewModel.rocketMultiplier.collectAsStateWithLifecycle()
    val isRocketRunning by viewModel.isRocketRunning.collectAsStateWithLifecycle()
    val isRocketCrashed by viewModel.isRocketCrashed.collectAsStateWithLifecycle()
    val rocketBetPlaced by viewModel.rocketBetPlaced.collectAsStateWithLifecycle()
    val rocketBetAmount by viewModel.rocketBetAmount.collectAsStateWithLifecycle()
    val rocketCashedOut by viewModel.rocketCashedOut.collectAsStateWithLifecycle()
    val rocketWinAmount by viewModel.rocketWinAmount.collectAsStateWithLifecycle()

    var isSearchVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackBarMsg) {
        snackBarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackBar()
        }
    }

    val openOrdersCount = allOrders.count { it.status == "OPEN" }

    if (userProfile?.isLoggedIn != true) {
        AuthGateScreen(
            onLogin = { id, pass -> viewModel.login(id, pass) },
            onRegister = { uname, phone, email, pass, country, currency ->
                viewModel.register(uname, phone, email, pass, country, currency)
            }
        )
    } else {
        Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NavyDarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopHeaderBar(
                    userProfile = userProfile,
                    isBalanceHidden = isBalanceHidden,
                    onToggleBalance = { viewModel.toggleBalanceVisibility() },
                    onDepositClick = { viewModel.openDeposit() },
                    onProfileClick = { viewModel.setTab(NavTab.ME) },
                    onAuthClick = { viewModel.openAuth() },
                    onSearchClick = { isSearchVisible = !isSearchVisible }
                )
                SearchBarRow(
                    visible = isSearchVisible,
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    onClose = {
                        viewModel.setSearchQuery("")
                        isSearchVisible = false
                    }
                )
            }
        },
        bottomBar = {
            Column {
                // Floating betslip button above navigation bar
                FloatingBetslipBar(
                    selectionCount = selections.size,
                    totalOdds = totalOdds,
                    onClick = { viewModel.setBetslipOpen(true) }
                )

                BottomNavBar(
                    currentTab = currentTab,
                    openOrdersCount = openOrdersCount,
                    onTabSelected = { tab ->
                        viewModel.setTab(tab)
                        if (tab == NavTab.LIVE) {
                            viewModel.setSelectedTimeFilter("Live")
                        } else if (tab == NavTab.SPORTS) {
                            viewModel.setSelectedTimeFilter("All")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(NavyDarkBackground)
        ) {
            when (currentTab) {
                NavTab.SPORTS, NavTab.LIVE -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        if (currentTab == NavTab.SPORTS) {
                            item {
                                SportsPromoHero(
                                    onLiveClick = {
                                        viewModel.setTab(NavTab.LIVE)
                                        viewModel.setSelectedTimeFilter("Live")
                                    },
                                    onRocketClick = { viewModel.setTab(NavTab.ROCKET) },
                                    onBookBetClick = { viewModel.bookABet() },
                                    onLoadCodeClick = { viewModel.openLoadCodeDialog() }
                                )
                            }
                        }

                        item {
                            SportFilterBar(
                                selectedSport = selectedSport,
                                onSportSelected = { viewModel.setSelectedSport(it) },
                                selectedTimeFilter = selectedTimeFilter,
                                onTimeFilterSelected = { viewModel.setSelectedTimeFilter(it) }
                            )
                        }

                        if (matches.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No matches found matching your filters.",
                                        color = TextMuted,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        } else {
                            items(matches) { match ->
                                MatchItemCard(
                                    match = match,
                                    isOutcomeSelected = { mId, oId -> viewModel.isSelected(match.id, mId, oId) },
                                    onOutcomeClick = { mId, mName, oId, oLabel, odds ->
                                        viewModel.toggleSelection(match, mId, mName, oId, oLabel, odds)
                                    },
                                    onOpenMatchDetails = { viewModel.openMatchDetail(match) }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }

                NavTab.ROCKET -> {
                    GlobalRocketScreen(
                        multiplier = rocketMultiplier,
                        isRunning = isRocketRunning,
                        isCrashed = isRocketCrashed,
                        betPlaced = rocketBetPlaced,
                        betAmount = rocketBetAmount,
                        hasCashedOut = rocketCashedOut,
                        winAmount = rocketWinAmount,
                        onStartGame = { viewModel.startRocketGame() },
                        onCashOut = { viewModel.cashOutRocket() },
                        onSetBetAmount = { viewModel.setRocketBetAmount(it) }
                    )
                }

                NavTab.ORDERS -> {
                    OrdersScreen(
                        orders = allOrders,
                        selectedFilter = ordersFilter,
                        onFilterChange = { viewModel.setOrdersFilter(it) },
                        onCashOutClick = { ticketId, amount -> viewModel.cashOut(ticketId, amount) }
                    )
                }

                NavTab.ME -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        onDepositClick = { viewModel.openDeposit() },
                        onWithdrawClick = { viewModel.openWithdraw() },
                        onAuthClick = { viewModel.openAuth() },
                        onLogoutClick = { viewModel.logout() }
                    )
                }
            }

            // Bottom Sheets & Modals
            if (isBetslipOpen) {
                BetslipBottomSheet(
                    selections = selections,
                    totalOdds = totalOdds,
                    stakeInput = stakeInput,
                    betType = betType,
                    bonusBoostPercent = bonusBoostPercent,
                    possibleWin = possibleWin,
                    acceptOddsChanges = acceptOddsChanges,
                    onStakeChange = { viewModel.setStakeInput(it) },
                    onAddStake = { viewModel.addStakeAmount(it) },
                    onBetTypeChange = { viewModel.setBetType(it) },
                    onAcceptOddsChangesChange = { viewModel.setAcceptOddsChanges(it) },
                    onRemoveSelection = { matchId, marketId -> viewModel.removeSelection(matchId, marketId) },
                    onClearAll = { viewModel.clearBetslip() },
                    onBookBetClick = { viewModel.bookABet() },
                    onLoadCodeClick = { viewModel.openLoadCodeDialog() },
                    onPlaceBetClick = { viewModel.placeBet() },
                    onDismiss = { viewModel.setBetslipOpen(false) }
                )
            }

            if (detailedMatch != null) {
                MatchDetailDialog(
                    match = detailedMatch!!,
                    isOutcomeSelected = { mId, oId -> viewModel.isSelected(detailedMatch!!.id, mId, oId) },
                    onOutcomeClick = { mId, mName, oId, oLabel, odds ->
                        viewModel.toggleSelection(detailedMatch!!, mId, mName, oId, oLabel, odds)
                    },
                    onDismiss = { viewModel.closeMatchDetail() }
                )
            }

            if (isDepositOpen) {
                WalletDialog(
                    initialTab = "DEPOSIT",
                    userProfile = userProfile,
                    transactions = walletTxns,
                    onDeposit = { amt, method -> viewModel.deposit(amt, method) },
                    onWithdraw = { amt, method, acc -> viewModel.withdraw(amt, method, acc) },
                    onDismiss = { viewModel.closeDeposit() }
                )
            }

            if (isWithdrawOpen) {
                WalletDialog(
                    initialTab = "WITHDRAW",
                    userProfile = userProfile,
                    transactions = walletTxns,
                    onDeposit = { amt, method -> viewModel.deposit(amt, method) },
                    onWithdraw = { amt, method, acc -> viewModel.withdraw(amt, method, acc) },
                    onDismiss = { viewModel.closeWithdraw() }
                )
            }

            if (isAuthOpen) {
                AuthDialog(
                    initialMode = authMode,
                    onLogin = { id, pass -> viewModel.login(id, pass) },
                    onRegister = { uname, phone, email, pass, country, currency ->
                        viewModel.register(uname, phone, email, pass, country, currency)
                    },
                    onDismiss = { viewModel.closeAuth() }
                )
            }

            bookingCodeDialog?.let { code ->
                BookingCodeDialog(
                    code = code,
                    onDismiss = { viewModel.closeBookingDialog() }
                )
            }

            if (isLoadCodeOpen) {
                LoadCodeDialog(
                    onLoadCode = { viewModel.loadBookingCode(it) },
                    onDismiss = { viewModel.closeLoadCodeDialog() }
                )
            }

            ticketSuccessOrder?.let { order ->
                BetPlacedReceiptDialog(
                    order = order,
                    onViewOrders = {
                        viewModel.setTab(NavTab.ORDERS)
                        viewModel.setOrdersFilter("OPEN")
                    },
                    onDismiss = { viewModel.closeReceipt() }
                )
            }

            if (showWelcomeDepositPrompt) {
                WelcomeDepositDialog(
                    currencySymbol = userProfile?.currencySymbol ?: "GH₵",
                    onSelectAmount = { amt -> viewModel.acceptWelcomeDeposit(amt) },
                    onSkip = { viewModel.dismissWelcomeDeposit() }
                )
            }
        }
    }
}
}
