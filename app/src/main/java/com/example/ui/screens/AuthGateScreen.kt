package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CountryOption
import com.example.data.model.SUPPORTED_COUNTRIES
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyCardSurface
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavyDarkSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhitePrimary

@Composable
fun AuthGateScreen(
    onLogin: (phoneOrEmail: String, pass: String) -> Unit,
    onRegister: (username: String, phone: String, email: String, pass: String, country: String, currencySymbol: String) -> Unit
) {
    var mode by remember { mutableStateOf("LOGIN") } // LOGIN or REGISTER

    // Country and Currency state
    var selectedCountry by remember { mutableStateOf(SUPPORTED_COUNTRIES[0]) } // Ghana default
    var selectedCurrency by remember { mutableStateOf(SUPPORTED_COUNTRIES[0].currencySymbol) }

    // Login state
    var loginIdentifier by remember { mutableStateOf("player@globalbetting.com") }
    var loginPassword by remember { mutableStateOf("GlobalPass2026") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Register state
    var regUsername by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("${selectedCountry.dialCode} ") }
    var regEmail by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .statusBarsPadding()
            .testTag("auth_gate_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Brand Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(ElectricCyan, AmberGoldPrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Global Betting",
                        tint = NavyDarkBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row {
                        Text(
                            text = "GLOBAL",
                            color = TextWhitePrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "BETTING",
                            color = AmberGoldPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "SIGN IN TO ENTER SPORTSBOOK",
                        color = ElectricCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Mini Graphic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.global_sports_hero_1790291770162),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xEE070D1E), Color(0x77070D1E))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "JOIN GLOBAL SPORTSBOOK",
                        color = AmberGoldPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Live odds on Premier League, Champions League, NBA & more.",
                        color = TextWhitePrimary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab bar: Login vs Register
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavyCardSurface)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (mode == "LOGIN") AmberGoldPrimary else Color.Transparent)
                        .clickable { mode = "LOGIN" }
                        .padding(vertical = 10.dp)
                        .testTag("gate_tab_login"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LOG IN",
                        color = if (mode == "LOGIN") NavyDarkBackground else TextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (mode == "REGISTER") AmberGoldPrimary else Color.Transparent)
                        .clickable { mode = "REGISTER" }
                        .padding(vertical = 10.dp)
                        .testTag("gate_tab_register"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "REGISTER",
                        color = if (mode == "REGISTER") NavyDarkBackground else TextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Form Content
            if (mode == "LOGIN") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x2200E5FF))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "💡 Quick 1-Tap: Demo credentials loaded below. Tap 'LOG IN' to enter directly!",
                        color = ElectricCyan,
                        fontSize = 11.5.sp,
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = loginIdentifier,
                    onValueChange = { loginIdentifier = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gate_input_identifier"),
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

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = loginPassword,
                    onValueChange = { loginPassword = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gate_input_password"),
                    label = { Text("Password", color = TextMuted) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = AmberGoldPrimary)
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onLogin(loginIdentifier, loginPassword) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("gate_submit_login_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                ) {
                    Text(
                        text = "LOG IN TO ENTER",
                        color = NavyDarkBackground,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            } else {
                // REGISTER: STEP 1 - CHOOSE COUNTRY & MONEY (CURRENCY)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavyCardSurface)
                        .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = null,
                                tint = AmberGoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "1. CHOOSE YOUR COUNTRY & MONEY (CURRENCY)",
                                color = TextWhitePrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Scrollable list of countries
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(SUPPORTED_COUNTRIES) { country ->
                                val isSelected = country.code == selectedCountry.code
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) NavyCardElevated else NavyDarkBackground)
                                        .border(
                                            1.5.dp,
                                            if (isSelected) AmberGoldPrimary else NavyBorder,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            selectedCountry = country
                                            selectedCurrency = country.currencySymbol
                                            regPhone = "${country.dialCode} "
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = country.flag, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(
                                                text = country.name,
                                                color = if (isSelected) AmberGoldPrimary else TextWhitePrimary,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${country.currencyCode} (${country.currencySymbol})",
                                                color = if (isSelected) MintEmerald else TextMuted,
                                                fontSize = 9.5.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Selected Summary & Currency Badge
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(NavyDarkBackground)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = selectedCountry.flag, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${selectedCountry.name} (${selectedCountry.dialCode})",
                                    color = TextWhitePrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Currency: ", color = TextMuted, fontSize = 11.sp)
                                Text(
                                    text = "${selectedCountry.currencyCode} (${selectedCurrency})",
                                    color = AmberGoldPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "2. ACCOUNT CREDENTIALS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = regUsername,
                    onValueChange = { regUsername = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gate_reg_username"),
                    label = { Text("Username", color = TextMuted) },
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

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = regPhone,
                    onValueChange = { regPhone = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gate_reg_phone"),
                    label = { Text("Mobile Number", color = TextMuted) },
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

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = regEmail,
                    onValueChange = { regEmail = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gate_reg_email"),
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

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = regPassword,
                    onValueChange = { regPassword = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gate_reg_password"),
                    label = { Text("Password (min 6 chars)", color = TextMuted) },
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

                Spacer(modifier = Modifier.height(10.dp))

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
                        text = "I am 18+ and agree to the Terms & Conditions.",
                        color = TextMuted,
                        fontSize = 11.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val u = if (regUsername.isNotBlank()) regUsername else "Player_" + (1000..9999).random()
                        val p = if (regPhone.isNotBlank()) regPhone else "${selectedCountry.dialCode} 50 123 4567"
                        val e = if (regEmail.isNotBlank()) regEmail else "$u@globalbetting.com"
                        onRegister(u, p, e, regPassword, selectedCountry.name, selectedCurrency)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("gate_submit_register_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                ) {
                    Text(
                        text = "REGISTER IN ${selectedCountry.name.uppercase()} (${selectedCurrency}0.00)",
                        color = NavyDarkBackground,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
