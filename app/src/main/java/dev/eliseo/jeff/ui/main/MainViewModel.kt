package dev.eliseo.jeff.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dev.eliseo.jeff.data.manager.Repository
import dev.eliseo.jeff.data.manager.Resource
import dev.eliseo.jeff.data.model.Geoname

class MainViewModel : ViewModel() {

    val geonameSuggestionList: LiveData<List<Geoname>> = Repository.recentGeoname()

}
