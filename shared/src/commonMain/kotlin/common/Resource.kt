package common

sealed class Resource<T> {
    class Data<T>(
        val payload: T
    ) : Resource<T>()

    class Error<T>(
        val message: String = ""
    ) : Resource<T>()

    class Loading<T> : Resource<T>()
}