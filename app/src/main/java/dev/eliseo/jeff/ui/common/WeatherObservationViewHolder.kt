package dev.eliseo.jeff.ui.common

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import dev.eliseo.jeff.R
import dev.eliseo.jeff.data.model.WeatherObservation
import kotlinx.android.synthetic.main.row_weather_observation.view.*

class WeatherObservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView = itemView.textViewRowWeatherObservationTitle

    fun bind(weatherObservation: WeatherObservation){
        titleTextView.text = weatherObservation.stationName
    }

    companion object {
        @LayoutRes
        const val layoutId: Int =  R.layout.row_weather_observation
    }
}