package com.example.internapp.presentation.listing_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.domain.usecase.GetAllPropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingScreenViewModel @Inject constructor(
    private val getAllPropertiesUseCase: GetAllPropertiesUseCase
) : ViewModel() {


    private var _state =  MutableStateFlow(ListingScreenState())
    val state = _state.asStateFlow()

    init {
        loadProperties()
    }

    private fun loadProperties() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAllPropertiesUseCase()
                .onSuccess { searchResultPage ->
                    _state.update {
                        it.copy(isLoading = false,properties = searchResultPage.results)
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
}
