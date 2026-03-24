package io.chthonic.weather.presentation.screens.locationlist

import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.presentation.models.ListUiState
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

internal class LocationListUiStateResolverTest {

    private lateinit var resolver: LocationListUiStateResolver

    @Before
    fun setup() {
        resolver = LocationListUiStateResolver()
    }

    // -------------------------------------------------------------------------
    // resolveOnSearchAction
    // -------------------------------------------------------------------------

    @Test
    fun `resolveOnSearchAction - empty string returns Idle`() {
        val result = resolver.resolveOnSearchAction("")
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchAction - single space returns Idle`() {
        val result = resolver.resolveOnSearchAction(" ")
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchAction - multiple spaces returns Idle`() {
        val result = resolver.resolveOnSearchAction("   ")
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchAction - tab character returns Idle`() {
        val result = resolver.resolveOnSearchAction("\t")
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchAction - newline character returns Idle`() {
        val result = resolver.resolveOnSearchAction("\n")
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchAction - mixed whitespace returns Idle`() {
        val result = resolver.resolveOnSearchAction(" \t\n ")
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchAction - single character returns Loading`() {
        val result = resolver.resolveOnSearchAction("a")
        assertEquals(ListUiState.Loading, result)
    }

    @Test
    fun `resolveOnSearchAction - regular search query returns Loading`() {
        val result = resolver.resolveOnSearchAction("London")
        assertEquals(ListUiState.Loading, result)
    }

    @Test
    fun `resolveOnSearchAction - query with leading space returns Loading`() {
        val result = resolver.resolveOnSearchAction(" London")
        assertEquals(ListUiState.Loading, result)
    }

    @Test
    fun `resolveOnSearchAction - query with trailing space returns Loading`() {
        val result = resolver.resolveOnSearchAction("London ")
        assertEquals(ListUiState.Loading, result)
    }

    @Test
    fun `resolveOnSearchAction - query with surrounding spaces returns Loading`() {
        val result = resolver.resolveOnSearchAction("  London  ")
        assertEquals(ListUiState.Loading, result)
    }

    @Test
    fun `resolveOnSearchAction - numeric query returns Loading`() {
        val result = resolver.resolveOnSearchAction("12345")
        assertEquals(ListUiState.Loading, result)
    }

    @Test
    fun `resolveOnSearchAction - special characters query returns Loading`() {
        val result = resolver.resolveOnSearchAction("!@#")
        assertEquals(ListUiState.Loading, result)
    }

    // -------------------------------------------------------------------------
    // resolveOnSearchResult — Error outcome (highest priority)
    // -------------------------------------------------------------------------

    @Test
    fun `resolveOnSearchResult - Error outcome with empty locations and blank search returns Error`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Error<List<Location>>("network error"),
            locations = emptyList(),
            searchText = "",
        )
        assertEquals(ListUiState.Error, result)
    }

    @Test
    fun `resolveOnSearchResult - Error outcome with empty locations and non-blank search returns Error`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Error<Location>("timeout"),
            locations = emptyList(),
            searchText = "Paris",
        )
        assertEquals(ListUiState.Error, result)
    }

    @Test
    fun `resolveOnSearchResult - Error outcome with non-empty locations and blank search returns Error`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Error<List<Location>>("server error"),
            locations = listOf(aLocationCurrentWeather()),
            searchText = "",
        )
        assertEquals(ListUiState.Error, result)
    }

    @Test
    fun `resolveOnSearchResult - Error outcome with non-empty locations and non-blank search returns Error`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Error<List<Location>>("unknown error"),
            locations = listOf(aLocationCurrentWeather()),
            searchText = "Berlin",
        )
        assertEquals(ListUiState.Error, result)
    }

    @Test
    fun `resolveOnSearchResult - Error outcome takes priority over non-empty locations`() {
        val locations = listOf(aLocationCurrentWeather(), aLocationCurrentWeather("Tokyo"))
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Error<List<Location>>("error"),
            locations = locations,
            searchText = "Tokyo",
        )
        assertEquals(ListUiState.Error, result)
    }

    // -------------------------------------------------------------------------
    // resolveOnSearchResult — Success outcome, non-empty locations (second priority)
    // -------------------------------------------------------------------------

    @Test
    fun `resolveOnSearchResult - Success outcome with non-empty locations and non-blank search returns Content`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = listOf(aLocationCurrentWeather()),
            searchText = "London",
        )
        assertEquals(ListUiState.Content, result)
    }

    @Test
    fun `resolveOnSearchResult - Success outcome with non-empty locations and blank search returns Content`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = listOf(aLocationCurrentWeather()),
            searchText = "",
        )
        assertEquals(ListUiState.Content, result)
    }

    @Test
    fun `resolveOnSearchResult - Success outcome with non-empty locations and whitespace search returns Content`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = listOf(aLocationCurrentWeather()),
            searchText = "   ",
        )
        assertEquals(ListUiState.Content, result)
    }

    @Test
    fun `resolveOnSearchResult - Success outcome with multiple locations returns Content`() {
        val locations = listOf(
            aLocationCurrentWeather("London"),
            aLocationCurrentWeather("Paris"),
            aLocationCurrentWeather("Berlin"),
        )
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = locations,
            searchText = "eu",
        )
        assertEquals(ListUiState.Content, result)
    }

    @Test
    fun `resolveOnSearchResult - non-empty locations take priority over blank search text`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = listOf(aLocationCurrentWeather()),
            searchText = "",
        )
        // Content wins over Idle because locations are non-empty
        assertEquals(ListUiState.Content, result)
    }

    // -------------------------------------------------------------------------
    // resolveOnSearchResult — Success outcome, empty locations, blank search → Idle
    // -------------------------------------------------------------------------

    @Test
    fun `resolveOnSearchResult - Success outcome with empty locations and empty search returns Idle`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = emptyList(),
            searchText = "",
        )
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchResult - Success outcome with empty locations and single space returns Idle`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = emptyList(),
            searchText = " ",
        )
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchResult - Success outcome with empty locations and whitespace-only search returns Idle`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = emptyList(),
            searchText = "\t\n  ",
        )
        assertEquals(ListUiState.Idle, result)
    }

    // -------------------------------------------------------------------------
    // resolveOnSearchResult — Success outcome, empty locations, non-blank search → Empty
    // -------------------------------------------------------------------------

    @Test
    fun `resolveOnSearchResult - Success outcome with empty locations and non-blank search returns Empty`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = emptyList(),
            searchText = "XYZ",
        )
        assertEquals(ListUiState.Empty, result)
    }

    @Test
    fun `resolveOnSearchResult - Success outcome with empty locations and single char search returns Empty`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = emptyList(),
            searchText = "Z",
        )
        assertEquals(ListUiState.Empty, result)
    }

    @Test
    fun `resolveOnSearchResult - Success outcome with empty locations and search with spaces returns Empty`() {
        // Leading/trailing spaces but non-blank overall
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = emptyList(),
            searchText = " NonExistentCity ",
        )
        assertEquals(ListUiState.Empty, result)
    }

    // -------------------------------------------------------------------------
    // resolveOnSearchResult — Outcome type variance (Success with typed data)
    // -------------------------------------------------------------------------

    @Test
    fun `resolveOnSearchResult - Success outcome with typed list data and locations returns Content`() {
        val typedOutcome: Outcome<List<String>> = Outcome.Success(listOf("result1"))
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = typedOutcome,
            locations = listOf(aLocationCurrentWeather()),
            searchText = "query",
        )
        assertEquals(ListUiState.Content, result)
    }

    @Test
    fun `resolveOnSearchResult - Success outcome with typed list data and empty locations returns Empty`() {
        val typedOutcome: Outcome<List<String>> = Outcome.Success(listOf("result1"))
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = typedOutcome,
            locations = emptyList(),
            searchText = "query",
        )
        assertEquals(ListUiState.Empty, result)
    }

    @Test
    fun `resolveOnSearchResult - Error outcome with typed data returns Error`() {
        val typedOutcome: Outcome<List<Location>> = Outcome.Error<List<Location>>("typed error")
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = typedOutcome,
            locations = emptyList(),
            searchText = "query",
        )
        assertEquals(ListUiState.Error, result)
    }

    // -------------------------------------------------------------------------
    // Priority ordering verification
    // -------------------------------------------------------------------------

    @Test
    fun `resolveOnSearchResult - priority 1 Error beats non-empty locations`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Error<List<Location>>("err"),
            locations = listOf(aLocationCurrentWeather()),
            searchText = "city",
        )
        assertEquals(ListUiState.Error, result)
    }

    @Test
    fun `resolveOnSearchResult - priority 2 Content beats blank searchText`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = listOf(aLocationCurrentWeather()),
            searchText = "",
        )
        assertEquals(ListUiState.Content, result)
    }

    @Test
    fun `resolveOnSearchResult - priority 3 Idle for Success with empty locations and blank search`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = emptyList(),
            searchText = "",
        )
        assertEquals(ListUiState.Idle, result)
    }

    @Test
    fun `resolveOnSearchResult - priority 4 Empty for Success with empty locations and non-blank search`() {
        val result = resolver.resolveOnSearchResult(
            geocodingOutcome = Outcome.Success(Unit),
            locations = emptyList(),
            searchText = "city",
        )
        assertEquals(ListUiState.Empty, result)
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private fun aLocationCurrentWeather(displayName: String = "London") =
        LocationCurrentWeather(
            location = Location(lat = 51.5074, lon = -0.1278),
            displayName = displayName,
        )
}
