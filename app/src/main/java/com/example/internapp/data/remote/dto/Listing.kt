package com.example.internapp.data.remote.dto

data class Listing(
    val address: com.example.internapp.data.remote.dto.AddressX,
    val categories: List<String>,
    val characteristics: com.example.internapp.data.remote.dto.Characteristics,
    val id: String,
    val lister: com.example.internapp.data.remote.dto.ListerDto,
    val localization: com.example.internapp.data.remote.dto.Localization,
    val offerType: String,
    val prices: com.example.internapp.data.remote.dto.Prices
)
