package dev.eliseo.jeff.data.remote.response

import dev.eliseo.jeff.data.model.WeatherObservation

data class WeatherResponse(val weatherObservations: List<WeatherObservation>)