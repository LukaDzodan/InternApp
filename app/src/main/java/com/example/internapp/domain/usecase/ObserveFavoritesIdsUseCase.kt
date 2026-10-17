package com.example.internapp.domain.usecase

import com.example.internapp.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoritesIdsUseCase @Inject constructor(
    private val repository: PropertyRepository
) {

    operator fun invoke() : Flow<Set<String>> {
        return repository.observeFavoritesPropertyIds()
    }

}
