package dev.eliseo.jeff.ui.details

import androidx.lifecycle.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dev.eliseo.jeff.data.manager.Repository
import dev.eliseo.jeff.data.manager.Resource
import dev.eliseo.jeff.data.manager.Status
import dev.eliseo.jeff.data.model.Geoname
import dev.eliseo.jeff.data.model.WeatherObservation

class DetailsViewModelFactory(val geonameId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(geonameId) as T
    }
}

class DetailsViewModel(val geonameId: Int) : ViewModel() {
    val geoname: LiveData<Geoname?> = Repository.getGeoname(geonameId)
    val title: LiveData<String>
    val weatherObservations: LiveData<Resource<List<WeatherObservation>>>
    val mapMarkers: LiveData<Resource<List<MarkerOptions>>>
    val mapBounds: LiveData<LatLngBounds>

    init {

        Repository.setRecentGeoname(geonameId)

        title = Transformations.map(geoname) {
            it?.name
        }

        weatherObservations = Transformations.switchMap(geoname) {
            Repository.getWeatherObservations(it?.bbox?.north, it?.bbox?.south, it?.bbox?.east, it?.bbox?.west)
        }

        mapMarkers = Transformations.map(weatherObservations) { resource ->
            return@map when(resource.status){
                Status.LOADING -> Resource.loading<List<MarkerOptions>>(null)
                Status.SUCCESS -> Resource.success(resource.data?.filter {
                    it.lat != null && it.lng != null
                }?.map {
                    MarkerOptions().position(LatLng(it.lat!!, it.lng!!)).title(it.stationName)
                })
                Status.ERROR -> Resource.error<List<MarkerOptions>>(resource.message, null)
            }
        }

        mapBounds = Transformations.map(geoname) {
            if(it?.bbox?.north == null || it.bbox.east == null || it.bbox.south == null || it.bbox.west == null){
                return@map null
            }
            LatLngBounds(LatLng(it.bbox.south, it.bbox.west), LatLng(it.bbox.north, it.bbox.east))
        }
    }
}
