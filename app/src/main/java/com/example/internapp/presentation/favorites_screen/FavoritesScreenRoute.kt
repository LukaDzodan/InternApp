package com.example.internapp.presentation.favorites_screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.internapp.R
import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.presentation.common.toText
import com.example.internapp.presentation.components.ListingScreen

@Composable
fun FavoriteScreenRoute(
    viewModel: FavoriteScreenViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onPropertyClick: (property: PropertyListing) -> Unit,

    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackBarMessage.collect { event ->
            snackBarHostState.showSnackbar(event.toText(context))
        }
    }

    ListingScreen(
        state = state,
        modifier = modifier,
        emptyStateText = stringResource(R.string.empty_favorites),
        navigationIconText = stringResource(R.string.top_bar_navigation_text_back),
        onPropertyClick = { property ->
            onPropertyClick(property)
        },
        onBookmarkClick = { propertyId ->
            viewModel.onBookmarkClick(propertyId)
        },
        onNavigationIconClick = {
            onBack()
        },
        snackbarHostState = snackBarHostState,
    )

}
