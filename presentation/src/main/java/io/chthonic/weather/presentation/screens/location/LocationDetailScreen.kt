package io.chthonic.weather.presentation.screens.location

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.chthonic.weather.presentation.AppBarStyle
import io.chthonic.weather.presentation.AppContainerState
import io.chthonic.weather.presentation.models.ListUiState
import io.chthonic.weather.presentation.models.TemperatureUnits
import io.chthonic.weather.presentation.theme.LocalSpacing
import io.chthonic.weather.presentation.theme.Spacing
import io.chthonic.weather.presentation.widgets.EMPTY_CONTENT_KEY
import io.chthonic.weather.presentation.widgets.ERROR_CONTENT_KEY
import io.chthonic.weather.presentation.widgets.EmptyContent
import io.chthonic.weather.presentation.widgets.ErrorContent
import io.chthonic.weather.presentation.widgets.LOADING_CONTENT_KEY
import io.chthonic.weather.presentation.widgets.LoadingContent
import io.chthonic.weather.presentation.widgets.TemperatureText
import io.chthonic.weather.presentation.widgets.TemperatureUnitsButton

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
fun LocationDetailScreen(
    name: String,
    lat: Double,
    lon: Double,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    appContainerState: AppContainerState,
) {
    val viewModel =
        hiltViewModel<LocationDetailViewModel, LocationDetailViewModel.LocationDetailViewModelFactory> { factory ->
            factory.create(
                name = name,
                lat = lat,
                lon = lon,
            )
        }

    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        appContainerState.updateAppBar(
            title = state.value.name,
            showNavigationIcon = true,
            style = AppBarStyle.Large,
            actions = {
                TemperatureUnitsButton(
                    state.value.temperatureUnits,
                    viewModel::onToggleTemperatureUnits,
                )
            }
        )
    }

    LocationDetailScreenContent(
        state.value.dayWeatherList,
        state.value.listUiState,
        state.value.temperatureUnits,
    )
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun LocationDetailScreenContent(
    dayWeatherList: List<DayWeather>,
    listUiState: ListUiState,
    units: TemperatureUnits,
) {
    val spacing = LocalSpacing.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (listUiState) {
            ListUiState.Loading -> item(LOADING_CONTENT_KEY) {
                LoadingContent(
                    Modifier.fillParentMaxHeight(
                        0.5f
                    )
                )
            }

            ListUiState.Empty -> item(EMPTY_CONTENT_KEY) {
                EmptyContent(
                    spacing = spacing,
                    modifier = Modifier.fillParentMaxHeight(0.5f),
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
                    dayWeatherList.size,
                    key = { dayWeatherList[it].key },
                ) { idx ->
                    dayWeatherList.getOrNull(idx)?.let { dayWeather ->
                        DayItem(
                            state = dayWeather,
                            units = units,
                            spacing = spacing,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayItem(
    state: DayWeather,
    units: TemperatureUnits,
    spacing: Spacing,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                imageVector = state.weatherConditionIcon,
                contentDescription = state.weatherCondition.description,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        headlineContent = {
            Text(
                text = state.dayName,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TemperatureText(
                    temperature = state.maxTemp,
                    units = units,
                    valueTextStyle = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontFeatureSettings = "tnum",
                    ),
                    otherTextStyle = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                TemperatureText(
                    temperature = state.minTemp,
                    units = units,
                    valueTextStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFeatureSettings = "tnum"
                    ),
                    otherTextStyle = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}