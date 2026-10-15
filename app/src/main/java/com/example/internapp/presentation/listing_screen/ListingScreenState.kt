package com.example.internapp.presentation.listing_screen

import com.example.internapp.domain.model.property.PropertyListing

data class ListingScreenState(
    val isLoading: Boolean = true,
    val properties : List<PropertyListing> = emptyList(),
    val error : String = "",
)
