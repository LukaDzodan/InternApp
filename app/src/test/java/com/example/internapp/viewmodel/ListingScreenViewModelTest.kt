package com.example.internapp.viewmodel

import com.example.internapp.domain.model.lister.Lister
import com.example.internapp.domain.model.property.OfferType
import com.example.internapp.domain.model.property.PropertyAddress
import com.example.internapp.domain.model.property.PropertyCategory
import com.example.internapp.domain.model.property.PropertyCharacteristics
import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.domain.model.property.PropertyLocalization
import com.example.internapp.domain.model.property.PropertyPrices
import com.example.internapp.domain.model.property.PropertyType
import com.example.internapp.domain.model.search_result.SearchResult
import com.example.internapp.domain.model.search_result.SearchResultPage
import com.example.internapp.domain.usecase.GetAllPropertiesUseCase
import com.example.internapp.domain.usecase.ObserveFavoritesIdsUseCase
import com.example.internapp.domain.usecase.ToggleBookmarkUseCase
import com.example.internapp.presentation.common.BookmarkSnackBar
import com.example.internapp.presentation.listing_screen.ListingScreenViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListingScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAllPropertiesUseCase: GetAllPropertiesUseCase = mockk()
    private val observeFavoritesIds: ObserveFavoritesIdsUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()

    @Test
    fun `loadProperties success updates state`() = runTest {
        val listing = minimalListing("listing-1")
        val page = SearchResultPage(
            from = 0,
            maxFrom = 10,
            size = 1,
            total = 1,
            results = listOf(
                SearchResult(
                    id = "r1",
                    listerBranding = null,
                    listing = listing,
                    listingType = PropertyType.BASIC,
                    remoteViewing = false,
                ),
            ),
        )
        every { observeFavoritesIds() } returns flowOf(emptySet())
        coEvery { getAllPropertiesUseCase() } returns Result.success(page)

        val viewModel = createViewModel()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals("", viewModel.state.value.error)
        assertEquals(1, viewModel.state.value.properties.size)
        assertEquals("listing-1", viewModel.state.value.properties.first().id)
    }

    @Test
    fun `loadProperties failure sets error`() = runTest {
        every { observeFavoritesIds() } returns flowOf(emptySet())
        coEvery { getAllPropertiesUseCase() } returns Result.failure(Exception("boom"))

        val viewModel = createViewModel()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals("boom", viewModel.state.value.error)
        assertTrue(viewModel.state.value.properties.isEmpty())
    }

    @Test
    fun `observeFavorites updates favoriteIds`() = runTest {
        val favorites = MutableStateFlow(setOf("fav-1"))
        every { observeFavoritesIds() } returns favorites
        coEvery { getAllPropertiesUseCase() } returns Result.success(emptyPage())

        val viewModel = createViewModel()
        assertEquals(setOf("fav-1"), viewModel.state.value.favoriteIds)

        favorites.value = setOf("fav-1", "fav-2")
        assertEquals(setOf("fav-1", "fav-2"), viewModel.state.value.favoriteIds)
    }

    @Test
    fun `onBookmarkClick success saved sends Added`() = runTest {
        stubIdleViewModelDefaults()
        coEvery { toggleBookmarkUseCase("p1") } returns Result.success(true)

        val viewModel = createViewModel()

        val snackbar = async { viewModel.snackBarMessage.first() }
        viewModel.onBookmarkClick("p1")

        assertEquals(BookmarkSnackBar.Added, snackbar.await())
    }

    @Test
    fun `onBookmarkClick success removed sends Removed`() = runTest {
        stubIdleViewModelDefaults()
        coEvery { toggleBookmarkUseCase("p1") } returns Result.success(false)

        val viewModel = createViewModel()

        val snackbar = async { viewModel.snackBarMessage.first() }
        viewModel.onBookmarkClick("p1")

        assertEquals(BookmarkSnackBar.Removed, snackbar.await())
    }

    @Test
    fun `onBookmarkClick failure sends Error`() = runTest {
        stubIdleViewModelDefaults()
        coEvery { toggleBookmarkUseCase("p1") } returns Result.failure(Exception("db"))

        val viewModel = createViewModel()

        val snackbar = async { viewModel.snackBarMessage.first() }
        viewModel.onBookmarkClick("p1")

        assertEquals(BookmarkSnackBar.Error, snackbar.await())
    }

    private fun stubIdleViewModelDefaults() {
        every { observeFavoritesIds() } returns flowOf(emptySet())
        coEvery { getAllPropertiesUseCase() } returns Result.success(emptyPage())
    }

    private fun createViewModel(): ListingScreenViewModel =
        ListingScreenViewModel(
            getAllPropertiesUseCase = getAllPropertiesUseCase,
            observeFavoritesIds = observeFavoritesIds,
            toggleBookmarkUseCase = toggleBookmarkUseCase,
        )

    private fun emptyPage(): SearchResultPage = SearchResultPage(
        from = 0,
        maxFrom = 0,
        size = 0,
        total = 0,
        results = emptyList(),
    )

    private fun minimalListing(id: String): PropertyListing = PropertyListing(
        id = id,
        offerType = OfferType.BUY,
        categories = listOf(PropertyCategory.HOUSE),
        address = PropertyAddress(
            country = "CH",
            geoCoordinates = null,
            locality = "City",
            postalCode = "6000",
            region = "LU",
            street = null,
        ),
        characteristics = PropertyCharacteristics(
            livingSpace = null,
            lotSize = null,
            numberOfRooms = null,
            totalFloorSpace = null,
        ),
        lister = Lister(logoUrl = null, phone = null),
        localization = PropertyLocalization(
            attachments = emptyList(),
            title = "Title",
            urls = emptyList(),
            primary = "de",
        ),
        prices = PropertyPrices(buy = null, currency = "CHF", rent = null),
    )
}
