package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavyDarkSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary

@Composable
fun MomoUssdFlowDialog(
    amount: Double,
    currencySymbol: String,
    onTransferComplete: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    // 1: Final MoMo Approval Prompt (Enter PIN to approve)
    // 2: Transfer Approved -> Tap "TRANSFER COMPLETELY"
    var currentStep by remember { mutableIntStateOf(1) }
    var userPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("momo_ussd_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyDarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, AmberGoldPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
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
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AmberGoldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = NavyDarkBackground,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "MTN MoMo Approval",
                                color = TextWhitePrimary,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Payment Authorization Request",
                                color = AmberGoldPrimary,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (currentStep == 1) {
                    // FINAL STEP: USER MOMO APPROVAL PROMPT
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF040814))
                            .border(1.dp, AmberGoldPrimary, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "MTN-GH MOBILE MONEY",
                                    color = AmberGoldPrimary,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "APPROVAL PROMPT",
                                    color = ElectricCyan,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Authorise payment of $currencySymbol${String.format("%.2f", amount)} to Telecel Cash.\nFee: $currencySymbol 0.00.\nEnter MM PIN to approve payment:",
                                color = TextWhitePrimary,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "MoMo 4-Digit PIN:",
                                color = AmberGoldPrimary,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = userPin,
                                onValueChange = {
                                    if (it.length <= 4) {
                                        userPin = it
                                        pinError = false
                                    }
                                },
                                label = { Text("PIN", color = TextMuted) },
                                placeholder = { Text("••••", color = TextMuted) },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                singleLine = true,
                                isError = pinError,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextWhitePrimary,
                                    unfocusedTextColor = TextWhitePrimary,
                                    focusedBorderColor = AmberGoldPrimary,
                                    unfocusedBorderColor = NavyBorder
                                )
                            )

                            if (pinError) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Please enter your 4-digit PIN",
                                    color = Color(0xFFFF5252),
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    if (userPin.length >= 4) {
                                        currentStep = 2
                                    } else {
                                        pinError = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .testTag("confirm_momo_approval_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = MintEmerald),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = NavyDarkBackground)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "APPROVE PAYMENT",
                                    color = NavyDarkBackground,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                            ) {
                                Text(
                                    text = "CANCEL",
                                    color = TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                } else {
                    // STEP 2: TRANSFER APPROVED -> "TRANSFER COMPLETELY"
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MintEmerald,
                            modifier = Modifier.size(52.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "TRANSFER APPROVED!",
                            color = MintEmerald,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Payment of $currencySymbol${String.format("%.2f", amount)} successfully authorized from your MTN MoMo account.",
                            color = TextWhitePrimary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(NavyCardElevated)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(text = "Transaction Details:", color = TextMuted, fontSize = 10.5.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "TXN ID: TXN-MTN-${(100000..999999).random()}\nDestination: Telecel Cash\nAmount: $currencySymbol${String.format("%.2f", amount)}\nStatus: Approved",
                                    color = TextWhitePrimary,
                                    fontSize = 11.5.sp,
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onTransferComplete(amount)
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("transfer_completely_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "TRANSFER COMPLETELY",
                                color = NavyDarkBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
