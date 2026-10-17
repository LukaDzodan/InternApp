package com.example.internapp.domain.usecase

import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.domain.repository.PropertyRepository
import jakarta.inject.Inject

class GetPropertyListingByIdUseCase @Inject constructor(
    private val repository: PropertyRepository
) {
    suspend operator fun invoke(id : String) : Result<PropertyListing> {
        return repository.getPropertyListingById(id)
    }
}
