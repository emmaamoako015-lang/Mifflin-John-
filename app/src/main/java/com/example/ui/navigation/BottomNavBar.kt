package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.LivePulseRed
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavyDarkSurface
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.NavTab

data class NavItem(
    val tab: NavTab,
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int = 0,
    val hasPulse: Boolean = false
)

@Composable
fun BottomNavBar(
    currentTab: NavTab,
    openOrdersCount: Int,
    onTabSelected: (NavTab) -> Unit
) {
    val items = listOf(
        NavItem(NavTab.SPORTS, "Sports", Icons.Default.SportsSoccer),
        NavItem(NavTab.LIVE, "Live", Icons.Default.FlashOn, hasPulse = true),
        NavItem(NavTab.ROCKET, "Rocket", Icons.Default.RocketLaunch),
        NavItem(NavTab.ORDERS, "Orders", Icons.Default.ReceiptLong, badgeCount = openOrdersCount),
        NavItem(NavTab.ME, "Me", Icons.Default.Person)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyDarkSurface)
            .border(width = 1.dp, color = NavyBorder)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentTab == item.tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onTabSelected(item.tab) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag("nav_tab_${item.tab.name.lowercase()}")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) AmberGoldPrimary else TextMuted,
                            modifier = Modifier.size(24.dp)
                        )

                        if (item.hasPulse) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(LivePulseRed)
                                    .align(Alignment.TopEnd)
                            )
                        } else if (item.badgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(AmberGoldPrimary)
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.badgeCount}",
                                    color = NavyDarkBackground,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.label,
                        color = if (isSelected) AmberGoldPrimary else TextMuted,
                        fontSize = 10.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
