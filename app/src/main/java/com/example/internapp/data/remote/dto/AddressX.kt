package com.example.internapp.data.remote.dto

data class AddressX(
    val country: String,
    val geoCoordinates: com.example.internapp.data.remote.dto.GeoCoordinates?,
    val locality: String,
    val postalCode: String,
    val region: String,
    val street: String?
)
