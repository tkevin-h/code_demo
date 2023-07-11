package com.thavin.vintrace.util

sealed class ResourceResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>(val isLoading: Boolean = true) : ResourceResult<T>()
    class Success<T>(data: T) : ResourceResult<T>(data)
    class Error<T>(data: T? = null, message: String) : ResourceResult<T>(data, message)
}
