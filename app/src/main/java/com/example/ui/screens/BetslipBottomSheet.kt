package com.example.ui.screens

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BetSelection
import com.example.data.model.BetType
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.LivePulseRed
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavyDarkSurface
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetslipBottomSheet(
    selections: List<BetSelection>,
    totalOdds: Double,
    stakeInput: String,
    betType: BetType,
    bonusBoostPercent: Int,
    possibleWin: Double,
    acceptOddsChanges: Boolean,
    onStakeChange: (String) -> Unit,
    onAddStake: (Double) -> Unit,
    onBetTypeChange: (BetType) -> Unit,
    onAcceptOddsChangesChange: (Boolean) -> Unit,
    onRemoveSelection: (matchId: String, marketId: String) -> Unit,
    onClearAll: () -> Unit,
    onBookBetClick: () -> Unit,
    onLoadCodeClick: () -> Unit,
    onPlaceBetClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NavyDarkSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDarkBackground)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "BETSLIP",
                        color = TextWhitePrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AmberGoldPrimary)
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${selections.size}",
                            color = NavyDarkBackground,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (selections.isNotEmpty()) {
                        IconButton(
                            onClick = onClearAll,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("betslip_clear_all_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear all selections",
                                tint = LivePulseRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("betslip_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Betslip",
                            tint = TextWhitePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Bet Type Selector (Single vs Multiple)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCardSurface)
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (betType == BetType.MULTIPLE) AmberGoldPrimary else Color.Transparent)
                        .clickable { onBetTypeChange(BetType.MULTIPLE) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Multiple (Accumulator)",
                        color = if (betType == BetType.MULTIPLE) NavyDarkBackground else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (betType == BetType.SINGLE) AmberGoldPrimary else Color.Transparent)
                        .clickable { onBetTypeChange(BetType.SINGLE) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Single",
                        color = if (betType == BetType.SINGLE) NavyDarkBackground else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Selections list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
                    .padding(horizontal = 16.dp)
            ) {
                items(selections) { sel ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyCardSurface)
                            .border(1.dp, NavyBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${sel.homeTeam} vs ${sel.awayTeam}",
                                    color = TextWhitePrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${sel.marketName} • ${sel.outcomeLabel}",
                                    color = ElectricCyan,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = String.format(Locale.US, "%.2f", sel.odds),
                                    color = AmberGoldPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove pick",
                                    tint = TextMuted,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { onRemoveSelection(sel.matchId, sel.marketId) }
                                )
                            }
                        }
                    }
                }
            }

            // Acca Bonus Boost Indicator (SportyBet feature)
            if (bonusBoostPercent > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x2200E676))
                        .border(1.dp, Color(0x6600E676), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MintEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+$bonusBoostPercent% Multi-Bonus Boost Activated!",
                            color = MintEmerald,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick Stake Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(10.0, 50.0, 100.0, 500.0).forEach { amount ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(NavyCardElevated)
                            .border(1.dp, NavyBorder, RoundedCornerShape(6.dp))
                            .clickable { onAddStake(amount) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+$${amount.toInt()}",
                            color = TextWhitePrimary,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Stake Input Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = stakeInput,
                    onValueChange = onStakeChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stake_input_field"),
                    label = { Text("Total Stake ($)", color = TextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhitePrimary,
                        unfocusedTextColor = TextWhitePrimary,
                        focusedBorderColor = AmberGoldPrimary,
                        unfocusedBorderColor = NavyBorder,
                        focusedContainerColor = NavyCardSurface,
                        unfocusedContainerColor = NavyCardSurface
                    )
                )
            }

            // Calculation Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCardSurface)
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Odds:", color = TextMuted, fontSize = 12.sp)
                        Text(
                            text = String.format(Locale.US, "%.2f", totalOdds),
                            color = AmberGoldPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Potential Win:", color = TextMuted, fontSize = 12.sp)
                        Text(
                            text = String.format(Locale.US, "$%.2f", possibleWin),
                            color = MintEmerald,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Accept Odds Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Accept Odds Changes Automatically",
                    color = TextMuted,
                    fontSize = 11.5.sp
                )
                Switch(
                    checked = acceptOddsChanges,
                    onCheckedChange = onAcceptOddsChangesChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AmberGoldPrimary,
                        checkedTrackColor = NavyCardElevated,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = NavyDarkBackground
                    )
                )
            }

            // Bottom Buttons: Book Bet & PLACE BET
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Book Bet Button
                Button(
                    onClick = onBookBetClick,
                    modifier = Modifier
                        .weight(0.4f)
                        .height(48.dp)
                        .testTag("book_bet_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyCardElevated)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Book",
                        tint = ElectricCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "BOOK",
                        color = ElectricCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Place Bet Button
                Button(
                    onClick = onPlaceBetClick,
                    modifier = Modifier
                        .weight(0.6f)
                        .height(48.dp)
                        .testTag("place_bet_submit_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                ) {
                    Text(
                        text = "PLACE BET",
                        color = NavyDarkBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
