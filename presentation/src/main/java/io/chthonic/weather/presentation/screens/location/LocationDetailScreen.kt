package io.chthonic.weather.presentation.screens.location

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.chthonic.weather.presentation.AppBarStyle
import io.chthonic.weather.presentation.AppContainerState
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
import io.chthonic.weather.presentation.widgets.LOADING_CONTENT_KEY
import io.chthonic.weather.presentation.widgets.LoadingContent
import io.chthonic.weather.presentation.widgets.PreviewSharedAnimation
import io.chthonic.weather.presentation.widgets.TemperatureText
import io.chthonic.weather.presentation.widgets.TemperatureUnitsButton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
fun LocationDetailScreen(
    name: String,
    lat: Double,
    lon: Double,
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

    val actions: (@Composable RowScope.() -> Unit) = remember(state.value.temperatureUnits) {
        {
            TemperatureUnitsButton(
                state.value.temperatureUnits,
                viewModel::onToggleTemperatureUnits,
            )
        }
    }
    appContainerState.updateAppBarForDestination(
        destination = Destination.LocationDetail,
        title = state.value.name,
        showNavigationIcon = true,
        style = AppBarStyle.Large,
        actions = actions,
    )

    LocationDetailScreenContent(
        state.value.dayWeatherList,
        state.value.listUiState,
        state.value.temperatureUnits,
    )
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun LocationDetailScreenContent(
    dayWeatherList: ImmutableList<DayWeather>,
    listUiState: ListUiState,
    units: TemperatureUnits,
) {
    val spacing = LocalSpacing.current
    val isDark = isSystemInDarkTheme()
    LazyColumn(
        contentPadding = PaddingValues(vertical = spacing.m),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
        modifier = Modifier.fillMaxSize(),
    ) {
        when (listUiState) {
            ListUiState.Loading, ListUiState.Idle -> item(LOADING_CONTENT_KEY) {
                LoadingContent(
                    spacing = spacing,
                    Modifier.fillParentMaxHeight(0.5f),
                )
            }

            ListUiState.Empty -> item(EMPTY_CONTENT_KEY) {
                EmptyContent(
                    spacing = spacing,
                    text = "No weather data available",
                    icon = Icons.Outlined.WbSunny,
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
                            isDark = isDark,
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
    isDark: Boolean,
    spacing: Spacing,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.m, vertical = spacing.xs),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.temperatureCardColor(state.maxTemp, isDark),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        ListItem(
            modifier = modifier,
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent, // let the Card background show
            ),
            leadingContent = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = AppColors.temperatureIconBoxColor(state.maxTemp, isDark),
                            shape = MaterialTheme.shapes.medium,
                        ),
                ) {
                    Icon(
                        imageVector = state.weatherConditionIcon,
                        contentDescription = state.weatherCondition.description,
                        modifier = Modifier.size(28.dp),
                        tint = AppColors.temperatureIconTint(state.maxTemp, isDark),
                    )
                }
            },
            headlineContent = {
                Text(
                    text = state.dayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
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
                        valueTextStyle = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontFeatureSettings = "tnum",
                        ),
                        unitsTextStyle = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                        color = AppColors.temperatureTextColor(state.maxTemp, isDark),
                    )
                    TemperatureText(
                        temperature = state.minTemp,
                        units = units,
                        valueTextStyle = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontFeatureSettings = "tnum"
                        ),
                        unitsTextStyle = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
        )
    }
}


@PreviewLightDark
@Composable
private fun PreviewListContentState() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        LocationDetailScreenContent(
            dayWeatherList = DayWeatherPreviewProvider().values.toImmutableList(),
            listUiState = ListUiState.Content,
            units = TemperatureUnits.CELSIUS,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewListEmptyState() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        LocationDetailScreenContent(
            dayWeatherList = persistentListOf(),
            listUiState = ListUiState.Empty,
            units = TemperatureUnits.CELSIUS,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewListLoadingState() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        LocationDetailScreenContent(
            dayWeatherList = persistentListOf(),
            listUiState = ListUiState.Loading,
            units = TemperatureUnits.CELSIUS,
        )
    }
}


@PreviewLightDark
@Composable
private fun PreviewListErrorState() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        LocationDetailScreenContent(
            dayWeatherList = persistentListOf(),
            listUiState = ListUiState.Error,
            units = TemperatureUnits.CELSIUS,
        )
    }
}

@PreviewLightDark
@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun PreviewWeatherLDayItem() {
    PreviewSharedAnimation { sharedTransitionScope, animatedContentScope ->
        DayItem(
            DayWeatherPreviewProvider().values.first(),
            spacing = Spacing(),
            units = TemperatureUnits.FAHRENHEIT,
            isDark = isSystemInDarkTheme()
        )
    }
}

private class DayWeatherPreviewProvider : PreviewParameterProvider<DayWeather> {
    override val values: Sequence<DayWeather> = sequenceOf(
        DayWeather(
            maxTemp = 23.0,
            minTemp = 23.0,
            weatherCondition = WeatherCondition.PARTLY_CLOUDY,
            date = LocalDate.now(),
        ),
        DayWeather(
            maxTemp = -1.0,
            minTemp = -12.0,
            weatherCondition = WeatherCondition.SNOW_MODERATE,
            date = LocalDate.now().plusDays(1),
        ),
        DayWeather(
            maxTemp = 40.0,
            minTemp = 31.0,
            weatherCondition = WeatherCondition.CLEAR_SKY,
            date = LocalDate.now().plusDays(2),
        ),
        DayWeather(
            maxTemp = 28.0,
            minTemp = 27.0,
            weatherCondition = WeatherCondition.THUNDERSTORM,
            date = LocalDate.now().plusDays(3),
        ),
        DayWeather(
            maxTemp = 18.0,
            minTemp = 14.0,
            weatherCondition = WeatherCondition.OVERCAST,
            date = LocalDate.now().plusDays(4),
        ),
        DayWeather(
            maxTemp = 10.0,
            minTemp = 5.0,
            weatherCondition = WeatherCondition.RAIN_MODERATE,
            date = LocalDate.now().plusDays(5),
        ),
        DayWeather(
            maxTemp = 23.4,
            minTemp = 23.4,
            weatherCondition = WeatherCondition.RAIN_SLIGHT,
            date = LocalDate.now().plusDays(6),
        ),
    )
}