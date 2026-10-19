package com.example.internapp.repo

import com.example.internapp.data.local.Dao
import com.example.internapp.data.local.FavoriteEntity
import com.example.internapp.data.remote.Api
import com.example.internapp.data.remote.dto.ListingsResponse
import com.example.internapp.data.remote.mappers.SearchResultMapper
import com.example.internapp.data.repository.PropertyRepositoryImpl
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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PropertyRepositoryImplTest {

    private val api: Api = mockk()
    private val searchResultMapper: SearchResultMapper = mockk()
    private val dao: Dao = mockk(relaxed = true)

    private lateinit var repository: PropertyRepositoryImpl

    @Before
    fun setUp() {
        repository = PropertyRepositoryImpl(
            api = api,
            searchResultMapper = searchResultMapper,
            dao = dao,
        )
    }

    @Test
    fun `getListings returns success and caches listings`() = runTest {
        val apiResponse = ListingsResponse(
            from = 0,
            maxFrom = 10,
            size = 1,
            total = 1,
            results = emptyList(),
        )
        val page = domainPageWithListingId("listing-1")
        coEvery { api.getListings() } returns apiResponse
        every { searchResultMapper.toSearchResultPage(apiResponse) } returns page
        val result = repository.getListings()
        assertTrue(result.isSuccess)
        assertEquals(page, result.getOrNull())
        val byId = repository.getPropertyListingById("listing-1")
        assertTrue(byId.isSuccess)
        assertEquals("listing-1", byId.getOrNull()?.id)
    }

    @Test
    fun `getListings returns failure when api throws`() = runTest {
        coEvery { api.getListings() } throws RuntimeException("network error")
        val result = repository.getListings()
        assertTrue(result.isFailure)
    }

    @Test
    fun `getPropertyListingById fails when not in cache`() = runTest {
        val result = repository.getPropertyListingById("missing-id")
        assertTrue(result.isFailure)
        assertEquals("Property does not exist", result.exceptionOrNull()?.message)
    }

    @Test
    fun `toggleBookmark removes favorite when already saved`() = runTest {
        coEvery { dao.isFavorite("p1") } returns true
        coEvery { dao.deleteFavorite("p1") } returns Unit
        val result = repository.toggleBookmark("p1")
        assertTrue(result.isSuccess)
        assertFalse(result.getOrNull()!!)
        coVerify(exactly = 1) { dao.deleteFavorite("p1") }
        coVerify(exactly = 0) { dao.insertFavorite(any()) }
    }

    @Test
    fun `toggleBookmark adds favorite when not saved`() = runTest {
        coEvery { dao.isFavorite("p1") } returnsMany listOf(false, true)
        coEvery { dao.insertFavorite(any()) } returns Unit
        val result = repository.toggleBookmark("p1")
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!)
        coVerify(exactly = 1) {
            dao.insertFavorite(
                any()
            )
        }
    }

    @Test
    fun `observeFavoritesPropertyIds maps list to set`() = runTest {
        every { dao.observeFavoriteIds() } returns flowOf(listOf("a", "b", "a"))
        val ids = repository.observeFavoritesPropertyIds().first()
        assertEquals(setOf("a", "b"), ids)
    }

    private fun domainPageWithListingId(id: String): SearchResultPage {
        val listing = minimalPropertyListing(id)
        return SearchResultPage(
            from = 0,
            maxFrom = 10,
            size = 1,
            total = 1,
            results = listOf(
                SearchResult(
                    id = "result-$id",
                    listerBranding = null,
                    listing = listing,
                    listingType = PropertyType.BASIC,
                    remoteViewing = false,
                ),
            ),
        )
    }

    private fun minimalPropertyListing(id: String): PropertyListing = PropertyListing(
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
