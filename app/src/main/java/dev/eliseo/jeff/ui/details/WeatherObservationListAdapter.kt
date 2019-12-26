package dev.eliseo.jeff.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import dev.eliseo.jeff.data.model.WeatherObservation
import dev.eliseo.jeff.ui.common.WeatherObservationViewHolder

class WeatherObservationListAdapter() :
    ListAdapter<WeatherObservation, WeatherObservationViewHolder>(WeatherObservation.diffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherObservationViewHolder {
        return WeatherObservationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(WeatherObservationViewHolder.layoutId, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherObservationViewHolder, position: Int) {
        val weatherObservation = getItem(position)
        holder.bind(weatherObservation)
    }
}



