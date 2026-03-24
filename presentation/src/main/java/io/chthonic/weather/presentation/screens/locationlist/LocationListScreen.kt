package io.chthonic.weather.presentation.screens.locationlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Location.Companion.UNKNOWN_COORD
import io.chthonic.weather.presentation.AppBarStyle
import io.chthonic.weather.presentation.AppContainerState
import io.chthonic.weather.presentation.LaunchedEffectOnNavLifecycleState
import io.chthonic.weather.presentation.R
import io.chthonic.weather.presentation.models.ListUiState
import io.chthonic.weather.presentation.models.TemperatureUnits
import io.chthonic.weather.presentation.models.WeatherCondition
import io.chthonic.weather.presentation.nav.Destination
import io.chthonic.weather.presentation.theme.AppColors
import io.chthonic.weather.presentation.theme.LocalSpacing
import io.chthonic.weather.presentation.theme.Spacing
import io.chthonic.weather.presentation.widgets.EMPTY_CONTENT_KEY
import io.chthonic.weather.presentation.widgets.ERROR_CONTENT_KEY
import io.chthonic.weather.presentation.widgets.EmptyContent
import io.chthonic.weather.presentation.widgets.ErrorContent
import io.chthonic.weather.presentation.widgets.HandleLocationPermissionState
import io.chthonic.weather.presentation.widgets.IDLE_CONTENT_KEY
import io.chthonic.weather.presentation.widgets.LOADING_CONTENT_KEY
import io.chthonic.weather.presentation.widgets.LoadingContent
import io.chthonic.weather.presentation.widgets.PreviewSharedAnimation
import io.chthonic.weather.presentation.widgets.TemperatureText
import io.chthonic.weather.presentation.widgets.TemperatureUnitsButton
import io.chthonic.weather.presentation.widgets.checkLocationPermission
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
fun LocationListScreen(
    viewModel: LocationListViewModel = hiltViewModel(),
    appContainerState: AppContainerState,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffectOnNavLifecycleState(Lifecycle.State.STARTED) {
        // only update (as soon as possible) on nav change
        appContainerState.updateAppBar(
            title = context.resources.getString(R.string.app_name),
            showNavigationIcon = false,
            style = AppBarStyle.Pinned,
            actions = {
                TemperatureUnitsButton(
                    state.value.temperatureUnits,
                    viewModel::onToggleTemperatureUnits,
                )
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.ToLocationDetail -> {
                    appContainerState.navController.navigate(
                        Destination.LocationDetail.buildUniqueRoute(
                            name = event.name,
                            lat = event.lat,
                            lon = event.lon,
                        )
                    )
                }
            }
        }
    }

    // recheck permission when returning from settings
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                context.checkLocationPermission(
                    onGranted = viewModel::onLocationPermissionGranted,
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    HandleLocationPermissionState(
        state.value.locationPermissionState,
        onGranted = viewModel::onLocationPermissionGranted,
        onDenied = viewModel::onLocationPermissionDenied,
        onDismissed = viewModel::onLocationPermissionDismissed,
        context = context,
    )

    LocationListContent(
        myLocation = state.value.myLocation,
        locations = state.value.searchLocations,
        searchText = state.value.searchText,
        listUiState = state.value.listUiState,
        units = state.value.temperatureUnits,
        onQueryChange = viewModel::onQueryChange,
        onClick = viewModel::onLocationClick,
    )
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun LocationListContent(
    myLocation: LocationCurrentWeather?,
    locations: ImmutableList<LocationCurrentWeather>,
    searchText: String,
    listUiState: ListUiState,
    units: TemperatureUnits,
    onQueryChange: (String) -> Unit,
    onClick: (LocationCurrentWeather) -> Unit
) {
    val spacing = LocalSpacing.current
    val isDark = isSystemInDarkTheme()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {

        stickyHeader {
            LocationSearchBar(
                query = searchText,
                onQueryChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.m)
                    .padding(vertical = spacing.m),
            )
        }

        myLocation?.let {
            item("myLocation") {
                WeatherLocationItem(
                    it,
                    units = units,
                    spacing = spacing,
                    isDark = isDark,
                    isMyLocation = true,
                ) {
                    onClick(it)
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(horizontal = spacing.m)
                        .padding(top = spacing.s, bottom = spacing.xs),
                )
            }
        }

        when (listUiState) {
            ListUiState.Loading -> item(LOADING_CONTENT_KEY) {
                LoadingContent(
                    spacing = spacing,
                    Modifier.fillParentMaxHeight(0.3f),
                )
            }

            ListUiState.Empty -> item(EMPTY_CONTENT_KEY) {
                EmptyContent(
                    spacing = spacing,
                    modifier = Modifier.fillParentMaxHeight(0.5f),
                )
            }

            ListUiState.Idle -> item(IDLE_CONTENT_KEY) {
                EmptyContent(
                    spacing = spacing,
                    modifier = Modifier.fillParentMaxHeight(0.5f),
                    icon = Icons.Outlined.Search,
                    text = "Search for a city"
                )
            }

            ListUiState.Error -> item(ERROR_CONTENT_KEY) {
                ErrorContent(
                    spacing = spacing,
                    modifier = Modifier.fillParentMaxHeight(0.5f),
                )
            }

            ListUiState.Content -> {
                items(
                    locations.size,
                    key = { locations[it].key },
                ) { idx ->
                    locations.getOrNull(idx)?.let { locationState ->
                        WeatherLocationItem(
                            locationState,
                            units = units,
                            spacing = spacing,
                            isDark = isDark,
                        ) {
                            onClick(
                                locationState
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun WeatherLocationItem(
    state: LocationCurrentWeather,
    units: TemperatureUnits,
    spacing: Spacing,
    isDark: Boolean,
    modifier: Modifier = Modifier,
    isMyLocation: Boolean = false,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.m, vertical = spacing.xs),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.temperatureCardColor(state.temp, isDark),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        ListItem(
            modifier = Modifier,// .clickable(onClick = onClick),
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent, // let the Card background show
            ),
            leadingContent = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = AppColors.temperatureIconBoxColor(state.temp, isDark),
                            shape = MaterialTheme.shapes.medium,
                        ),
                ) {
                    Icon(
                        state.displayIcon,
                        contentDescription = state.weatherCondition?.description,
                        modifier = Modifier.size(28.dp),
                        tint = AppColors.temperatureIconTint(state.temp, isDark),
                    )

                    if (isMyLocation) {
                        Icon(
                            imageVector = Icons.Filled.MyLocation,
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.TopStart)
                                .offset(x = (-3).dp, y = (-3).dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape,
                                )
                                .padding(1.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            },
            headlineContent = {
                Column {
                    Text(
                        text = state.displayName,
                        maxLines = 2,
                        minLines = 2,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier,
                    )

                    Text(
                        text = state.displayCoords,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = spacing.xs),
                    )
                }
            },
            trailingContent = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier) {
                    TemperatureText(
                        temperature = state.temp,
                        units = units,
                        color = AppColors.temperatureTextColor(state.temp, isDark),
                        valueTextStyle = MaterialTheme.typography.displaySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontFeatureSettings = "tnum", // tabular numbers
                        ),
                        unitsTextStyle = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                        modifier = Modifier.alpha(if (state.isLoading) 0f else 1f),
                    )

                    this@Card.AnimatedVisibility(visible = state.isLoading) {
                        CircularProgressIndicator()
                    }
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    DockedSearchBar(
        modifier = modifier,
        inputField = @Composable {
            InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text("Search City") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = @Composable {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { onQueryChange("") }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
            )
        },
        expanded = false,
        onExpandedChange = { expanded: Boolean -> },
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = 4.dp,
        content = { },
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@PreviewLightDark
@Composable
private fun PreviewListContentState() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        LocationListContent(
            myLocation = LocationCurrentWeather(
                location = Location(12.0, 13.0),
                displayName = "MY LOCATION",
                weatherCondition = WeatherCondition.CLEAR_SKY,
                temp = 23.4,
            ),
            locations = WeatherLocationPreviewProvider().values.toImmutableList(),
            searchText = "mew",
            listUiState = ListUiState.Content,
            units = TemperatureUnits.CELSIUS,
            onQueryChange = {},
            onClick = {},
        )
    }
}


@PreviewLightDark
@Composable
private fun PreviewListIdleState() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        LocationListContent(
            myLocation = LocationCurrentWeather(
                location = Location(12.0, 13.0),
                displayName = "MY LOCATION",
                weatherCondition = WeatherCondition.CLEAR_SKY,
                temp = 23.4,
            ),
            locations = persistentListOf(),
            searchText = "",
            listUiState = ListUiState.Idle,
            units = TemperatureUnits.CELSIUS,
            onQueryChange = {},
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewListEmptyState() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        LocationListContent(
            myLocation = LocationCurrentWeather(
                location = Location(12.0, 13.0),
                displayName = "MY LOCATION",
                weatherCondition = WeatherCondition.CLEAR_SKY,
                temp = 23.4,
            ),
            locations = persistentListOf(),
            searchText = "mew",
            listUiState = ListUiState.Empty,
            units = TemperatureUnits.CELSIUS,
            onQueryChange = {},
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewListErrorState() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        LocationListContent(
            myLocation = LocationCurrentWeather(
                location = Location(12.0, 13.0),
                displayName = "MY LOCATION",
                weatherCondition = WeatherCondition.CLEAR_SKY,
                temp = 23.4,
            ),
            locations = persistentListOf(),
            searchText = "mew",
            listUiState = ListUiState.Error,
            units = TemperatureUnits.CELSIUS,
            onQueryChange = {},
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun PreviewWeatherLocationItem() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        WeatherLocationItem(
            WeatherLocationPreviewProvider().values.first(),
            onClick = {},
            spacing = Spacing(),
            units = TemperatureUnits.FAHRENHEIT,
            isDark = isSystemInDarkTheme()
        )
    }
}

private class WeatherLocationPreviewProvider : PreviewParameterProvider<LocationCurrentWeather> {
    override val values: Sequence<LocationCurrentWeather> = sequenceOf(
        LocationCurrentWeather(
            location = Location(0.512, 0.63),
            displayName = "London",
            temp = 23.4,
            weatherCondition = WeatherCondition.PARTLY_CLOUDY,
        ),
        LocationCurrentWeather(
            location = Location(1.2, -2.4),
            displayName = "New York",
            temp = null,
            weatherCondition = null,
        ),
        LocationCurrentWeather(
            location = Location(12.4, 88.0),
            displayName = "Cape Town",
            temp = 40.0,
            weatherCondition = WeatherCondition.CLEAR_SKY,
        ),
        LocationCurrentWeather(
            location = Location(8.4, 88.0),
            displayName = "Moscow",
            temp = -12.0,
            weatherCondition = WeatherCondition.SNOW_MODERATE,
        ),
        LocationCurrentWeather(
            location = Location(UNKNOWN_COORD, UNKNOWN_COORD),
            displayName = "Error",
            temp = null,
            weatherCondition = null,
            weatherError = true,
        ),
        LocationCurrentWeather(
            location = Location(112.0, 88.0),
            displayName = "Paris",
            temp = null,
            weatherCondition = null,
            weatherError = true,
        ),
    )
}