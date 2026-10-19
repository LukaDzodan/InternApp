package com.example.internapp.data.repository

import com.example.internapp.data.local.Dao
import com.example.internapp.data.local.FavoriteEntity
import com.example.internapp.data.remote.Api
import com.example.internapp.data.remote.mappers.SearchResultMapper
import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.domain.model.search_result.SearchResultPage
import com.example.internapp.domain.repository.PropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val api: Api,
    private val searchResultMapper: SearchResultMapper,
    private val dao: Dao,
) : PropertyRepository {

    private var cashedPropertyListings: List<PropertyListing> = emptyList()

    override suspend fun getListings(): Result<SearchResultPage> = withContext(Dispatchers.IO) {
        try {
            val response = api.getListings()
            val searchResultPage = searchResultMapper.toSearchResultPage(response)
            cashedPropertyListings = searchResultPage.results.map { it.listing }

            Result.success(searchResultPage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPropertyListingById(id: String): Result<PropertyListing> {
        val property = cashedPropertyListings.find { it.id == id }

        return if (property != null) {
            Result.success(property)
        } else {
            Result.failure(Exception("Property does not exist"))
        }
    }

    override suspend fun toggleBookmark(propertyId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            runCatching {
                if (dao.isFavorite(propertyId)) {
                    dao.deleteFavorite(propertyId)
                    false
                } else {
                    dao.insertFavorite(
                        FavoriteEntity(
                            propertyId = propertyId,
                            savedAt = System.currentTimeMillis()
                        ),
                    )
                    dao.isFavorite(propertyId)
                }
            }
        }

    override fun observeFavoritesPropertyIds(): Flow<Set<String>> {
        return dao.observeFavoriteIds().map { it.toSet() }
    }


}
