# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build
./gradlew build

# Run all unit tests
./gradlew testDebugUnitTest

# Run unit tests for a specific module
./gradlew :feature:cityforecast:testDebugUnitTest
./gradlew :domain:testDebugUnitTest

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint
```

Key versions: Kotlin 2.3.20, AGP 9.1.0, Gradle 9.4.1, JVM target 17, minSdk 26, targetSdk 36.

## Architecture

The app is a weather forecast app (F☀️recast) using **MVVM + Clean Architecture** with a strict layered module graph. Dependencies always point inward toward the domain.

### Module Map

| Layer | Module | Responsibility |
|---|---|---|
| App | `:app` | Entry point, Hilt root, navigation host, wires feature nav graphs |
| Feature | `:feature:cityforecast` | City list & detail screens, ViewModels |
| UI | `:ui:common` | Shared Compose components, Material 3 theme, temperature-reactive color system |
| Domain | `:domain` | Use case implementations, orchestrates repos |
| Domain API | `:domain:dataapi` | Interfaces the data layer must implement |
| Domain API | `:domain:presentationapi` | Interfaces the presentation layer consumes |
| Data | `:data:weather` | OpenMeteo API client |
| Data | `:data:geocoding` | Nominatim geocoding/city search |
| Data | `:data:location` | Device GPS location with permission handling |
| Data | `:data:common` | Ktor `HttpClient` + JSON serializer, shared across data modules |
| Shared | `:common` | Cross-layer models |

### Data Flow

`Presentation` → `domain:presentationapi` → `domain` → `domain:dataapi` → `data:*`

Each layer only depends on the interfaces defined in the API modules, never on concrete implementations from another layer.

### Key Conventions

- **ViewModels** hold `MutableStateFlow<State>` internally, expose read-only `StateFlow<State>`. Navigation events use `Channel.receiveAsFlow()`.
- **UI state logic** is extracted into resolver classes (e.g. `CityListUiStateResolver`) to keep ViewModels thin and testable.
- **Assisted injection** is used where ViewModels need nav arguments — see `CityDetailViewModel` with `@HiltViewModel(assistedFactory = ...)`.
- **Feature modules** expose a single `NavGraphBuilder` extension (e.g. `cityForecastNavGraph()`) registered in `:app`.
- **KSP** is used for annotation processing (not KAPT).
- All Hilt modules use `@InstallIn(SingletonComponent::class)`.
- All dependencies are declared in `gradle/libs.versions.toml`.

### Networking

Ktor Client (OkHttp engine) with: Content Negotiation (kotlinx.serialization), Timber logging, 10s connect / 30s socket / 30s request timeouts, and automatic retry (3x with exponential backoff) on server errors.

External APIs used (both free, no API keys required):
- **OpenMeteo** — weather data
- **Nominatim** — geocoding / city search

### Testing Stack

JUnit 4, MockK, and `kotlinx-coroutines-test`. Unit tests live in `src/test/java`. CI (GitHub Actions) runs `testDebugUnitTest` on push/PR to main.
