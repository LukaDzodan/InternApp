package com.example.internapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.internapp.R
import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.presentation.common.PropertyListUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingScreen(
    modifier: Modifier = Modifier,
    state: PropertyListUiState,
    navigationIconText : String,
    onPropertyClick: (property: PropertyListing) -> Unit,
    onBookmarkClick: (propertyId: String) -> Unit,
    onNavigationIconClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.top_bar_title)) },
                navigationIcon = {
                    Text(
                        text = navigationIconText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable(onClick = onNavigationIconClick)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        when {
            state.error.isNotEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error
                    )
                }
            }

            else -> {
                val lazyColumnState = rememberLazyListState()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        else -> {
                            LazyColumn(
                                state = lazyColumnState,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(12.dp, 12.dp, 4.dp, 12.dp)
                            ) {
                                items(
                                    items = state.properties,
                                    key = { it.id },
                                ) { propertyListing ->
                                    PropertyListingItem(
                                        propertyListing = propertyListing,
                                        isBookmarked = state.favoriteIds?.contains(propertyListing.id)
                                            ?: false,
                                        onClick = { onPropertyClick(propertyListing) },
                                        onBookmarkClick = { onBookmarkClick(propertyListing.id) },
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
