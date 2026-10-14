package com.example.internapp.data.mappers

import com.example.internapp.data.dto.Address
import com.example.internapp.data.dto.ListerBrandingDto
import com.example.internapp.data.dto.ListerDto
import com.example.internapp.domain.model.lister.ListerAddress
import com.example.internapp.domain.model.lister.Lister
import com.example.internapp.domain.model.lister.ListerBranding


interface ListerMapper {
    fun toListerAddress(dto: Address): ListerAddress
    fun toLister(dto: ListerDto): Lister

    fun toListerBranding(dto : ListerBrandingDto) : ListerBranding?
}

class ListerMapperImpl : ListerMapper {

    override fun toListerAddress(dto: Address): ListerAddress = ListerAddress(
            country = dto.country,
            locality = dto.locality,
            postalCode = dto.postalCode,
            region = dto.region,
            street = dto.street
        )

    override fun toLister(dto: ListerDto): Lister = Lister(
        logoUrl = dto.logoUrl,
        phone = dto.phone
    )

    override fun toListerBranding(dto: ListerBrandingDto): ListerBranding = ListerBranding(
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
