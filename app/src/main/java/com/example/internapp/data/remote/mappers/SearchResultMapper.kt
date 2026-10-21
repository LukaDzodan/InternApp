package com.example.internapp.data.remote.mappers

import com.example.internapp.data.remote.dto.ListingType
import com.example.internapp.data.remote.dto.ListingsResponse
import com.example.internapp.domain.model.property.PropertyType
import com.example.internapp.domain.model.search_result.SearchResult
import com.example.internapp.domain.model.search_result.SearchResultPage

interface SearchResultMapper {
    fun toSearchResult(searchResult: com.example.internapp.data.remote.dto.Result): SearchResult
    fun toSearchResultPage(listingsResponse: ListingsResponse) : SearchResultPage
}

class SearchResultMapperImpl(
    val listerMapper: ListerMapper,
    val propertyMapper: PropertyMapper
) : SearchResultMapper {
    override fun toSearchResult(searchResult: com.example.internapp.data.remote.dto.Result): SearchResult {
        return SearchResult(
            id = searchResult.id,
            listerBranding = searchResult.listerBranding?.let { listerMapper.toListerBranding(it) },
            listing = propertyMapper.toPropertyListing(searchResult.listing),
            listingType = toPropertyType(searchResult.listingType),
            remoteViewing = searchResult.remoteViewing,
        )
    }

    override fun toSearchResultPage(listingsResponse: ListingsResponse): SearchResultPage {
        return SearchResultPage(
            from = listingsResponse.from,
            maxFrom = listingsResponse.maxFrom,
            results = listingsResponse.results.map { toSearchResult(it) },
            size = listingsResponse.size,
            total = listingsResponse.total
        )
    }

    private fun toPropertyType(listingType: ListingType): PropertyType{
        return when (listingType.type) {
            "TOP" -> PropertyType.TOP
            "BASIC" -> PropertyType.BASIC
            "STANDARD" -> PropertyType.STANDARD
            else -> PropertyType.UNKNOWN
        }
    }

}
