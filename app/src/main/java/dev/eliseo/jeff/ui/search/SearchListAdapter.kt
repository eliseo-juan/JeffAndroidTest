package dev.eliseo.jeff.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import dev.eliseo.jeff.data.model.Geoname
import dev.eliseo.jeff.ui.common.GeonameViewHolder

class SearchListAdapter(private val listener: (Geoname) -> Unit) :
    ListAdapter<Geoname, GeonameViewHolder>(Geoname.diffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeonameViewHolder {
        return GeonameViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(GeonameViewHolder.layoutId, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GeonameViewHolder, position: Int) {
        val geoname = getItem(position)
        holder.bind(geoname)
        holder.itemView.setOnClickListener {
            listener(geoname)
        }
    }
}



