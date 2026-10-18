package com.example.internapp.presentation.listing_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.domain.usecase.GetAllPropertiesUseCase
import com.example.internapp.domain.usecase.ObserveFavoritesIdsUseCase
import com.example.internapp.domain.usecase.ToggleBookmarkUseCase
import com.example.internapp.presentation.common.BookmarkSnackBar
import com.example.internapp.presentation.common.PropertyListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingScreenViewModel @Inject constructor(
    private val getAllPropertiesUseCase: GetAllPropertiesUseCase,
    private val observeFavoritesIds: ObserveFavoritesIdsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
) : ViewModel() {


    private var _state =  MutableStateFlow(PropertyListUiState())
    val state = _state.asStateFlow()

    private val _snackBarMessage = Channel<BookmarkSnackBar>(Channel.CONFLATED)
    val snackBarMessage = _snackBarMessage.receiveAsFlow()

    init {
        loadProperties()
        observeFavorites()
    }

    private fun loadProperties() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAllPropertiesUseCase()
                .onSuccess { searchResultPage ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "",
                            properties = searchResultPage.results.map { it.listing })
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown message"
                        )
                    }
                }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            observeFavoritesIds().collect { ids ->
                _state.update { it.copy(favoriteIds = ids) }
            }
        }
    }
    fun onBookmarkClick(propertyId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(propertyId).fold(
                onSuccess = { isSaved ->
                    _snackBarMessage.send(if(isSaved) BookmarkSnackBar.Added else BookmarkSnackBar.Removed)
                },
                onFailure = {
                    _snackBarMessage.send(BookmarkSnackBar.Error)
                }
            )
        }
    }

}
