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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.BetOrderEntity
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary
import java.util.Locale

@Composable
fun BookingCodeDialog(
    code: String,
    onDismiss: () -> Unit
) {
    var copied by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NavyCardSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null,
                    tint = AmberGoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BET BOOKED SUCCESSFULLY",
                    color = TextWhitePrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Share this Booking Code with friends or use it on any device to reload your selections instantly:",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavyCardElevated)
                        .border(1.dp, AmberGoldPrimary, RoundedCornerShape(8.dp))
                        .clickable { copied = true }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = code,
                            color = AmberGoldPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy code",
                            tint = if (copied) MintEmerald else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                if (copied) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "✓ Booking Code copied to clipboard!",
                        color = MintEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "DONE", color = NavyDarkBackground, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun LoadCodeDialog(
    onLoadCode: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var codeInput by remember { mutableStateOf("GB-8924X") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NavyCardSurface,
        title = {
            Text(
                text = "LOAD BET BOOKING CODE",
                color = TextWhitePrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Enter a shared Global Betting booking code to load picks into your betslip:",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = codeInput,
                    onValueChange = { codeInput = it.uppercase() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("load_code_input_field"),
                    label = { Text("Booking Code (e.g. GB-8924X)", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhitePrimary,
                        unfocusedTextColor = TextWhitePrimary,
                        focusedBorderColor = AmberGoldPrimary,
                        unfocusedBorderColor = NavyBorder,
                        focusedContainerColor = NavyCardElevated,
                        unfocusedContainerColor = NavyCardElevated
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onLoadCode(codeInput) },
                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "LOAD PICKS", color = NavyDarkBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = TextMuted)
            }
        }
    )
}

@Composable
fun BetPlacedReceiptDialog(
    order: BetOrderEntity,
    onViewOrders: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NavyCardSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MintEmerald,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "BET PLACED!",
                        color = TextWhitePrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Ticket #${order.ticketId}",
                        color = ElectricCyan,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCardElevated)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Bet Type:", color = TextMuted, fontSize = 12.sp)
                    Text(text = order.betType, color = TextWhitePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Stake:", color = TextMuted, fontSize = 12.sp)
                    Text(
                        text = String.format(Locale.US, "$%.2f", order.totalStake),
                        color = TextWhitePrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Odds:", color = TextMuted, fontSize = 12.sp)
                    Text(
                        text = String.format(Locale.US, "%.2f", order.totalOdds),
                        color = AmberGoldPrimary,
                        fontSize = 12.sp,
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
                        text = String.format(Locale.US, "$%.2f", order.potentialWin),
                        color = MintEmerald,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onViewOrders()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("receipt_view_orders_button")
            ) {
                Text(text = "VIEW MY ORDERS", color = NavyDarkBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close", color = TextMuted)
            }
        }
    )
}

@Composable
fun WelcomeDepositDialog(
    currencySymbol: String = "GH₵",
    onSelectAmount: (Double) -> Unit,
    onSkip: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onSkip,
        containerColor = NavyCardSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = AmberGoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WELCOME DEPOSIT BONUS",
                    color = TextWhitePrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Claim your 150% Welcome Match Bonus on your initial deposit! Fund your account now to start betting, or skip and explore the sportsbook first.",
                    color = TextMuted,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(50.0, 100.0, 200.0).forEach { amt ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NavyCardElevated)
                                .border(1.dp, AmberGoldPrimary, RoundedCornerShape(8.dp))
                                .clickable { onSelectAmount(amt) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$currencySymbol${amt.toInt()}",
                                    color = AmberGoldPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "+$currencySymbol${(amt * 1.5).toInt()} Free",
                                    color = MintEmerald,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSelectAmount(100.0) },
                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("welcome_deposit_now_button")
            ) {
                Text(text = "DEPOSIT NOW", color = NavyDarkBackground, fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onSkip,
                modifier = Modifier.testTag("welcome_deposit_skip_button")
            ) {
                Text(text = "Skip & Explore", color = TextMuted)
            }
        }
    )
}

