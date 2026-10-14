package com.example.internapp.data.repository

import com.example.internapp.data.remote.Api
import com.example.internapp.data.remote.mappers.SearchResultMapper
import com.example.internapp.domain.model.search_result.SearchResultPage
import com.example.internapp.domain.repository.PropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val api: Api,
    private val searchResultMapper: SearchResultMapper
) : PropertyRepository {

    override suspend fun getListings(): Result<SearchResultPage> = withContext(Dispatchers.IO) {
        try {
            val response = api.getListings()
            Result.success(searchResultMapper.toSearchResultPage(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
