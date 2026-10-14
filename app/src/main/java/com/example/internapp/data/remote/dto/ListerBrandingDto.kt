package com.example.internapp.data.remote.dto

data class ListerBrandingDto(
    val adActive: Boolean,
    val address: com.example.internapp.data.remote.dto.Address?,
    val isPremiumBranding: Boolean,
    val isQualityPartner: Boolean,
    val legalName: String,
    val logoUrl: String,
    val name: String?,
    val profilePageUrlKeyword: String
)
