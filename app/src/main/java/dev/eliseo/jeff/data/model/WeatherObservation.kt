package dev.eliseo.jeff.data.model

import androidx.recyclerview.widget.DiffUtil

data class WeatherObservation (
    val humidity: Int?,
    val lat: Double?,
    val lng: Double?,
    val stationName: String?,
    val temperature: Int?,
    val clouds: String?,
    val dewPoint: String?,
    val windSpeed: String?
) {

    companion object {

        val diffUtilItemCallback = object : DiffUtil.ItemCallback<WeatherObservation>() {
            override fun areItemsTheSame(oldItem: WeatherObservation, newItem: WeatherObservation): Boolean {
                return oldItem.stationName == newItem.stationName
            }

            override fun areContentsTheSame(oldItem: WeatherObservation, newItem: WeatherObservation): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }

}