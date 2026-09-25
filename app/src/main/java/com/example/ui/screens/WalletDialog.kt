package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.components.MomoUssdFlowDialog
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.UserProfileEntity
import com.example.data.db.WalletTransactionEntity
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavyDarkSurface
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PaymentMethodItem(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val fee: String = "Free"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDialog(
    initialTab: String = "DEPOSIT", // DEPOSIT or WITHDRAW
    userProfile: UserProfileEntity?,
    transactions: List<WalletTransactionEntity>,
    onDeposit: (amount: Double, method: String) -> Unit,
    onWithdraw: (amount: Double, method: String, accountInfo: String) -> Unit,
    onDismiss: () -> Unit
) {
    var activeTab by remember { mutableStateOf(initialTab) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val methods = listOf(
        PaymentMethodItem("momo", "Mobile Money (MTN / Momo)", Icons.Default.PhoneAndroid),
        PaymentMethodItem("card", "Credit / Debit Card", Icons.Default.CreditCard),
        PaymentMethodItem("bank", "Bank Wire Transfer", Icons.Default.AccountBalance),
        PaymentMethodItem("crypto", "Crypto (USDT / BTC)", Icons.Default.CurrencyBitcoin)
    )

    var selectedMethod by remember { mutableStateOf(methods.first()) }
    var amountInput by remember { mutableStateOf("100") }
    val curr = userProfile?.currencySymbol ?: "$"
    var accountInput by remember { mutableStateOf(userProfile?.phoneNumber ?: "+1 555-019-7832") }
    var showMomoUssdFlow by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NavyDarkSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDarkBackground)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GLOBAL WALLET & CASHIER",
                        color = TextWhitePrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Available Balance: " + String.format(Locale.US, "%s%.2f", curr, userProfile?.balance ?: 0.0),
                        color = AmberGoldPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_wallet_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhitePrimary
                    )
                }
            }

            // Tab bar (Deposit, Withdraw, Transactions)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCardSurface)
                    .padding(4.dp)
            ) {
                listOf("DEPOSIT", "WITHDRAW", "HISTORY").forEach { tab ->
                    val isSelected = activeTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) AmberGoldPrimary else Color.Transparent)
                            .clickable { activeTab = tab }
                            .padding(vertical = 7.dp)
                            .testTag("wallet_tab_${tab.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) NavyDarkBackground else TextMuted,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (activeTab == "DEPOSIT") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "SELECT DEPOSIT METHOD",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    methods.forEach { method ->
                        val isSelected = selectedMethod.id == method.id
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NavyCardElevated else NavyCardSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) AmberGoldPrimary else NavyBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedMethod = method }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = method.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) AmberGoldPrimary else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = method.name,
                                        color = TextWhitePrimary,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(text = method.fee, color = MintEmerald, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (selectedMethod.id == "momo") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2200E5FF))
                                .border(1.dp, Color(0x6600E5FF), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "MTN MOBILE MONEY DEPOSIT",
                                    color = AmberGoldPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "A secure MTN MoMo approval prompt will appear. Enter your PIN to approve the transfer and tap Transfer Completely.",
                                    color = TextWhitePrimary,
                                    fontSize = 10.5.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Preset Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(50.0, 100.0, 200.0, 500.0).forEach { amt ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NavyCardElevated)
                                    .border(1.dp, NavyBorder, RoundedCornerShape(6.dp))
                                    .clickable { amountInput = amt.toInt().toString() }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$curr${amt.toInt()}",
                                    color = TextWhitePrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("deposit_amount_field"),
                        label = { Text("Deposit Amount ($curr)", color = TextMuted) },
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

                    Spacer(modifier = Modifier.height(14.dp))

                    if (selectedMethod.id == "momo") {
                        Button(
                            onClick = { showMomoUssdFlow = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("ussd_170_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                        ) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = NavyDarkBackground)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DEPOSIT $curr$amountInput VIA MOMO",
                                color = NavyDarkBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                val amt = amountInput.toDoubleOrNull() ?: 0.0
                                if (amt > 0) onDeposit(amt, selectedMethod.name)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("deposit_confirm_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                        ) {
                            Text(
                                text = "INSTANT DEPOSIT $curr$amountInput",
                                color = NavyDarkBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            } else if (activeTab == "WITHDRAW") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "PAYOUT DESTINATION",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    methods.take(3).forEach { method ->
                        val isSelected = selectedMethod.id == method.id
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NavyCardElevated else NavyCardSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) AmberGoldPrimary else NavyBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedMethod = method }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = method.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) AmberGoldPrimary else TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = method.name,
                                    color = TextWhitePrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = accountInput,
                        onValueChange = { accountInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_account_field"),
                        label = { Text("Account Number / Phone Number", color = TextMuted) },
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

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_amount_field"),
                        label = { Text("Withdraw Amount ($)", color = TextMuted) },
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

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val amt = amountInput.toDoubleOrNull() ?: 0.0
                            if (amt > 0) onWithdraw(amt, selectedMethod.name, accountInput)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("withdraw_confirm_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                    ) {
                        Text(
                            text = "REQUEST WITHDRAWAL $$amountInput",
                            color = NavyDarkBackground,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            } else {
                // TRANSACTIONS HISTORY
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(280.dp)
                ) {
                    if (transactions.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No wallet transactions yet.", color = TextMuted)
                            }
                        }
                    } else {
                        items(transactions) { txn ->
                            val dateStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                                .format(Date(txn.timestamp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NavyCardSurface)
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = txn.description,
                                            color = TextWhitePrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "$dateStr • ${txn.txId}",
                                            color = TextMuted,
                                            fontSize = 10.5.sp
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = if (txn.amount > 0) "+$curr${String.format(Locale.US, "%.2f", txn.amount)}" else "-$curr${String.format(Locale.US, "%.2f", -txn.amount)}",
                                            color = if (txn.amount > 0) MintEmerald else AmberGoldPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Bal: $curr${String.format(Locale.US, "%.2f", txn.balanceAfter)}",
                                            color = TextDarker,
                                            fontSize = 10.5.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showMomoUssdFlow) {
        val depositAmt = amountInput.toDoubleOrNull() ?: 100.0
        MomoUssdFlowDialog(
            amount = depositAmt,
            currencySymbol = curr,
            onTransferComplete = { finalAmt ->
                onDeposit(finalAmt, "MTN MoMo (*170#) via Telecel")
                showMomoUssdFlow = false
                onDismiss()
            },
            onDismiss = { showMomoUssdFlow = false }
        )
    }
}
