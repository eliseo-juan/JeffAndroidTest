package dev.eliseo.jeff.ui.common

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import dev.eliseo.jeff.R
import dev.eliseo.jeff.data.model.Geoname
import kotlinx.android.synthetic.main.row_geoname.view.*

class GeonameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView = itemView.textViewRowsuggestionGeonameTitle
    private val subtitleTextView = itemView.textViewRowsuggestionGeonameSubtitle

    fun bind(geoname: Geoname){
        titleTextView.text = geoname.name
        subtitleTextView.text = geoname.toponymName
    }

    companion object {
        @LayoutRes
        const val layoutId: Int =  R.layout.row_geoname
    }
}