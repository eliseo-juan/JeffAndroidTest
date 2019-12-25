package dev.eliseo.jeff.data.remote.util

import retrofit2.Response

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(
                error.message ?: "unknown error"
            )
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            val body = response.body()
            return if (response.isSuccessful && body != null) {
                ApiSuccessResponse(body = body)
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(
                    errorMsg ?: "unknown error"
                )
            }
        }
    }
}

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()
data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()