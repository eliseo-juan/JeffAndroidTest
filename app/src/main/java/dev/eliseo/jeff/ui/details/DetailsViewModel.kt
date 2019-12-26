package dev.eliseo.jeff.ui.details

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dev.eliseo.jeff.data.manager.Repository
import dev.eliseo.jeff.data.manager.Resource
import dev.eliseo.jeff.data.manager.Status
import dev.eliseo.jeff.data.model.Geoname
import dev.eliseo.jeff.data.model.WeatherObservation
import kotlin.math.roundToInt

class DetailsViewModelFactory(val geonameId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(geonameId) as T
    }
}

class DetailsViewModel(val geonameId: Int) : ViewModel() {
    private val geoname: LiveData<Geoname?> = Repository.getGeoname(geonameId)
    val weatherObservations: LiveData<Resource<List<WeatherObservation>>>

    val titleText: LiveData<String>
    val subtitleText: LiveData<String>
    val bodyText: LiveData<String>
    val mapMarkers: LiveData<Resource<List<MarkerOptions>>>
    val mapBounds: LiveData<LatLngBounds>
    val showPlaceholder = MediatorLiveData<Boolean?>()
    val temperature = MediatorLiveData<Int?>()
    val cloudText = MediatorLiveData<String?>()
    val isCloudTextVisible = MediatorLiveData<Boolean?>()
    val temperatureText = MediatorLiveData<String?>()
    val isTemperatureVisible = MediatorLiveData<Boolean?>()
    val humidityText = MediatorLiveData<String?>()
    val isHumidityVisible = MediatorLiveData<Boolean?>()
    val windSpeedText = MediatorLiveData<String?>()
    val isWindSpeedVisible = MediatorLiveData<Boolean?>()
    val dewPointText = MediatorLiveData<String?>()
    val isDewPointVisible = MediatorLiveData<Boolean?>()

    init {

        Repository.setRecentGeoname(geonameId)

        titleText = Transformations.map(geoname) {
            it?.name
        }
        subtitleText = Transformations.map(geoname) {
            it?.toponymName
        }
        bodyText = Transformations.map(geoname) {
            it?.countryName
        }

        weatherObservations = Transformations.switchMap(geoname) {
            Repository.getWeatherObservations(
                it?.bbox?.north,
                it?.bbox?.south,
                it?.bbox?.east,
                it?.bbox?.west
            )
        }

        mapMarkers = Transformations.map(weatherObservations) { resource ->
            return@map when (resource?.status) {
                Status.LOADING -> Resource.loading<List<MarkerOptions>>(null)
                Status.SUCCESS -> Resource.success(resource.data?.filter {
                    it.lat != null && it.lng != null
                }?.map {
                    MarkerOptions().position(LatLng(it.lat!!, it.lng!!)).title(it.stationName)
                })
                Status.ERROR -> Resource.error<List<MarkerOptions>>(resource.message, null)
                else -> Resource.loading<List<MarkerOptions>>(null)
            }
        }

        mapBounds = Transformations.map(geoname) {
            if (it?.bbox?.north == null || it.bbox.east == null || it.bbox.south == null || it.bbox.west == null) {
                return@map null
            }
            LatLngBounds(LatLng(it.bbox.south, it.bbox.west), LatLng(it.bbox.north, it.bbox.east))
        }

        showPlaceholder.addSource(weatherObservations) {
            showPlaceholder.value = (it?.status == Status.SUCCESS && it.data?.isEmpty() == true)
        }

        temperature.addSource(weatherObservations) { resource ->
            resource?.data?.mapNotNull { it.temperature }?.average()?.takeIf { !it.isNaN() }
                ?.roundToInt()
                ?.let { temperature.value = it }
        }

        temperatureText.addSource(temperature) { temperature ->
            temperature?.toString()?.let { String.format("%sºC", it) }
                ?.let { temperatureText.value = it }
        }

        isTemperatureVisible.addSource(temperatureText) { temperatureText ->
            temperatureText?.isNotBlank()?.let { isTemperatureVisible.value = it }
        }

        humidityText.addSource(weatherObservations) { resource ->
            resource?.data?.mapNotNull { it.humidity }?.average()?.takeIf { !it.isNaN() }
                ?.roundToInt()
                ?.let { humidityText.value = String.format("%d%%", it) }
        }

        isHumidityVisible.addSource(humidityText) { resource ->
            resource?.isNotBlank()?.let { isHumidityVisible.value = it }
        }

        cloudText.addSource(weatherObservations) { resource ->
            resource?.data?.mapNotNull { it.clouds }?.firstOrNull()
                ?.let { cloudText.value = it }
        }

        isCloudTextVisible.addSource(cloudText) { resource ->
            resource?.isNotBlank()?.let { isCloudTextVisible.value = it }
        }

        windSpeedText.addSource(weatherObservations) { resource ->
            resource?.data?.mapNotNull { it.windSpeed?.toDoubleOrNull() }?.average()
                ?.takeIf { !it.isNaN() }?.roundToInt()
                ?.let { windSpeedText.value = String.format("%dkm/h", it) }
        }

        isWindSpeedVisible.addSource(windSpeedText) { resource ->
            resource?.isNotBlank()?.let { isWindSpeedVisible.value = it }
        }

        dewPointText.addSource(weatherObservations) { resource ->
            resource?.data?.mapNotNull { it.dewPoint?.toDoubleOrNull() }?.average()
                ?.takeIf { !it.isNaN() }?.roundToInt()
                ?.let { dewPointText.value = String.format("%s%%", it) }
        }

        isDewPointVisible.addSource(dewPointText) { resource ->
            resource?.isNotBlank()?.let { isDewPointVisible.value = it }
        }
    }
}
