package com.example.data.model

data class CountryOption(
    val name: String,
    val code: String,
    val flag: String,
    val dialCode: String,
    val currencyCode: String,
    val currencySymbol: String
)

val SUPPORTED_COUNTRIES = listOf(
    CountryOption(
        name = "Ghana",
        code = "GH",
        flag = "🇬🇭",
        dialCode = "+233",
        currencyCode = "GHS",
        currencySymbol = "GH₵"
    ),
    CountryOption(
        name = "Nigeria",
        code = "NG",
        flag = "🇳🇬",
        dialCode = "+234",
        currencyCode = "NGN",
        currencySymbol = "₦"
    ),
    CountryOption(
        name = "Kenya",
        code = "KE",
        flag = "🇰🇪",
        dialCode = "+254",
        currencyCode = "KES",
        currencySymbol = "KSh"
    ),
    CountryOption(
        name = "South Africa",
        code = "ZA",
        flag = "🇿🇦",
        dialCode = "+27",
        currencyCode = "ZAR",
        currencySymbol = "R"
    ),
    CountryOption(
        name = "United Kingdom",
        code = "GB",
        flag = "🇬🇧",
        dialCode = "+44",
        currencyCode = "GBP",
        currencySymbol = "£"
    ),
    CountryOption(
        name = "United States",
        code = "US",
        flag = "🇺🇸",
        dialCode = "+1",
        currencyCode = "USD",
        currencySymbol = "$"
    ),
    CountryOption(
        name = "Côte d'Ivoire",
        code = "CI",
        flag = "🇨🇮",
        dialCode = "+225",
        currencyCode = "XOF",
        currencySymbol = "CFA"
    ),
    CountryOption(
        name = "Cameroon",
        code = "CM",
        flag = "🇨🇲",
        dialCode = "+237",
        currencyCode = "XAF",
        currencySymbol = "CFA"
    ),
    CountryOption(
        name = "Tanzania",
        code = "TZ",
        flag = "🇹🇿",
        dialCode = "+255",
        currencyCode = "TZS",
        currencySymbol = "TSh"
    ),
    CountryOption(
        name = "Uganda",
        code = "UG",
        flag = "🇺🇬",
        dialCode = "+256",
        currencyCode = "UGX",
        currencySymbol = "USh"
    )
)
