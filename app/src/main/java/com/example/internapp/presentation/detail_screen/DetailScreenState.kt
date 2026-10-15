package com.example.internapp.presentation.detail_screen

import com.example.internapp.domain.model.property.PropertyListing

data class DetailScreenState(
    val property : PropertyListing? = null,
    val error : String = "",
    val isBookmarked : Boolean = false
)
