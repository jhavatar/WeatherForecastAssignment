package io.chthonic.weather.data.common

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CommonSingletonModule {

    @Provides
    @Singleton
    fun provideJsonClient(): Json {
        return Json {
            encodeDefaults = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            allowStructuredMapKeys = true
            prettyPrint = false
            useArrayPolymorphism = false
            explicitNulls = false
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(jsonClient: Json): HttpClient {
        return HttpClient(OkHttp) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Timber.v("HttpsClient traffic: $message")
                    }
                }
            }

            install(HttpTimeout) {
                connectTimeoutMillis = 10_000   // TCP + TLS handshake
                socketTimeoutMillis = 30_000    // waiting for data
                requestTimeoutMillis = 30_000   // total request time
            }

            install(ContentNegotiation) {
                json(jsonClient)
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(3)
                exponentialDelay()
            }
        }
    }
}