package com.example.internapp.presentation.detail_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.internapp.R
import com.example.internapp.domain.model.property.AttachmentType
import com.example.internapp.domain.model.property.OfferType
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailScreenRoute(
    propertyId: String,
    onBack: () -> Unit,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(propertyId) {
        viewModel.load(propertyId)
    }

    DetailScreen(
        state = state,
        onBack = { onBack() },
        onPropertyBookmark = {},
        modifier = modifier,
        snackbarHostState = snackBarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(
    state: DetailScreenState,
    onBack: () -> Unit,
    onPropertyBookmark: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val formatter = remember { NumberFormat.getNumberInstance(Locale.forLanguageTag("de-CH")) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.top_bar_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        when {
            state.error.isNotEmpty() -> {
                Box(Modifier
                    .fillMaxSize()
                    .padding(padding), contentAlignment = Alignment.Center) {
                    Text(state.error)
                }
            }
            state.property == null -> {
                Box(Modifier
                    .fillMaxSize()
                    .padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                val property = state.property
                val imageUrls = property.localization.attachments
                    .filter { it.type == AttachmentType.IMAGE && it.url.isNotBlank() }
                    .map { it.url }

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        if (imageUrls.isEmpty()) {
                            Image(
                                painter = painterResource(R.drawable.no_image_property),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        } else if (imageUrls.size == 1) {
                            AsyncImage(
                                model = imageUrls.first(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            val pagerState = rememberPagerState(pageCount = { imageUrls.size })
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize(),
                            ) { page ->
                                AsyncImage(
                                    model = imageUrls[page],
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            Text(
                                text = "${pagerState.currentPage + 1}/${imageUrls.size}",
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                                    .background(Color.Black.copy(0.5f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }

                        IconButton(
                            onClick = onPropertyBookmark,
                            modifier = Modifier.align(Alignment.TopEnd),
                        ) {
                            Icon(
                                if (state.isBookmarked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null,
                                tint = if (state.isBookmarked) MaterialTheme.colorScheme.error else Color.White,
                            )
                        }
                    }

                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                        Text(
                            text = property.localization.title.ifBlank { "Untitled" },
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )

                        val currency = property.prices.currency.ifBlank { "CHF" }

                        val priceText = when (property.offerType) {
                            OfferType.BUY -> property.prices.buy?.price?.let { "$currency ${formatter.format(it)}" } ?: "—"
                            OfferType.RENT -> "—"
                            OfferType.UNKNOWN -> "—"
                        }
                        Text(priceText, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)

                        val addr = buildString {
                            property.address.street?.takeIf { it.isNotBlank() }?.let { append("$it, ") }
                            if (property.address.postalCode.isNotBlank()) append("${property.address.postalCode} ")
                            append(property.address.locality)
                        }.trim()
                        if (addr.isNotBlank()) {
                            Text(addr, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        property.characteristics.numberOfRooms?.let {
                            Text("Rooms: $it", style = MaterialTheme.typography.bodyMedium)
                        }
                        property.characteristics.livingSpace?.let {
                            Text("Living space: $it m²", style = MaterialTheme.typography.bodyMedium)
                        }
                        property.characteristics.lotSize?.let {
                            Text("Lot: $it m²", style = MaterialTheme.typography.bodyMedium)
                        }

                        if (property.categories.isNotEmpty()) {
                            Text(
                                property.categories.joinToString(" · ") { it.name.replace('_', ' ') },
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }

                        property.lister.phone?.takeIf { it.isNotBlank() }?.let { phone ->
                            Text("Phone: $phone", style = MaterialTheme.typography.bodyMedium)
                        }
                        property.lister.logoUrl?.takeIf { it.isNotBlank() }?.let { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(40.dp)
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InformationSection(

    modifier: Modifier = Modifier
) {

}
