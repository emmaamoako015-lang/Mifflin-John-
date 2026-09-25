package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.BetOrderEntity
import com.example.data.db.Converters
import com.example.data.model.BetSelection
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.LivePulseRed
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
    orders: List<BetOrderEntity>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onCashOutClick: (ticketId: String, amount: Double) -> Unit
) {
    val filters = listOf("ALL", "OPEN", "SETTLED", "WON", "CASHED_OUT")

    val filteredOrders = when (selectedFilter) {
        "OPEN" -> orders.filter { it.status == "OPEN" }
        "SETTLED" -> orders.filter { it.status != "OPEN" }
        "WON" -> orders.filter { it.status == "WON" }
        "CASHED_OUT" -> orders.filter { it.status == "CASHED_OUT" }
        else -> orders
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .testTag("orders_screen")
    ) {
        // Top Subheader
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyDarkBackground)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Column {
                Text(
                    text = "MY BET ORDERS",
                    color = TextWhitePrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Track your live sports bets, view tickets, and cash out early",
                    color = TextMuted,
                    fontSize = 11.5.sp
                )
            }
        }

        // Filters Row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                val isSelected = selectedFilter == filter
                val label = when (filter) {
                    "ALL" -> "All Orders"
                    "OPEN" -> "Open (${orders.count { it.status == "OPEN" }})"
                    "SETTLED" -> "Settled"
                    "WON" -> "Won"
                    "CASHED_OUT" -> "Cashed Out"
                    else -> filter
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (isSelected) AmberGoldPrimary else NavyCardSurface)
                        .border(
                            1.dp,
                            if (isSelected) AmberGoldPrimary else NavyBorder,
                            RoundedCornerShape(18.dp)
                        )
                        .clickable { onFilterChange(filter) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("order_filter_${filter.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) NavyDarkBackground else TextWhitePrimary,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = TextDarker,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Orders in this Category",
                        color = TextWhitePrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Placed bets will show up here with live status & instant Cash Out.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredOrders) { order ->
                    OrderTicketCard(
                        order = order,
                        onCashOut = { onCashOutClick(order.ticketId, order.cashOutOffer) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp)) // padding for bottom bar
                }
            }
        }
    }
}

@Composable
fun OrderTicketCard(
    order: BetOrderEntity,
    onCashOut: () -> Unit
) {
    val dateStr = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
        .format(Date(order.timestamp))
    val selections = Converters.jsonToSelections(order.selectionsJson)

    val (statusBg, statusTextColor) = when (order.status) {
        "OPEN" -> Pair(Color(0x3300E5FF), ElectricCyan)
        "WON" -> Pair(Color(0x3300E676), MintEmerald)
        "LOST" -> Pair(Color(0x33FF3B30), LivePulseRed)
        "CASHED_OUT" -> Pair(Color(0x33FFB300), AmberGoldPrimary)
        else -> Pair(NavyCardElevated, TextWhitePrimary)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NavyCardSurface)
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("order_card_${order.ticketId}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Ticket Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ticket #${order.ticketId}",
                        color = TextWhitePrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "$dateStr • ${order.betType}",
                        color = TextMuted,
                        fontSize = 10.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = order.status,
                        color = statusTextColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Selections list inside ticket
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCardElevated)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selections.forEach { sel ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${sel.homeTeam} vs ${sel.awayTeam}",
                                color = TextWhitePrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${sel.marketName}: ${sel.outcomeLabel}",
                                color = ElectricCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format(Locale.US, "%.2f", sel.odds),
                                color = AmberGoldPrimary,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val (badgeBg, badgeColor, badgeText) = when (sel.selectionStatus) {
                                "WON" -> Triple(Color(0x3300E676), MintEmerald, "✓")
                                "LOST" -> Triple(Color(0x33FF3B30), LivePulseRed, "✗")
                                else -> Triple(Color(0x3300E5FF), ElectricCyan, "•")
                            }
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(badgeBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = badgeText, color = badgeColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Financial Summary Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Stake", color = TextMuted, fontSize = 10.5.sp)
                    Text(
                        text = String.format(Locale.US, "$%.2f", order.totalStake),
                        color = TextWhitePrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Total Odds", color = TextMuted, fontSize = 10.5.sp)
                    Text(
                        text = String.format(Locale.US, "%.2f", order.totalOdds),
                        color = AmberGoldPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Potential Win", color = TextMuted, fontSize = 10.5.sp)
                    Text(
                        text = String.format(Locale.US, "$%.2f", order.potentialWin),
                        color = MintEmerald,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Cash Out Section (if ticket is OPEN)
            if (order.status == "OPEN" && order.cashOutOffer > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onCashOut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("cashout_button_${order.ticketId}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Cash Out",
                        tint = NavyDarkBackground,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Cash Out " + String.format(Locale.US, "$%.2f", order.cashOutOffer),
                        color = NavyDarkBackground,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            } else if (order.status == "CASHED_OUT") {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Cashed Out at " + String.format(Locale.US, "$%.2f", order.cashOutOffer),
                    color = AmberGoldPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
