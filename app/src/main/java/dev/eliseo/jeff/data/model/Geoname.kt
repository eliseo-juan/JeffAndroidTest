package dev.eliseo.jeff.data.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Geoname(
    @PrimaryKey val geonameId: Int,
    val name: String?,
    val toponymName: String?,
    val countryName: String?,
    val lat: Double?,
    val lng: Double?,
    @Embedded val bbox: Bbox?
) {

    companion object {

        val diffUtilItemCallback = object : DiffUtil.ItemCallback<Geoname>() {
            override fun areItemsTheSame(oldItem: Geoname, newItem: Geoname): Boolean {
                return oldItem.geonameId == newItem.geonameId
            }

            override fun areContentsTheSame(oldItem: Geoname, newItem: Geoname): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}