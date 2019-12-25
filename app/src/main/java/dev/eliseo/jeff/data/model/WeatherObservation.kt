package dev.eliseo.jeff.data.model

data class WeatherObservation (
    val humidity: Int?,
    val lat: Double?,
    val lng: Double?,
    val stationName: String?,
    val temperature: Int?,
    val clouds: String?,
    val dewPoint: String?,
    val windSpeed: String?
)