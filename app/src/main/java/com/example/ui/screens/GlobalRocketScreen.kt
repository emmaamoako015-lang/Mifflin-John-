package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGoldDark
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricBlue
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
import java.util.Locale

@Composable
fun GlobalRocketScreen(
    multiplier: Float,
    isRunning: Boolean,
    isCrashed: Boolean,
    betPlaced: Boolean,
    betAmount: Double,
    hasCashedOut: Boolean,
    winAmount: Double,
    onStartGame: () -> Unit,
    onCashOut: () -> Unit,
    onSetBetAmount: (Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(14.dp)
            .testTag("global_rocket_screen")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFB300)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = null,
                        tint = AmberGoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "GLOBAL ROCKET",
                        color = TextWhitePrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Real-time multiplier crash arena",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0x2200E5FF))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "MAX MULTIPLIER 100x",
                    color = ElectricCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Visual Flight Canvas & Big Multiplier
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0C1938), Color(0xFF070D1E))
                    )
                )
                .border(1.dp, NavyBorder, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val progress = ((multiplier - 1.00f) / 4.0f).coerceIn(0.0f, 1.0f)
                val startX = 30.dp.toPx()
                val startY = size.height - 30.dp.toPx()
                val currentX = startX + (size.width - 60.dp.toPx()) * progress
                val currentY = startY - (size.height - 60.dp.toPx()) * (progress * progress)

                // Draw curve
                val path = Path().apply {
                    moveTo(startX, startY)
                    quadraticTo(
                        startX + (currentX - startX) * 0.5f,
                        startY,
                        currentX,
                        currentY
                    )
                }

                drawPath(
                    path = path,
                    color = if (isCrashed) LivePulseRed else ElectricCyan,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Draw rocket point
                drawCircle(
                    color = if (isCrashed) LivePulseRed else AmberGoldPrimary,
                    radius = 8.dp.toPx(),
                    center = Offset(currentX, currentY)
                )
            }

            // Central Multiplier Text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isCrashed) {
                    Text(
                        text = "CRASHED!",
                        color = LivePulseRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "@ ${String.format(Locale.US, "%.2f", multiplier)}x",
                        color = Color(0xFFFF8A80),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                } else if (isRunning) {
                    Text(
                        text = "${String.format(Locale.US, "%.2f", multiplier)}x",
                        color = AmberGoldPrimary,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "ROCKET ASCENDING...",
                        color = ElectricCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                } else {
                    Text(
                        text = "READY FOR LAUNCH",
                        color = TextMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Place your bet & cash out before crash!",
                        color = TextWhitePrimary,
                        fontSize = 11.sp
                    )
                }

                if (hasCashedOut) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xEE00E676))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "YOU WON +$$winAmount!",
                            color = NavyDarkBackground,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bet Controls Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NavyCardSurface)
                .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BET AMOUNT",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Stake: $${betAmount.toInt()}",
                        color = AmberGoldPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Amount Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10.0, 20.0, 50.0, 100.0).forEach { amt ->
                        val isSelected = betAmount == amt
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) AmberGoldPrimary else NavyCardElevated)
                                .clickable(enabled = !isRunning) { onSetBetAmount(amt) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$${amt.toInt()}",
                                color = if (isSelected) NavyDarkBackground else TextWhitePrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Button: Bet or Cash Out!
                if (isRunning && betPlaced && !hasCashedOut && !isCrashed) {
                    val potential = String.format(Locale.US, "%.2f", betAmount * multiplier)
                    Button(
                        onClick = onCashOut,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("rocket_cashout_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintEmerald)
                    ) {
                        Text(
                            text = "CASH OUT $$potential",
                            color = NavyDarkBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else {
                    Button(
                        onClick = onStartGame,
                        enabled = !isRunning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("rocket_launch_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberGoldPrimary,
                            disabledContainerColor = NavyCardElevated
                        )
                    ) {
                        Text(
                            text = if (isRunning) "FLIGHT IN PROGRESS..." else "LAUNCH ROCKET ($$betAmount)",
                            color = if (isRunning) TextMuted else NavyDarkBackground,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}
