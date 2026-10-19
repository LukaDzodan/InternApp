package com.example.internapp.viewmodel

import com.example.internapp.domain.model.lister.Lister
import com.example.internapp.domain.model.property.OfferType
import com.example.internapp.domain.model.property.PropertyAddress
import com.example.internapp.domain.model.property.PropertyCategory
import com.example.internapp.domain.model.property.PropertyCharacteristics
import com.example.internapp.domain.model.property.PropertyListing
import com.example.internapp.domain.model.property.PropertyLocalization
import com.example.internapp.domain.model.property.PropertyPrices
import com.example.internapp.domain.usecase.GetPropertyListingByIdUseCase
import com.example.internapp.domain.usecase.ObserveFavoritesIdsUseCase
import com.example.internapp.domain.usecase.ToggleBookmarkUseCase
import com.example.internapp.presentation.detail_screen.DetailScreenViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPropertyListingByIdUseCase: GetPropertyListingByIdUseCase = mockk()
    private val observeFavoritesIdsUseCase: ObserveFavoritesIdsUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()

    @Test
    fun `load success sets property and clears error`() = runTest {
        val listing = minimalListing("detail-1")
        every { observeFavoritesIdsUseCase() } returns flowOf(emptySet())
        coEvery { getPropertyListingByIdUseCase("detail-1") } returns Result.success(listing)

        val viewModel = createViewModel()
        viewModel.load("detail-1")
        advanceUntilIdle()

        assertEquals("detail-1", viewModel.state.value.property?.id)
        assertEquals("", viewModel.state.value.error)
    }

    @Test
    fun `load failure sets error and clears property`() = runTest {
        every { observeFavoritesIdsUseCase() } returns flowOf(emptySet())
        coEvery { getPropertyListingByIdUseCase("x") } returns Result.failure(Exception("not found"))

        val viewModel = createViewModel()
        viewModel.load("x")

        assertNull(viewModel.state.value.property)
        assertEquals("not found", viewModel.state.value.error)
    }

    @Test
    fun `isBookmarked true when propertyId in favorites after favorites emit`() = runTest {
        val favorites = MutableStateFlow<Set<String>>(emptySet())
        every { observeFavoritesIdsUseCase() } returns favorites
        coEvery { getPropertyListingByIdUseCase("p1") } returns Result.success(minimalListing("p1"))

        val viewModel = createViewModel()
        viewModel.load("p1")
        assertFalse(viewModel.state.value.isBookmarked)

        favorites.value = setOf("p1")
        assertTrue(viewModel.state.value.isBookmarked)
    }

    @Test
    fun `isBookmarked false when propertyId not in favorites`() = runTest {
        val favorites = MutableStateFlow(setOf("other-id"))
        every { observeFavoritesIdsUseCase() } returns favorites
        coEvery { getPropertyListingByIdUseCase("p1") } returns Result.success(minimalListing("p1"))

        val viewModel = createViewModel()
        viewModel.load("p1")

        assertFalse(viewModel.state.value.isBookmarked)
    }

    @Test
    fun `toggleBookmark calls use case when property loaded`() = runTest {
        every { observeFavoritesIdsUseCase() } returns flowOf(emptySet())
        coEvery { getPropertyListingByIdUseCase("p1") } returns Result.success(minimalListing("p1"))
        coEvery { toggleBookmarkUseCase("p1") } returns Result.success(true)

        val viewModel = createViewModel()
        viewModel.load("p1")
        viewModel.toggleBookmark()

        coVerify(exactly = 1) { toggleBookmarkUseCase("p1") }
    }

    @Test
    fun `toggleBookmark does nothing before load`() = runTest {
        every { observeFavoritesIdsUseCase() } returns flowOf(emptySet())

        val viewModel = createViewModel()
        viewModel.toggleBookmark()

        coVerify(exactly = 0) { toggleBookmarkUseCase(any()) }
    }

    private fun createViewModel(): DetailScreenViewModel =
        DetailScreenViewModel(
            getPropertyListingByIdUseCase = getPropertyListingByIdUseCase,
            observeFavoritesIdsUseCase = observeFavoritesIdsUseCase,
            toggleBookmarkUseCase = toggleBookmarkUseCase,
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
