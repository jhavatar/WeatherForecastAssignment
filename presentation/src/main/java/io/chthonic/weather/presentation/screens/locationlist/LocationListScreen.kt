package io.chthonic.weather.presentation.screens.locationlist

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.chthonic.weather.common.models.Location
import io.chthonic.weather.presentation.R
import io.chthonic.weather.presentation.models.TemperatureUnits
import io.chthonic.weather.presentation.models.WeatherCondition
import io.chthonic.weather.presentation.nav.Destination
import io.chthonic.weather.presentation.theme.LocalSpacing
import io.chthonic.weather.presentation.theme.Spacing
import io.chthonic.weather.presentation.widgets.PreviewSharedAnimation
import io.chthonic.weather.presentation.widgets.TemperatureText
import kotlinx.collections.immutable.ImmutableList

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
fun LocationListScreen(
    viewModel: LocationListViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    updateAppBarTitle: (String?) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        updateAppBarTitle(context.resources.getString(R.string.app_name))
    }

    viewModel.navigateSideEffect.collectAsStateWithLifecycle().value?.let { sideEffect ->
        when (val navTarget = sideEffect.getContentIfNotHandled()) {
            is LocationListViewModel.NavigationTarget.CharacterScreen -> {
                navController.navigate(Destination.Character.buildUniqueRoute(navTarget.characterId))
            }

            else -> { // ignore
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    LocationListContent(
        locations = state.value.locations,
        searchText = state.value.searchText,
        onQueryChange = viewModel::onQueryChange,
        onClick = viewModel::onLocationClick,
    )

//    when (val loadState = lazyCharInfoItems.loadState.refresh) {
//        is LoadState.Loading -> LoadingProgress()
//        is LoadState.Error -> showSnackbar(
//            loadState.error.message ?: "Loading characters failed",
//            SnackbarDuration.Short,
//        )
//
//        else -> {}
//    }
//    when (val loadState = lazyCharInfoItems.loadState.append) {
//        is LoadState.Error -> showSnackbar(
//            loadState.error.message ?: "Loading characters failed",
//            SnackbarDuration.Short
//        )
//
//        else -> {}
//    }
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun LocationListContent(
    locations: ImmutableList<LocationCurrentWeather>,
    searchText: String,
    onQueryChange: (String) -> Unit,
    onClick: (LocationCurrentWeather) -> Unit
) {
    val spacing = LocalSpacing.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
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

        items(
            locations.size,
            key = {
                locations[it].hashCode()
            },
        ) { idx ->
            locations.getOrNull(idx)?.let { locationState ->
                WeatherLocationItem(locationState, spacing = spacing) { onClick(locationState) }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun WeatherLocationItem(
    state: LocationCurrentWeather,
    units: TemperatureUnits = TemperatureUnits.CELSIUS,
    spacing: Spacing,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.m),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(spacing.m),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = state.displayName,
                maxLines = 2,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier,
            )

            Text(
                text = state.displayCoords,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = spacing.xs),
            )
        }


        (state.weatherConditionIcon ?: Icons.Outlined.HourglassEmpty).let { icon ->
            Icon(
                icon,
                contentDescription = state.weatherCondition?.description,
                modifier = Modifier.size(36.dp)
            )
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier) {
            state.temp?.let {
                TemperatureText(
                    temperature = state.temp,
                    units = units,
                    color = MaterialTheme.colorScheme.primary,
                    textStyle = MaterialTheme.typography.displayMedium,
                )
            }
            this@Row.AnimatedVisibility(visible = !state.hasTemp) {
                CircularProgressIndicator()
            }
        }
    }
}

//@OptIn(ExperimentalSharedTransitionApi::class)
//@Preview
//@Composable
//private fun PreviewLocationListContent() {
//    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
//        LocationListContent(
//            flowOf(
//                PagingData.from(
//                    CharInfoPreviewProvider().values.toList()
//                )
//            ).collectAsLazyPagingItems(),
//            sharedTransitionScope = sharedTransitionScope,
//            animatedContentScope = animatedContentScope,
//        ) {}
//    }
//}

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
                placeholder = { Text("Search sessions") },
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

@Preview
@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun PreviewWeatherLocationItem() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        WeatherLocationItem(
            WeatherLocationPreviewProvider().values.first(),
            onClick = {},
            spacing = Spacing(),
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
            displayName = "new York",
            temp = null,
            weatherCondition = null,
        ),
    )
}