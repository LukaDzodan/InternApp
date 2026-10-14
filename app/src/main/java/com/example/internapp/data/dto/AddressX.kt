package com.example.internapp.data.dto

data class AddressX(
    val country: String,
    val geoCoordinates: GeoCoordinates?,
    val locality: String,
    val postalCode: String,
    val region: String,
    val street: String?
)
