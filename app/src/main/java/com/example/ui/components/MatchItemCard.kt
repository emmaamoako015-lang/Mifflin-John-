package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarketOutcome
import com.example.data.model.Match
import com.example.data.model.OddsDirection
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.LivePulseRed
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.OddsBoxBg
import com.example.ui.theme.OddsBoxBorder
import com.example.ui.theme.OddsGreenUp
import com.example.ui.theme.OddsRedDown
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary
import java.util.Locale

@Composable
fun MatchItemCard(
    match: Match,
    isOutcomeSelected: (marketId: String, outcomeId: String) -> Boolean,
    onOutcomeClick: (marketId: String, marketName: String, outcomeId: String, outcomeLabel: String, odds: Double) -> Unit,
    onOpenMatchDetails: () -> Unit
) {
    var selectedMarketTab by remember { mutableStateOf("1X2") }
    var isFavorite by remember { mutableStateOf(match.isFavorite) }

    val activeMarket = when (selectedMarketTab) {
        "O/U" -> match.markets.firstOrNull { it.name.contains("Over", ignoreCase = true) } ?: match.markets.firstOrNull()
        "GG/NG" -> match.markets.firstOrNull { it.name.contains("Both", ignoreCase = true) } ?: match.markets.firstOrNull()
        "DC" -> match.markets.firstOrNull { it.name.contains("Double", ignoreCase = true) } ?: match.markets.firstOrNull()
        else -> match.markets.firstOrNull()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NavyCardSurface)
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            .testTag("match_card_${match.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header: League & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = match.league,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = " • ${match.country}",
                        color = TextDarker,
                        fontSize = 10.5.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (match.isLive) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0x33FF3B30))
                                .border(1.dp, Color(0x66FF3B30), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(LivePulseRed)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "LIVE ${match.matchTime}",
                                    color = LivePulseRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Text(
                            text = match.matchTime,
                            color = ElectricCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) AmberGoldPrimary else TextDarker,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { isFavorite = !isFavorite }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Teams & Score Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenMatchDetails() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team Names
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AmberGoldPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = match.homeTeam,
                            color = TextWhitePrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = match.awayTeam,
                            color = TextWhitePrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Scores (if Live)
                if (match.isLive && match.homeScore != null && match.awayScore != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyCardElevated)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${match.homeScore} : ${match.awayScore}",
                            color = AmberGoldPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Live pitch status ticker
            if (match.isLive && match.currentPitchAction.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x221E60FF))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = match.currentPitchAction,
                        color = Color(0xFF90CDF4),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick market switcher pills (1X2, O/U, GG/NG, DC)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("1X2", "O/U", "GG/NG", "DC").forEach { tab ->
                    val isTabActive = selectedMarketTab == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isTabActive) Color(0x33FFB300) else Color.Transparent)
                            .clickable { selectedMarketTab = tab }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = tab,
                            color = if (isTabActive) AmberGoldPrimary else TextDarker,
                            fontSize = 10.sp,
                            fontWeight = if (isTabActive) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Odds buttons row
            if (activeMarket != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    activeMarket.outcomes.forEach { outcome ->
                        val isSelected = isOutcomeSelected(activeMarket.id, outcome.id)
                        OddsButton(
                            outcome = outcome,
                            isSelected = isSelected,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onOutcomeClick(
                                    activeMarket.id,
                                    activeMarket.name,
                                    outcome.id,
                                    outcome.label,
                                    outcome.odds
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom "+48 Markets" Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenMatchDetails() }
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+${(match.markets.size * 8) + 12} Markets",
                    color = ElectricCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View all markets",
                    tint = ElectricCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun OddsButton(
    outcome: MarketOutcome,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) AmberGoldPrimary else OddsBoxBg,
        label = "odds_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) AmberGoldPrimary else OddsBoxBorder,
        label = "odds_border"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 7.dp)
            .testTag("odds_button_${outcome.id}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = outcome.label,
                color = if (isSelected) NavyDarkBackground else TextMuted,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format(Locale.US, "%.2f", outcome.odds),
                    color = if (isSelected) NavyDarkBackground else TextWhitePrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )

                if (outcome.oddsDirection == OddsDirection.UP) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = "Odds went up",
                        tint = OddsGreenUp,
                        modifier = Modifier.size(14.dp)
                    )
                } else if (outcome.oddsDirection == OddsDirection.DOWN) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Odds went down",
                        tint = OddsRedDown,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
