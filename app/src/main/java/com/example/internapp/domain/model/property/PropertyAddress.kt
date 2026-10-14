package com.example.internapp.domain.model.property

import com.example.internapp.domain.model.GeoLocation


data class PropertyAddress(
    val country: String,
    val geoCoordinates: GeoLocation?,
    val locality: String,
    val postalCode: String,
    val region: String,
    val street: String?
)
