package com.example.internapp.domain.repository

import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.domain.model.search_result.SearchResultPage
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {

    suspend fun getListings(): Result<SearchResultPage>

    suspend fun getPropertyListingById(id: String): Result<PropertyListing>

    suspend fun toggleBookmark(propertyId: String): Result<Boolean>

    fun observeFavoritesPropertyIds(): Flow<Set<String>>

}
