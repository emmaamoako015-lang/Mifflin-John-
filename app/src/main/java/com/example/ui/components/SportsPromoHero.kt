package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.LivePulseRed
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary

@Composable
fun SportsPromoHero(
    onLiveClick: () -> Unit,
    onRocketClick: () -> Unit,
    onBookBetClick: () -> Unit,
    onLoadCodeClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Hero Card with generated visual asset
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .height(138.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.global_sports_hero_1790291770162),
                contentDescription = "Championship Hero Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // Gradient Overlay for text readability
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xEE070D1E),
                                Color(0x99070D1E),
                                Color(0x33000000)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(AmberGoldPrimary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "HOT ACCUMULATOR BOOST",
                        color = Color(0xFF070D1E),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "UP TO +15% MULTI WIN",
                    color = TextWhitePrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "Bet on 3+ selections & receive real bonus payout instantly!",
                    color = Color(0xFFE2E8F0),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(220.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // SportyBet-style Horizontal Quick Action Bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                QuickActionButton(
                    icon = Icons.Default.FlashOn,
                    label = "Live In-Play",
                    iconColor = LivePulseRed,
                    hasPulse = true,
                    onClick = onLiveClick,
                    testTag = "quick_live_button"
                )
            }
            item {
                QuickActionButton(
                    icon = Icons.Default.RocketLaunch,
                    label = "Global Rocket",
                    iconColor = AmberGoldPrimary,
                    onClick = onRocketClick,
                    testTag = "quick_rocket_button"
                )
            }
            item {
                QuickActionButton(
                    icon = Icons.Default.AutoAwesome,
                    label = "Acca Boost",
                    iconColor = MintEmerald,
                    onClick = { },
                    testTag = "quick_boost_button"
                )
            }
            item {
                QuickActionButton(
                    icon = Icons.Default.BookmarkBorder,
                    label = "Book a Bet",
                    iconColor = ElectricCyan,
                    onClick = onBookBetClick,
                    testTag = "quick_book_bet_button"
                )
            }
            item {
                QuickActionButton(
                    icon = Icons.Default.QrCode,
                    label = "Load Code",
                    iconColor = ElectricBlue,
                    onClick = onLoadCodeClick,
                    testTag = "quick_load_code_button"
                )
            }
            item {
                QuickActionButton(
                    icon = Icons.Default.MilitaryTech,
                    label = "Jackpot $50K",
                    iconColor = AmberGoldPrimary,
                    onClick = { },
                    testTag = "quick_jackpot_button"
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    iconColor: Color,
    hasPulse: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(NavyCardSurface)
                .border(1.dp, NavyBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )

            if (hasPulse) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(LivePulseRed)
                        .align(Alignment.TopEnd)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = TextWhitePrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
