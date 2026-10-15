package com.example.internapp.domain.usecase

import com.example.internapp.domain.model.search_result.SearchResultPage
import com.example.internapp.domain.repository.PropertyRepository
import javax.inject.Inject

class GetAllPropertiesUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke() : Result<SearchResultPage> = propertyRepository.getListings()
}
