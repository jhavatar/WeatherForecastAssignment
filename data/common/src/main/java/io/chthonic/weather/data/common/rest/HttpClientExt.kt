package io.chthonic.weather.data.common.rest

import io.chthonic.weather.common.models.Outcome
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.ExperimentalSerializationApi
import timber.log.Timber

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified T> HttpClient.executeAsResult(
    crossinline request: suspend HttpClient.() -> HttpResponse,
): Outcome<T> = try {
    val response = request()
    when (response.status.value) {
        in 200..299 -> Outcome.Success(response.body())
        else -> {
            val message = try {
                response.bodyAsText()
            } catch (_: NoTransformationFoundException) {
                "No error message supplied"
            } catch (_: JsonConvertException) {
                "No error message supplied"
            }
            Timber.e("Error response: $message")
            Outcome.Error(
//                status = response.status.value,
                message = message ?: "Unspecified error"
            )
        }
    }
} catch (e: Exception) {
    Outcome.Error(e.message ?: "Unknown error")
}