package com.example.ui.components

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.Star
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
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary

data class SportItem(
    val name: String,
    val icon: ImageVector
)

@Composable
fun SportFilterBar(
    selectedSport: String,
    onSportSelected: (String) -> Unit,
    selectedTimeFilter: String,
    onTimeFilterSelected: (String) -> Unit
) {
    val sports = listOf(
        SportItem("All", Icons.Default.Star),
        SportItem("Football", Icons.Default.SportsSoccer),
        SportItem("Basketball", Icons.Default.SportsBasketball),
        SportItem("Tennis", Icons.Default.SportsTennis),
        SportItem("Cricket", Icons.Default.SportsCricket),
        SportItem("eSports", Icons.Default.Gamepad)
    )

    val timeFilters = listOf("All", "Live", "Today", "Upcoming")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Sports horizontal pills
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sports) { sport ->
                val isSelected = selectedSport.equals(sport.name, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) AmberGoldPrimary else NavyCardSurface)
                        .border(
                            1.dp,
                            if (isSelected) AmberGoldPrimary else NavyBorder,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onSportSelected(sport.name) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("sport_tab_${sport.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = sport.icon,
                            contentDescription = sport.name,
                            tint = if (isSelected) NavyDarkBackground else TextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = sport.name,
                            color = if (isSelected) NavyDarkBackground else TextWhitePrimary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time Filters Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            timeFilters.forEach { filter ->
                val isSelected = selectedTimeFilter.equals(filter, ignoreCase = true)
                val isLiveFilter = filter == "Live"

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) NavyCardElevated else Color.Transparent)
                        .border(
                            1.dp,
                            if (isSelected) AmberGoldPrimary else NavyBorder,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { onTimeFilterSelected(filter) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("time_filter_${filter.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLiveFilter) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(LivePulseRed)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = filter,
                            color = if (isSelected) AmberGoldPrimary else TextMuted,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
