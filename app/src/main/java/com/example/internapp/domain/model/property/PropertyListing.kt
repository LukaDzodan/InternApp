package com.example.internapp.domain.model.property

import com.example.internapp.domain.model.lister.Lister


data class PropertyListing(
    val address: PropertyAddress,
    val categories: List<PropertyCategory>,
    val characteristics: PropertyCharacteristics,
    val id: String,
    val lister: Lister,
    val localization: PropertyLocalization,
    val offerType: OfferType,
    val prices: PropertyPrices
)
