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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.UserProfileEntity
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
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
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    onDepositClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onAuthClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var selectedOddsFormat by remember { mutableStateOf("Decimal (1.85)") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
            .testTag("profile_screen")
    ) {
        // User Info Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NavyCardSurface)
                .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            if (userProfile?.isLoggedIn == true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(NavyCardElevated)
                            .border(2.dp, AmberGoldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = AmberGoldPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.username,
                                color = TextWhitePrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Account",
                                tint = MintEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = userProfile.email,
                            color = TextMuted,
                            fontSize = 11.5.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AmberGoldPrimary)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = userProfile.vipTier,
                                    color = NavyDarkBackground,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NavyCardElevated)
                                    .border(1.dp, NavyBorder, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${userProfile.country} • ${userProfile.currencySymbol}",
                                    color = ElectricCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Join Global Betting",
                        color = TextWhitePrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Log in to place bets, manage wallet, and cash out orders",
                        color = TextMuted,
                        fontSize = 11.5.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onAuthClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "LOG IN / REGISTER", color = NavyDarkBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Balance Breakdown Card
        if (userProfile?.isLoggedIn == true) {
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
                        Column {
                            Text(text = "Main Cash Balance", color = TextMuted, fontSize = 11.5.sp)
                            Text(
                                text = String.format(Locale.US, "%s%.2f", userProfile.currencySymbol, userProfile.balance),
                                color = TextWhitePrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Bonus Credits", color = TextMuted, fontSize = 11.5.sp)
                            Text(
                                text = String.format(Locale.US, "%s%.2f", userProfile.currencySymbol, userProfile.bonusBalance),
                                color = AmberGoldPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onDepositClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("profile_deposit_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = NavyDarkBackground, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "DEPOSIT", color = NavyDarkBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = onWithdrawClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("profile_withdraw_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyCardElevated)
                        ) {
                            Text(text = "WITHDRAW", color = TextWhitePrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Settings Section
        Text(
            text = "BETTING PREFERENCES",
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NavyCardSurface)
                .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
        ) {
            ProfileSettingRow(
                icon = Icons.Default.AccountBalanceWallet,
                title = "Odds Format",
                value = selectedOddsFormat,
                onClick = {
                    selectedOddsFormat = when (selectedOddsFormat) {
                        "Decimal (1.85)" -> "Fractional (5/6)"
                        "Fractional (5/6)" -> "American (-118)"
                        else -> "Decimal (1.85)"
                    }
                }
            )

            ProfileSettingRow(
                icon = Icons.Default.Shield,
                title = "Responsible Gaming Limits",
                value = "$5,000 / Day",
                onClick = { }
            )

            ProfileSettingRow(
                icon = Icons.Default.Security,
                title = "Biometric Login / Security",
                value = "Enabled",
                onClick = { }
            )

            ProfileSettingRow(
                icon = Icons.Default.Notifications,
                title = "Live Match Alerts",
                value = "Goals & Cashout",
                onClick = { }
            )

            ProfileSettingRow(
                icon = Icons.Default.HelpOutline,
                title = "24/7 Global Customer Support",
                value = "Live Chat",
                onClick = { }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (userProfile?.isLoggedIn == true) {
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("logout_button"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF3B30))
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = Color(0xFFFF8A80),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LOG OUT OF GLOBAL BETTING",
                    color = Color(0xFFFF8A80),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ProfileSettingRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AmberGoldPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = TextWhitePrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                color = TextMuted,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = TextDarker,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
