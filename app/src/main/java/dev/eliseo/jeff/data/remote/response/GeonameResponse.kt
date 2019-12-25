package dev.eliseo.jeff.data.remote.response

import dev.eliseo.jeff.data.model.Geoname

data class GeonameResponse(val totalResultsCount: Int, val geonames: List<Geoname>)