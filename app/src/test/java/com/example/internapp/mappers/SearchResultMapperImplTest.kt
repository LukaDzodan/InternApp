package com.example.internapp.mappers

import com.example.internapp.data.remote.dto.Address
import com.example.internapp.data.remote.dto.AddressX
import com.example.internapp.data.remote.dto.Buy
import com.example.internapp.data.remote.dto.Characteristics
import com.example.internapp.data.remote.dto.De
import com.example.internapp.data.remote.dto.ListerBrandingDto
import com.example.internapp.data.remote.dto.ListerDto
import com.example.internapp.data.remote.dto.Listing
import com.example.internapp.data.remote.dto.ListingType
import com.example.internapp.data.remote.dto.ListingsResponse
import com.example.internapp.data.remote.dto.Localization
import com.example.internapp.data.remote.dto.Prices
import com.example.internapp.data.remote.dto.Result
import com.example.internapp.data.remote.dto.Text
import com.example.internapp.data.remote.mappers.GeoMapperImpl
import com.example.internapp.data.remote.mappers.ListerMapperImpl
import com.example.internapp.data.remote.mappers.PriceMapperImpl
import com.example.internapp.data.remote.mappers.PropertyMapperImpl
import com.example.internapp.data.remote.mappers.SearchResultMapperImpl
import com.example.internapp.domain.model.property.PropertyType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class SearchResultMapperImplTest {

    private val mapper = SearchResultMapperImpl(
        listerMapper = ListerMapperImpl(),
        propertyMapper = PropertyMapperImpl(
            geoMapper = GeoMapperImpl(),
            priceMapper = PriceMapperImpl(),
            listerMapper = ListerMapperImpl(),
        ),
    )

    @Test
    fun `toSearchResult maps id remoteViewing and listingType`() {
        val dto = minimalResult(
            id = "1",
            listingType = "TOP",
            remoteViewing = true,
            listingId = "listing-abc",
        )

        val result = mapper.toSearchResult(dto)

        assertEquals("1", result.id)
        assertEquals(true, result.remoteViewing)
        assertEquals(PropertyType.TOP, result.listingType)
        assertEquals("listing-abc", result.listing.id)
    }

    @Test
    fun `toSearchResult maps STANDARD and BASIC listing types`() {
        val standard = mapper.toSearchResult(
            minimalResult(listingType = "STANDARD"),
        )
        val basic = mapper.toSearchResult(
            minimalResult(listingType = "BASIC"),
        )

        assertEquals(PropertyType.STANDARD, standard.listingType)
        assertEquals(PropertyType.BASIC, basic.listingType)
    }

    @Test
    fun `unknown listingType maps to UNKNOWN`() {
        val dto = minimalResult(listingType = "PREMIUM_PLUS")

        assertEquals(PropertyType.UNKNOWN, mapper.toSearchResult(dto).listingType)
    }

    @Test
    fun `toSearchResult maps null listerBranding`() {
        val dto = minimalResult(listerBranding = null)

        assertNull(mapper.toSearchResult(dto).listerBranding)
    }

    @Test
    fun `toSearchResult maps listerBranding`() {
        val branding = ListerBrandingDto(
            adActive = true,
            address = Address(
                country = "CH",
                locality = "Zug",
                postalCode = "6300",
                region = "ZG",
                street = null,
            ),
            isPremiumBranding = false,
            isQualityPartner = true,
            legalName = "Branding AG",
            logoUrl = "https://example.com/logo.png",
            name = "Branding Name",
            profilePageUrlKeyword = "branding",
        )
        val dto = minimalResult(listerBranding = branding)

        val result = mapper.toSearchResult(dto)

        assertEquals("Branding Name", result.listerBranding?.name)
        assertEquals("Zug", result.listerBranding?.address?.locality)
    }

    @Test
    fun `toSearchResultPage maps pagination and results`() {
        val response = ListingsResponse(
            from = 0,
            maxFrom = 100,
            size = 2,
            total = 50,
            results = listOf(
                minimalResult(id = "r1", listingType = "TOP"),
                minimalResult(id = "r2", listingType = "BASIC"),
            ),
        )

        val page = mapper.toSearchResultPage(response)

        assertEquals(0, page.from)
        assertEquals(100, page.maxFrom)
        assertEquals(2, page.size)
        assertEquals(50, page.total)
        assertEquals(2, page.results.size)
        assertEquals("r1", page.results[0].id)
        assertEquals(PropertyType.TOP, page.results[0].listingType)
        assertEquals(PropertyType.BASIC, page.results[1].listingType)
    }

    @Test
    fun `toSearchResultPage maps empty results`() {
        val response = ListingsResponse(
            from = 0,
            maxFrom = 0,
            size = 0,
            total = 0,
            results = emptyList(),
        )

        val page = mapper.toSearchResultPage(response)

        assertEquals(0, page.results.size)
    }

    private fun minimalResult(
        id: String = "result-id",
        listingType: String = "STANDARD",
        remoteViewing: Boolean = false,
        listingId: String = "listing-id",
        listerBranding: ListerBrandingDto? = null,
    ): Result = Result(
        id = id,
        listerBranding = listerBranding,
        listing = minimalListing(id = listingId),
        listingType = ListingType(type = listingType),
        remoteViewing = remoteViewing,
    )

    private fun minimalListing(
        id: String = "listing-id",
        title: String = "Test title",
    ): Listing = Listing(
        id = id,
        offerType = "BUY",
        categories = listOf("HOUSE"),
        address = AddressX(
            country = "CH",
            geoCoordinates = null,
            locality = "Luzern",
            postalCode = "6000",
            region = "LU",
            street = null,
        ),
        characteristics = Characteristics(
            livingSpace = null,
            lotSize = null,
            numberOfRooms = null,
            totalFloorSpace = null,
        ),
        lister = ListerDto(logoUrl = null, phone = null),
        localization = Localization(
            primary = "de",
            de = De(
                attachments = emptyList(),
                text = Text(title = title),
                urls = emptyList(),
            ),
        ),
        prices = Prices(
            currency = "CHF",
            buy = Buy(price = 100_000L, area = "ALL", interval = "ONETIME"),
            rent = null,
        ),
    )
}
