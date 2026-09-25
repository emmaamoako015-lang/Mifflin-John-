package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Match
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.LivePulseRed
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavyDarkSurface
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailDialog(
    match: Match,
    isOutcomeSelected: (marketId: String, outcomeId: String) -> Boolean,
    onOutcomeClick: (marketId: String, marketName: String, outcomeId: String, outcomeLabel: String, odds: Double) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedCategory by remember { mutableStateOf("All") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NavyDarkSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDarkBackground)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = match.league,
                        color = AmberGoldPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${match.homeTeam} vs ${match.awayTeam}",
                        color = TextWhitePrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_match_details_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhitePrimary
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    // Pitch Visualizer Card
                    PitchVisualizer(match = match)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Live Statistics Row
                    MatchStatsSection(match = match)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Categories Selector
                    val categories = listOf("All", "Main", "Goals", "Halftime", "Specials")
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { cat ->
                            val isSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) AmberGoldPrimary else NavyCardSurface)
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) NavyDarkBackground else TextWhitePrimary,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Filtered markets
                val filteredMarkets = match.markets.filter {
                    selectedCategory == "All" || it.category.equals(selectedCategory, ignoreCase = true)
                }

                items(filteredMarkets) { market ->
                    MarketCardSection(
                        market = market,
                        isOutcomeSelected = isOutcomeSelected,
                        onOutcomeClick = onOutcomeClick
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun PitchVisualizer(match: Match) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F3D24)) // Deep pitch turf green
            .border(1.dp, Color(0xFF1B5E20), RoundedCornerShape(12.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 1.5.dp.toPx())
            val lineColor = Color(0x66FFFFFF)

            // Outer border
            drawRect(
                color = lineColor,
                topLeft = Offset(12.dp.toPx(), 12.dp.toPx()),
                size = Size(size.width - 24.dp.toPx(), size.height - 24.dp.toPx()),
                style = stroke
            )

            // Halfway line
            drawLine(
                color = lineColor,
                start = Offset(size.width / 2, 12.dp.toPx()),
                end = Offset(size.width / 2, size.height - 12.dp.toPx()),
                strokeWidth = 1.5.dp.toPx()
            )

            // Center circle
            drawCircle(
                color = lineColor,
                radius = 24.dp.toPx(),
                center = Offset(size.width / 2, size.height / 2),
                style = stroke
            )

            // Left penalty box
            drawRect(
                color = lineColor,
                topLeft = Offset(12.dp.toPx(), size.height / 2 - 32.dp.toPx()),
                size = Size(36.dp.toPx(), 64.dp.toPx()),
                style = stroke
            )

            // Right penalty box
            drawRect(
                color = lineColor,
                topLeft = Offset(size.width - 48.dp.toPx(), size.height / 2 - 32.dp.toPx()),
                size = Size(36.dp.toPx(), 64.dp.toPx()),
                style = stroke
            )

            // Ball Position Indicator
            val ballX = size.width * match.ballPositionPercentX
            val ballY = size.height * match.ballPositionPercentY
            // Outer glow
            drawCircle(
                color = Color(0x66FFB300),
                radius = 10.dp.toPx(),
                center = Offset(ballX, ballY)
            )
            // Ball dot
            drawCircle(
                color = Color(0xFFFFB300),
                radius = 4.dp.toPx(),
                center = Offset(ballX, ballY)
            )
        }

        // Pitch Overlay Info
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = match.homeTeam,
                    color = TextWhitePrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                if (match.isLive) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xCCFF3B30))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${match.liveMinute ?: 45}' LIVE",
                            color = TextWhitePrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Text(
                    text = match.awayTeam,
                    color = TextWhitePrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Live event ticker badge
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xDD070D1E))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = match.currentPitchAction,
                    color = ElectricCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Bottom scores / info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Score: ${match.homeScore ?: 0}",
                    color = TextWhitePrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Score: ${match.awayScore ?: 0}",
                    color = TextWhitePrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun MatchStatsSection(match: Match) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(NavyCardSurface)
            .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            text = "LIVE MATCH STATS",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Possession Bar
        StatBar(
            title = "Possession",
            homeVal = "${match.possessionHome}%",
            awayVal = "${match.possessionAway}%",
            homePercent = match.possessionHome / 100f
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Dangerous Attacks
        StatBar(
            title = "Dangerous Attacks",
            homeVal = "${match.dangerousAttacksHome}",
            awayVal = "${match.dangerousAttacksAway}",
            homePercent = (match.dangerousAttacksHome.toFloat() / (match.dangerousAttacksHome + match.dangerousAttacksAway).coerceAtLeast(1))
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Shots on Target
        StatBar(
            title = "Shots on Target",
            homeVal = "${match.shotsOnTargetHome}",
            awayVal = "${match.shotsOnTargetAway}",
            homePercent = (match.shotsOnTargetHome.toFloat() / (match.shotsOnTargetHome + match.shotsOnTargetAway).coerceAtLeast(1))
        )
    }
}

@Composable
fun StatBar(
    title: String,
    homeVal: String,
    awayVal: String,
    homePercent: Float
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = homeVal, color = AmberGoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = title, color = TextMuted, fontSize = 10.5.sp)
            Text(text = awayVal, color = ElectricCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(3.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(NavyCardElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(homePercent.coerceIn(0.1f, 0.9f))
                    .height(6.dp)
                    .background(AmberGoldPrimary)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ElectricCyan)
            )
        }
    }
}

@Composable
fun MarketCardSection(
    market: com.example.data.model.Market,
    isOutcomeSelected: (marketId: String, outcomeId: String) -> Boolean,
    onOutcomeClick: (marketId: String, marketName: String, outcomeId: String, outcomeLabel: String, odds: Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(NavyCardSurface)
            .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(
            text = market.name,
            color = TextWhitePrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            market.outcomes.forEach { outcome ->
                val isSelected = isOutcomeSelected(market.id, outcome.id)
                OddsButton(
                    outcome = outcome,
                    isSelected = isSelected,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onOutcomeClick(
                            market.id,
                            market.name,
                            outcome.id,
                            outcome.label,
                            outcome.odds
                        )
                    }
                )
            }
        }
    }
}
