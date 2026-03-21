package io.chthonic.weather.data.common

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CommonSingletonModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

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
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor(
                logger = HttpLoggingInterceptor.Logger.DEFAULT
            ).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()


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

//            val dateIsoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).apply {
//                timeZone = TimeZone.getDefault() // or TimeZone.getTimeZone("UTC")
//            }
//            val iso8601DateWithColon: () -> String = {
//                val raw = dateIsoFormatter.format(Date()) // e.g. 2025-08-25T11:42:31.123+0200
//                raw.substring(0, raw.length - 2) + ":" + raw.substring(raw.length - 2)
//            }
//
//            install(DefaultRequest) {
//                url("${BuildConfig.SESSION_ENDPOINTS_BASE_URL}/")
//                header(HttpHeaders.ContentType, ContentType.Application.Json)
//                headers {
//                    append("CI-OSVersion", "${Build.VERSION.SDK_INT}")
//                    append("CI-AppVersion", BuildConfig.VERSION_NAME)
//                    append("CI-Locale", Locale.getDefault().toLanguageTag())
//                    append("CI-DateTime", iso8601DateWithColon())
//                }
//            }

            install(ContentNegotiation) {
                json(jsonClient)
            }

//            install(Auth) {
//                bearer {
//                    loadTokens {
//                        val tokenState = authTokenProvider.getValidIdToken()
//                        tokenState.accessToken?.let {
//                            BearerTokens(
//                                accessToken = it,
//                                refreshToken = tokenState.refreshToken,
//                            )
//                        }
//                    }
//                }
//            }

            install(HttpRequestRetry) {
                retryOnServerErrors(3)

                // Note, explicitly exclude auth failures since handled manually
                retryIf { _, response ->
                    when (response.status) {
                        HttpStatusCode.InternalServerError,
                        HttpStatusCode.BadGateway,
                        HttpStatusCode.ServiceUnavailable,
                        HttpStatusCode.GatewayTimeout -> true

                        else -> false
                    }
                }

                exponentialDelay()
            }
        }
    }
}