package com.example.internapp.presentation.detail_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.domain.usecase.GetPropertyListingByIdUseCase
import com.example.internapp.domain.usecase.ObserveFavoritesIdsUseCase
import com.example.internapp.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getPropertyListingByIdUseCase: GetPropertyListingByIdUseCase,
    private val observeFavoritesIdsUseCase: ObserveFavoritesIdsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
) : ViewModel() {

    private var _state = MutableStateFlow(DetailScreenState())
    val state = _state.asStateFlow()

    private var propertyId: String = ""

    init {
        observeFavoritesIds()
        Log.d("abetabe", propertyId)
    }

    fun load(id: String) {
        viewModelScope.launch {
            propertyId = id
            getPropertyListingByIdUseCase(id).fold(
                onSuccess = { property ->
                    _state.update {
                        it.copy(
                            property = property,
                            error = ""
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            property = null,
                            error = throwable.message ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }

    private fun observeFavoritesIds() {
        viewModelScope.launch {
            observeFavoritesIdsUseCase().collect { ids ->
                _state.update {
                    it.copy(isBookmarked = propertyId in ids)
                }
                Log.d("Ids su:", "$ids")
            }
        }
    }

    fun toggleBookmark() {
        if (propertyId.isBlank()) return
        viewModelScope.launch {
            toggleBookmarkUseCase(propertyId)
        }
    }


}
