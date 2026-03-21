package io.chthonic.weather.common.models

sealed interface Outcome<T> {
    data class Success<T>(val data: T) : Outcome<T>
    data class Error<T>(val message: String) : Outcome<T>

    fun <R> map(transform: (T) -> R): Outcome<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(message)
    }
}