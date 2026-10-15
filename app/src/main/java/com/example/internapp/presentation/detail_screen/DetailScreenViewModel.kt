package com.example.internapp.presentation.detail_screen

import android.R.id.message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internapp.domain.usecase.GetPropertyListingByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getPropertyListingByIdUseCase: GetPropertyListingByIdUseCase

) : ViewModel() {

    private var _state = MutableStateFlow(DetailScreenState())
    val state = _state.asStateFlow()


    fun load(id: String) {
        viewModelScope.launch {
            getPropertyListingByIdUseCase(id).fold(
                onSuccess = { property -> _state.update { it.copy(
                    property = property,
                    error = ""
                ) }},
                onFailure = { throwable -> _state.update { it.copy(
                    property = null,
                    error = throwable.message ?: "Unknown error"
                ) }}
            )
        }
    }

}
