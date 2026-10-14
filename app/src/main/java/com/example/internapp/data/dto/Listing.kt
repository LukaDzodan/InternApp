package com.example.internapp.data.dto

data class Listing(
    val address: AddressX,
    val categories: List<String>,
    val characteristics: Characteristics,
    val id: String,
    val lister: ListerDto,
    val localization: Localization,
    val offerType: String,
    val prices: Prices
)
