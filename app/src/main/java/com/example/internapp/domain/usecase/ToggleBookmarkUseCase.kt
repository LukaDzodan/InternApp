package com.example.internapp.domain.usecase

import com.example.internapp.domain.repository.PropertyRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val repository: PropertyRepository
){
    suspend operator fun invoke(propertyId : String) : Result<Boolean> {
        return  repository.toggleBookmark(propertyId)
    }
}
