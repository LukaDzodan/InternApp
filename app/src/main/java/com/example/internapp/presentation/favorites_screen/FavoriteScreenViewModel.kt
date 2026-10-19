package com.example.internapp.presentation.favorites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.domain.usecase.GetAllPropertiesUseCase
import com.example.internapp.domain.usecase.ObserveFavoritesIdsUseCase
import com.example.internapp.domain.usecase.ToggleBookmarkUseCase
import com.example.internapp.presentation.common.BookmarkSnackBar
import com.example.internapp.presentation.common.PropertyListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteScreenViewModel @Inject constructor(
    private val observeFavoritesIdsUseCase: ObserveFavoritesIdsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val getAllPropertiesUseCase: GetAllPropertiesUseCase,
) : ViewModel() {

    private var _state = MutableStateFlow(PropertyListUiState())
    val state = _state.asStateFlow()

    private val allListings = MutableStateFlow<List<PropertyListing>>(emptyList())

    private val _snackBarMessage = Channel<BookmarkSnackBar>(Channel.CONFLATED)
    val snackBarMessage = _snackBarMessage.receiveAsFlow()

    init {
        getAllPropertiesOnce()
        observeFavoriteList()
    }

    private fun getAllPropertiesOnce() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAllPropertiesUseCase().fold(
                onSuccess = { searchResultPage ->
                    allListings.value = searchResultPage.results.map { it.listing }
                    _state.update { it.copy(isLoading = false) }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }

    private fun observeFavoriteList() {
        viewModelScope.launch {
            combine(
                observeFavoritesIdsUseCase(),
                allListings,
            ) { favoriteIds, properties ->
                properties.filter { it.id in favoriteIds }
            }.collect { favoriteProperties ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        properties = favoriteProperties,
                        favoriteIds = favoriteProperties.map { it.id }.toSet(),
                        error = ""
                    )
                }
            }
        }
    }

    fun onBookmarkClick(propertyId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(propertyId).fold(
                onSuccess = { isSaved ->
                    _snackBarMessage.send(if (isSaved) BookmarkSnackBar.Added else BookmarkSnackBar.Removed)
                },
                onFailure = {
                    _snackBarMessage.send(BookmarkSnackBar.Error)
                }
            )
        }
    }
}
