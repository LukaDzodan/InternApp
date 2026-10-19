package com.example.internapp.mappers

import com.example.internapp.data.remote.dto.Address
import com.example.internapp.data.remote.dto.ListerBrandingDto
import com.example.internapp.data.remote.dto.ListerDto
import com.example.internapp.data.remote.mappers.ListerMapperImpl
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ListerMapperImplTest {

    private val mapper = ListerMapperImpl()

    @Test
    fun `toLister maps logoUrl and phone`() {
        val dto = ListerDto(
            logoUrl = "https://example.com/logo.png",
            phone = "+41 44 123 45 67",
        )
        val result = mapper.toLister(dto)
        assertEquals("https://example.com/logo.png", result.logoUrl)
        assertEquals("+41 44 123 45 67", result.phone)
    }
    @Test
    fun `toLister allows null fields`() {
        val dto = ListerDto(logoUrl = null, phone = null)
        val result = mapper.toLister(dto)
        assertNull(result.logoUrl)
        assertNull(result.phone)
    }
    @Test
    fun `toListerAddress maps all fields`() {
        val dto = Address(
            country = "CH",
            locality = "Zürich",
            postalCode = "8001",
            region = "ZH",
            street = "Bahnhofstrasse 1",
        )
        val result = mapper.toListerAddress(dto)
        assertEquals("CH", result.country)
        assertEquals("Zürich", result.locality)
        assertEquals("8001", result.postalCode)
        assertEquals("ZH", result.region)
        assertEquals("Bahnhofstrasse 1", result.street)
    }
    @Test
    fun `toListerBranding maps fields and nested address`() {
        val addressDto = Address(
            country = "CH",
            locality = "Bern",
            postalCode = "3000",
            region = "BE",
            street = null,
        )
        val dto = ListerBrandingDto(
            adActive = true,
            address = addressDto,
            isPremiumBranding = false,
            isQualityPartner = true,
            legalName = "Example AG",
            logoUrl = "https://example.com/brand.png",
            name = "Example Realty",
            profilePageUrlKeyword = "example-realty",
        )
        val result = mapper.toListerBranding(dto)
        assertEquals(true, result.adActive)
        assertEquals(false, result.isPremiumBranding)
        assertEquals(true, result.isQualityPartner)
        assertEquals("Example AG", result.legalName)
        assertEquals("https://example.com/brand.png", result.logoUrl)
        assertEquals("Example Realty", result.name)
        assertEquals("example-realty", result.profilePageUrlKeyword)
        assertEquals("Bern", result.address?.locality)
        assertEquals("3000", result.address?.postalCode)
        assertNull(result.address?.street)
    }
    @Test
    fun `toListerBranding maps null address to null`() {
        val dto = ListerBrandingDto(
            adActive = false,
            address = null,
            isPremiumBranding = true,
            isQualityPartner = false,
            legalName = "No Address Ltd",
            logoUrl = "https://example.com/x.png",
            name = null,
            profilePageUrlKeyword = "no-address",
        )
        val result = mapper.toListerBranding(dto)
        assertNull(result.address)
        assertNull(result.name)
    }

}
