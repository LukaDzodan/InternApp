package com.example.internapp.domain.repository

import com.example.internapp.domain.model.search_result.SearchResultPage

interface PropertyRepository {

    suspend fun getListings() : Result<SearchResultPage>

}
