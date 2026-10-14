package com.example.internapp.data.remote.mappers

import com.example.internapp.domain.model.lister.ListerAddress
import com.example.internapp.domain.model.lister.Lister
import com.example.internapp.domain.model.lister.ListerBranding


interface ListerMapper {
    fun toListerAddress(dto: com.example.internapp.data.remote.dto.Address): ListerAddress
    fun toLister(dto: com.example.internapp.data.remote.dto.ListerDto): Lister

    fun toListerBranding(dto : com.example.internapp.data.remote.dto.ListerBrandingDto) : ListerBranding?
}

class ListerMapperImpl : com.example.internapp.data.remote.mappers.ListerMapper {

    override fun toListerAddress(dto: com.example.internapp.data.remote.dto.Address): ListerAddress = ListerAddress(
            country = dto.country,
            locality = dto.locality,
            postalCode = dto.postalCode,
            region = dto.region,
            street = dto.street
        )

    override fun toLister(dto: com.example.internapp.data.remote.dto.ListerDto): Lister = Lister(
        logoUrl = dto.logoUrl,
        phone = dto.phone
    )

    override fun toListerBranding(dto: com.example.internapp.data.remote.dto.ListerBrandingDto): ListerBranding = ListerBranding(
        adActive = dto.adActive,
        address = dto.address?.let { toListerAddress(dto.address)},
        isPremiumBranding = dto.isPremiumBranding,
        isQualityPartner = dto.isQualityPartner,
        legalName = dto.legalName,
        logoUrl = dto.logoUrl,
        name = dto.name,
        profilePageUrlKeyword = dto.profilePageUrlKeyword
    )

}
