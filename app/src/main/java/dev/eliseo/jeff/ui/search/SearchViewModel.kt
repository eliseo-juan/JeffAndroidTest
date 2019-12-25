package dev.eliseo.jeff.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dev.eliseo.jeff.data.manager.Repository
import dev.eliseo.jeff.data.manager.Resource
import dev.eliseo.jeff.data.model.Geoname

class SearchViewModel : ViewModel() {

    private val searchQuery = MutableLiveData<String>()
    val geonameSearchList: LiveData<Resource<List<Geoname>>>

    init {
        geonameSearchList = Transformations.switchMap(searchQuery) {
            Repository.searchGeoname(it)
        }
    }

    fun setQuery(query: String) {
        if (searchQuery.value != query) {
            searchQuery.value = query
        }
    }
}
