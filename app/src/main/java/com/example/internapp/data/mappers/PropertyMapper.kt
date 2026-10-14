package com.example.internapp.data.mappers

import com.example.internapp.data.dto.AddressX
import com.example.internapp.data.dto.Attachment
import com.example.internapp.data.dto.Characteristics
import com.example.internapp.data.dto.Listing
import com.example.internapp.data.dto.Localization
import com.example.internapp.data.dto.Prices
import com.example.internapp.data.dto.Url
import com.example.internapp.domain.model.property.AttachmentType
import com.example.internapp.domain.model.property.OfferType
import com.example.internapp.domain.model.property.PropertyAddress
import com.example.internapp.domain.model.property.PropertyAttachment
import com.example.internapp.domain.model.property.PropertyCategory
import com.example.internapp.domain.model.property.PropertyCharacteristics
import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.domain.model.property.PropertyLocalization
import com.example.internapp.domain.model.property.PropertyPrices
import com.example.internapp.domain.model.property.PropertyUrlType

interface PropertyMapper {
    fun toPropertyAddress(address: AddressX): PropertyAddress
    fun toPropertyAttachment(attachment: List<Attachment>): List<PropertyAttachment>
    fun toPropertyCharacteristic(characteristics: Characteristics): PropertyCharacteristics
    fun toPropertyLocalization(localization: Localization): PropertyLocalization
    fun toPropertyPrices(prices: Prices): PropertyPrices
    fun toPropertyListing(listing : Listing) : PropertyListing
}


class PropertyMapperImpl(
    val geoMapper: GeoMapper,
    val priceMapper: PriceMapper,
    val listerMapper: ListerMapper,
) : PropertyMapper {
    override fun toPropertyAddress(address: AddressX): PropertyAddress {
        return PropertyAddress(
            country = address.country,
            geoCoordinates = address.geoCoordinates?.let { geoMapper.toGeoLocation(address.geoCoordinates)},
            locality = address.locality,
            postalCode = address.postalCode,
            region = address.region,
            street = address.street
        )
    }

    override fun toPropertyAttachment(attachment: List<Attachment>): List<PropertyAttachment> {
        return attachment.map { att ->
            PropertyAttachment(
                file = att.file,
                url = att.url,
                type = toAttachmentType(att.type)
            )
        }
    }

    override fun toPropertyCharacteristic(characteristics: Characteristics): PropertyCharacteristics {
        return PropertyCharacteristics(
            livingSpace = characteristics.livingSpace,
            lotSize = characteristics.lotSize,
            numberOfRooms = characteristics.numberOfRooms,
            totalFloorSpace = characteristics.totalFloorSpace
        )
    }

    override fun toPropertyLocalization(localization: Localization): PropertyLocalization {
        return PropertyLocalization(
            attachments = toPropertyAttachment(localization.de.attachments),
            title = localization.de.text.title,
            urls = toPropertyUrlTypes(localization.de.urls),
            primary = localization.primary
        )
    }

    override fun toPropertyPrices(prices: Prices): PropertyPrices {
        return PropertyPrices(
            currency = prices.currency,
            buy = priceMapper.toBuyPrice(prices.buy),
            rent = priceMapper.toRentPrice(prices.rent)
        )
    }

    override fun toPropertyListing(listing: Listing): PropertyListing {
        return PropertyListing(
            address = toPropertyAddress(listing.address),
            categories = toPropertyCategories(listing.categories),
            characteristics = toPropertyCharacteristic(listing.characteristics),
            id = listing.id,
            lister = listerMapper.toLister(listing.lister),
            localization = toPropertyLocalization(listing.localization),
            offerType = toOfferType(listing.offerType),
            prices = toPropertyPrices(listing.prices),
        )
    }

    private fun toAttachmentType(type: String): AttachmentType {
        return when (type) {
            "IMAGE" -> AttachmentType.IMAGE
            "DOCUMENT" -> AttachmentType.DOCUMENT
            else -> AttachmentType.UNKNOWN
        }
    }

    private fun toPropertyUrlTypes(urls: List<Url>?): List<PropertyUrlType> {
        return urls?.map { toPropertyUrlType(it) } ?: emptyList()
    }

    private fun toPropertyUrlType(url: Url): PropertyUrlType {
        return when (url.type) {
            "VIRTUAL_TOUR" -> PropertyUrlType.VIRTUAL_TOUR
            "YOUTUBE" -> PropertyUrlType.YOUTUBE
            else -> PropertyUrlType.UNKNOWN
        }
    }

    private fun toPropertyCategories(categories: List<String>): List<PropertyCategory> {
        return categories.map { toPropertyCategory(it) }
    }

    private fun toPropertyCategory(category: String): PropertyCategory {
        return when (category) {
            "HOUSE" -> PropertyCategory.HOUSE
            "SINGLE_HOUSE" -> PropertyCategory.SINGLE_HOUSE
            "MULTIPLE_DWELLING" -> PropertyCategory.MULTIPLE_DWELLING
            "CASTLE" -> PropertyCategory.CASTLE
            "CHALET" -> PropertyCategory.CHALET
            else -> PropertyCategory.UNKNOWN
        }
    }

    private fun toOfferType(offerType: String): OfferType {
        return when (offerType) {
            "RENT" -> OfferType.RENT
            "BUY" -> OfferType.BUY
            else -> OfferType.UNKNOWN
        }
    }

}
