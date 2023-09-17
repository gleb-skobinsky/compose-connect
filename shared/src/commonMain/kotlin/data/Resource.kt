package data

import io.ktor.http.*

sealed class Resource<T> {
    data class Data<T>(
        val payload: T
    ) : Resource<T>()

    data class Error<T>(
        val message: String = "",
        val status: HttpStatusCode
    ) : Resource<T>()
}