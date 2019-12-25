package dev.eliseo.jeff.data.remote

import androidx.lifecycle.LiveData
import dev.eliseo.jeff.data.remote.response.GeonameResponse
import dev.eliseo.jeff.data.remote.response.WeatherResponse
import dev.eliseo.jeff.data.remote.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("searchJSON")
    fun search(@Query("q") query: String,
               @Query("maxRows") maxRows: Int?,
               @Query("startRow") startRow: Int?,
               @Query("lang") lang: String?,
               @Query("isNameRequired") isNameRequired: Boolean?,
               @Query("style") style: String?,
               @Query("username") username: String) : LiveData<ApiResponse<GeonameResponse>>

    @GET("weatherJSON")
    fun getWeather(@Query("north") north: Double,
               @Query("south") south: Double,
               @Query("east") east: Double,
               @Query("west") west: Double,
               @Query("username") username: String) : LiveData<ApiResponse<WeatherResponse>>

}