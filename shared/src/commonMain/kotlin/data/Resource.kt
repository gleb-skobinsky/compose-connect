package data

import io.ktor.http.*

sealed class Resource<T> {
    class Data<T>(
        val payload: T
    ) : Resource<T>()

    class Error<T>(
        message: String = "",
        status: HttpStatusCode
    ) : Resource<T>()
}