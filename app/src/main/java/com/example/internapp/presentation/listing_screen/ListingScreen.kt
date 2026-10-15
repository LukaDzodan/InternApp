package com.example.internapp.presentation.listing_screen

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.internapp.R
import com.example.internapp.presentation.components.PropertyListingItem

@Composable
fun ListingScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: ListingScreenViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }




    ListingScreen(
        state = state,
        snackbarHostState = snackBarHostState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingScreen(
    modifier: Modifier = Modifier,
    state : ListingScreenState,
    snackbarHostState: SnackbarHostState,
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text(text = stringResource(R.string.top_bar_title))}
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
                ){
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
                                contentPadding = PaddingValues(12.dp,12.dp,4.dp,12.dp)
                            ) {
                                items(
                                    items = state.properties,
                                    key = { it.id},
                                ){ propertyListing ->
                                    PropertyListingItem(
                                        propertyListing = propertyListing,
                                        isBookmarked = false,
                                        onClick = {},
                                        onBookmarkClick = {},
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
