package dev.eliseo.jeff.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeonameSearchResult(
    @PrimaryKey val query: String,
    val geonameIds: List<Int>
)