package com.example.internapp.domain.model.lister


data class ListerBranding(
    val adActive: Boolean,
    val address: ListerAddress?,
    val isPremiumBranding: Boolean,
    val isQualityPartner: Boolean,
    val legalName: String,
    val logoUrl: String,
    val name: String?,
    val profilePageUrlKeyword: String
)
