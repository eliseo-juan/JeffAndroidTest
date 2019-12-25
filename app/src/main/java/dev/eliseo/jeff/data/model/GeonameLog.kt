package dev.eliseo.jeff.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class GeonameLog(
    @PrimaryKey val geonameId: Int,
    val date: Date
)