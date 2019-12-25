package dev.eliseo.jeff.data.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import dev.eliseo.jeff.data.local.AppDatabase
import dev.eliseo.jeff.data.model.Geoname
import dev.eliseo.jeff.data.model.GeonameLog
import dev.eliseo.jeff.data.model.GeonameSearchResult
import dev.eliseo.jeff.data.model.WeatherObservation
import dev.eliseo.jeff.data.remote.Server
import dev.eliseo.jeff.data.remote.response.GeonameResponse
import dev.eliseo.jeff.data.remote.response.WeatherResponse
import dev.eliseo.jeff.data.remote.util.AbsentLiveData
import dev.eliseo.jeff.data.remote.util.ApiResponse
import dev.eliseo.jeff.data.remote.util.ApiSuccessResponse
import java.util.*

object Repository {

    val api = Server.api
    val db = AppDatabase.db

    fun getGeoname(geonameId: Int): LiveData<Geoname?> {
        return db.geonameDao().get(geonameId)
    }

    fun setRecentGeoname(geonameId: Int) {
        db.geonameLog().insert(GeonameLog(geonameId, Date()))
    }

    fun recentGeoname(): LiveData<List<Geoname>> {
        return db.geonameDao().getRecent()
    }

    fun searchGeoname(query: String): LiveData<Resource<List<Geoname>>> {
        return object : NetworkBoundResource<List<Geoname>, GeonameResponse>() {

            override fun saveCallResult(item: GeonameResponse) {
                val geonameIds = item.geonames.mapNotNull { it.geonameId }
                val searchResult = GeonameSearchResult(query, geonameIds)
                db.runInTransaction {
                    db.geonameDao().insert(item.geonames)
                    db.geonameSearchResult().insert(searchResult)
                }
            }

            override fun shouldFetch(data: List<Geoname>?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<List<Geoname>> {
                return Transformations.switchMap(db.geonameSearchResult().search(query)) { searchData ->
                    if (searchData == null) {
                        AbsentLiveData.create()
                    } else {
                        db.geonameDao().loadOrdered(searchData.geonameIds)
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<GeonameResponse>> {
                return api.search(query, 20, 0, "en", true, "FULL", "ilgeonamessample")
            }
        }.asLiveData()
    }

    fun getWeatherObservations(
        north: Double?,
        south: Double?,
        east: Double?,
        west: Double?
    ): LiveData<Resource<List<WeatherObservation>>>? {
        if (north == null || south == null || east == null || west == null) {
            return AbsentLiveData.create()
        }
        return object : NetworkResource<List<WeatherObservation>, WeatherResponse>() {

            override fun createCall(): LiveData<ApiResponse<WeatherResponse>> {
                return api.getWeather(south = south, north = north, east = east, west = west, username = "ilgeonamessample")
            }

            override fun processResponse(response: ApiSuccessResponse<WeatherResponse>): List<WeatherObservation> {
                return response.body.weatherObservations
            }
        }.asLiveData()
    }
}