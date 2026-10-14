package com.example.internapp.data.dto

data class ListerBrandingDto(
    val adActive: Boolean,
    val address: Address?,
    val isPremiumBranding: Boolean,
    val isQualityPartner: Boolean,
    val legalName: String,
    val logoUrl: String,
    val name: String?,
    val profilePageUrlKeyword: String
)
