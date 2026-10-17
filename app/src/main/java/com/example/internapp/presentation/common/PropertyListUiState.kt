package com.example.internapp.presentation.common

import com.example.internapp.domain.model.property.PropertyListing

data class PropertyListUiState(
    val isLoading: Boolean = true,
    val properties : List<PropertyListing> = emptyList(),
    val favoriteIds: Set<String>? = emptySet(),
    val error : String = "",
)
