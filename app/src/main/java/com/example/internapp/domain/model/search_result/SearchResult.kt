package com.example.internapp.domain.model.search_result

import com.example.internapp.domain.model.lister.ListerBranding
import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.domain.model.property.PropertyType


data class SearchResult(
    val id: String,
    val listerBranding: ListerBranding?,
    val listing: PropertyListing,
    val listingType: PropertyType,
    val remoteViewing: Boolean
)
