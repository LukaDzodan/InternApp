package com.example.internapp.mappers

import com.example.internapp.data.remote.dto.AddressX
import com.example.internapp.data.remote.dto.Attachment
import com.example.internapp.data.remote.dto.Buy
import com.example.internapp.data.remote.dto.Characteristics
import com.example.internapp.data.remote.dto.De
import com.example.internapp.data.remote.dto.GeoCoordinates
import com.example.internapp.data.remote.dto.ListerDto
import com.example.internapp.data.remote.dto.Listing
import com.example.internapp.data.remote.dto.Localization
import com.example.internapp.data.remote.dto.Prices
import com.example.internapp.data.remote.dto.Text
import com.example.internapp.data.remote.dto.Url
import com.example.internapp.data.remote.mappers.GeoMapperImpl
import com.example.internapp.data.remote.mappers.ListerMapperImpl
import com.example.internapp.data.remote.mappers.PriceMapperImpl
import com.example.internapp.data.remote.mappers.PropertyMapperImpl
import com.example.internapp.domain.model.price.PriceInterval
import com.example.internapp.domain.model.property.AttachmentType
import com.example.internapp.domain.model.property.OfferType
import com.example.internapp.domain.model.property.PropertyCategory
import com.example.internapp.domain.model.property.PropertyUrlType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class PropertyMapperImplTest {

    private val mapper = PropertyMapperImpl(
        geoMapper = GeoMapperImpl(),
        priceMapper = PriceMapperImpl(),
        listerMapper = ListerMapperImpl()
    )

    @Test
    fun `toPropertyListing maps core fields`() {
        val listing = minimalListing(
            id = "prop-123",
            offerType = "BUY",
            categories = listOf("HOUSE"),
            title = "Nice apartment",
        )
        val result = mapper.toPropertyListing(listing)
        assertEquals("prop-123", result.id)
        assertEquals(OfferType.BUY, result.offerType)
        assertEquals(PropertyCategory.HOUSE, result.categories.single())
        assertEquals("Nice apartment", result.localization.title)
        assertEquals("de", result.localization.primary)
        assertEquals("+41 00 000", result.lister.phone)
        assertEquals("CHF", result.prices.currency)
        assertEquals(1_200_000L, result.prices.buy?.price)
        assertEquals(PriceInterval.ONETIME, result.prices.buy?.interval)
    }
    @Test
    fun `toPropertyAddress maps geo coordinates`() {
        val address = AddressX(
            country = "CH",
            geoCoordinates = GeoCoordinates(latitude = 47.37, longitude = 8.54),
            locality = "Zürich",
            postalCode = "8001",
            region = "ZH",
            street = "Teststrasse 1",
        )
        val result = mapper.toPropertyAddress(address)
        assertEquals(47.37, result.geoCoordinates?.latitude)
        assertEquals(8.54, result.geoCoordinates?.longitude)
        assertEquals("Zürich", result.locality)
    }
    @Test
    fun `toPropertyAddress maps null geoCoordinates`() {
        val address = AddressX(
            country = "CH",
            geoCoordinates = null,
            locality = "Bern",
            postalCode = "3000",
            region = "BE",
            street = null,
        )
        val result = mapper.toPropertyAddress(address)
        assertNull(result.geoCoordinates)
        assertNull(result.street)
    }
    @Test
    fun `toPropertyCharacteristic maps numbers`() {
        val dto = Characteristics(
            livingSpace = 120,
            lotSize = 400,
            numberOfRooms = 4.5,
            totalFloorSpace = 130,
        )
        val result = mapper.toPropertyCharacteristic(dto)
        assertEquals(120, result.livingSpace)
        assertEquals(400, result.lotSize)
        assertEquals(4.5, result.numberOfRooms)
        assertEquals(130, result.totalFloorSpace)
    }
    @Test
    fun `toPropertyLocalization maps attachment and url types`() {
        val localization = Localization(
            primary = "de",
            de = De(
                attachments = listOf(
                    Attachment(file = "img.jpg", type = "IMAGE", url = "https://cdn/img.jpg"),
                    Attachment(file = "x.pdf", type = "OTHER", url = "https://cdn/x.pdf"),
                ),
                text = Text(title = "Title"),
                urls = listOf(
                    Url(type = "VIRTUAL_TOUR"),
                    Url(type = "UNKNOWN_API_VALUE"),
                ),
            ),
        )
        val result = mapper.toPropertyLocalization(localization)
        assertEquals(AttachmentType.IMAGE, result.attachments[0].type)
        assertEquals(AttachmentType.UNKNOWN, result.attachments[1].type)
        assertEquals(PropertyUrlType.VIRTUAL_TOUR, result.urls!![0])
        assertEquals(PropertyUrlType.UNKNOWN, result.urls[1])
    }
    @Test
    fun `toPropertyLocalization maps null urls to empty list`() {
        val localization = Localization(
            primary = "de",
            de = De(
                attachments = emptyList(),
                text = Text(title = "T"),
                urls = null,
            ),
        )
        val result = mapper.toPropertyLocalization(localization)
        assertEquals(0, result.urls?.size)
    }
    @Test
    fun `unknown offerType maps to UNKNOWN`() {
        val listing = minimalListing(offerType = "LEASE")
        assertEquals(OfferType.UNKNOWN, mapper.toPropertyListing(listing).offerType)
    }
    @Test
    fun `unknown category maps to UNKNOWN`() {
        val listing = minimalListing(categories = listOf("PENTHOUSE"))
        assertEquals(PropertyCategory.UNKNOWN, mapper.toPropertyListing(listing).categories.single())
    }
    private fun minimalListing(
        id: String = "id-1",
        offerType: String = "BUY",
        categories: List<String> = listOf("SINGLE_HOUSE"),
        title: String = "Default title",
    ): Listing = Listing(
        id = id,
        offerType = offerType,
        categories = categories,
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
        lister = ListerDto(logoUrl = null, phone = "+41 00 000"),
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
            buy = Buy(price = 1_200_000L, area = "ALL", interval = "ONETIME"),
            rent = null,
        ),
    )


}
