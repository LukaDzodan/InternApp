package com.example.internapp.presentation.listing_screen

import com.example.internapp.domain.model.search_result.SearchResult

data class ListingScreenState(
    val isLoading: Boolean = true,
    val properties : List<SearchResult> = emptyList(),
    val error : String = "",
)
