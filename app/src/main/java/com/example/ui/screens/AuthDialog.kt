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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavyDarkSurface
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Flag
import com.example.data.model.CountryOption
import com.example.data.model.SUPPORTED_COUNTRIES
import com.example.ui.theme.TextDarker
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthDialog(
    initialMode: String = "LOGIN",
    onLogin: (phoneOrEmail: String, pass: String) -> Unit,
    onRegister: (username: String, phone: String, email: String, pass: String, country: String, currencySymbol: String) -> Unit,
    onDismiss: () -> Unit
) {
    var mode by remember { mutableStateOf(initialMode) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Country & Currency state
    var selectedCountry by remember { mutableStateOf(SUPPORTED_COUNTRIES[0]) }
    var selectedCurrency by remember { mutableStateOf(SUPPORTED_COUNTRIES[0].currencySymbol) }

    // Form fields
    var loginIdentifier by remember { mutableStateOf("player@globalbetting.com") }
    var loginPassword by remember { mutableStateOf("GlobalPass2026") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    // Register fields
    var regUsername by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regReferral by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(true) }

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
                Text(
                    text = if (mode == "LOGIN") "LOGIN TO GLOBAL BETTING" else "REGISTER NEW ACCOUNT",
                    color = TextWhitePrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_auth_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhitePrimary
                    )
                }
            }

            // Mode Toggle Tabs (Login vs Register)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCardSurface)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (mode == "LOGIN") AmberGoldPrimary else Color.Transparent)
                        .clickable { mode = "LOGIN" }
                        .padding(vertical = 8.dp)
                        .testTag("auth_tab_login"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LOG IN",
                        color = if (mode == "LOGIN") NavyDarkBackground else TextMuted,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (mode == "REGISTER") AmberGoldPrimary else Color.Transparent)
                        .clickable { mode = "REGISTER" }
                        .padding(vertical = 8.dp)
                        .testTag("auth_tab_register"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "REGISTER",
                        color = if (mode == "REGISTER") NavyDarkBackground else TextMuted,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (mode == "LOGIN") {
                    // Quick Demo auto-fill hint
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x2200E5FF))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "💡 Quick 1-Tap: Credentials prefilled for instant testing.",
                            color = ElectricCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = loginIdentifier,
                        onValueChange = { loginIdentifier = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_input_identifier"),
                        label = { Text("Mobile Number or Email", color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = AmberGoldPrimary)
                        },
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

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_input_password"),
                        label = { Text("Password", color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = AmberGoldPrimary)
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = TextMuted
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AmberGoldPrimary,
                                    checkmarkColor = NavyDarkBackground
                                )
                            )
                            Text(text = "Remember me", color = TextMuted, fontSize = 12.sp)
                        }

                        Text(
                            text = "Forgot password?",
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onLogin(loginIdentifier, loginPassword) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("auth_submit_login_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                    ) {
                        Text(
                            text = "LOG IN",
                            color = NavyDarkBackground,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else {
                    // REGISTER FORM: SELECT COUNTRY & CURRENCY
                    Text(
                        text = "SELECT COUNTRY & CURRENCY",
                        color = AmberGoldPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(SUPPORTED_COUNTRIES) { country ->
                            val isSelected = country.code == selectedCountry.code
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) NavyCardElevated else NavyDarkBackground)
                                    .border(1.dp, if (isSelected) AmberGoldPrimary else NavyBorder, RoundedCornerShape(6.dp))
                                    .clickable {
                                        selectedCountry = country
                                        selectedCurrency = country.currencySymbol
                                        regPhone = "${country.dialCode} "
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${country.flag} ${country.name} (${country.currencySymbol})",
                                    color = if (isSelected) AmberGoldPrimary else TextWhitePrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = regUsername,
                        onValueChange = { regUsername = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_input_username"),
                        label = { Text("Choose Username", color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = AmberGoldPrimary)
                        },
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
                        value = regPhone,
                        onValueChange = { regPhone = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_input_phone"),
                        label = { Text("Mobile Number (+Country code)", color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = AmberGoldPrimary)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                        value = regEmail,
                        onValueChange = { regEmail = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_input_email"),
                        label = { Text("Email Address", color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                        value = regPassword,
                        onValueChange = { regPassword = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_input_password"),
                        label = { Text("Create Password (min 6 characters)", color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = AmberGoldPrimary)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                        value = regReferral,
                        onValueChange = { regReferral = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Referral / Promo Code (Optional)", color = TextMuted) },
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

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = acceptTerms,
                            onCheckedChange = { acceptTerms = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AmberGoldPrimary,
                                checkmarkColor = NavyDarkBackground
                            )
                        )
                        Text(
                            text = "I am at least 18 years old and agree to the Terms of Service & Privacy Policy.",
                            color = TextMuted,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val uname = if (regUsername.isNotBlank()) regUsername else "Player_" + (1000..9999).random()
                            val phone = if (regPhone.isNotBlank()) regPhone else "${selectedCountry.dialCode} 50 123 4567"
                            val email = if (regEmail.isNotBlank()) regEmail else "$uname@globalbetting.com"
                            onRegister(uname, phone, email, regPassword, selectedCountry.name, selectedCurrency)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("auth_submit_register_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                    ) {
                        Text(
                            text = "REGISTER IN ${selectedCountry.name.uppercase()} (${selectedCurrency}0.00)",
                            color = NavyDarkBackground,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}
