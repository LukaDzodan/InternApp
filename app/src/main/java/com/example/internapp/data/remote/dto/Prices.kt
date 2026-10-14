package com.example.internapp.data.remote.dto

data class Prices(
    val buy: com.example.internapp.data.remote.dto.Buy,
    val currency: String,
    val rent: com.example.internapp.data.remote.dto.Rent?
)
