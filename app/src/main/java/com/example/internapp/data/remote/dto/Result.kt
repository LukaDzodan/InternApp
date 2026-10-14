package com.example.internapp.data.remote.dto

data class Result(
    val id: String,
    val listerBranding: ListerBrandingDto?,
    val listing: Listing,
    val listingType: ListingType,
    val remoteViewing: Boolean
)
