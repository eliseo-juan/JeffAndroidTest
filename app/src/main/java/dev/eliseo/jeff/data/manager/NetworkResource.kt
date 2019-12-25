package dev.eliseo.jeff.data.manager

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import dev.eliseo.jeff.AppExecutors
import dev.eliseo.jeff.data.remote.util.ApiErrorResponse
import dev.eliseo.jeff.data.remote.util.ApiResponse
import dev.eliseo.jeff.data.remote.util.ApiSuccessResponse


/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkResource<ResultType, RequestType>
@MainThread constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        fetchFromNetwork()
    }


    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        setValue(Resource.loading(null))
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse<RequestType> -> {
                    AppExecutors.mainThread.execute {
                        setValue(Resource.success(processResponse(response)))
                    }
                }
                is ApiErrorResponse<RequestType> -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage, null))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected abstract fun processResponse(response: ApiSuccessResponse<RequestType>) : ResultType

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}