package dev.eliseo.jeff.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.eliseo.jeff.data.model.GeonameLog

@Dao
interface GeonameLogDao {

    @Query("SELECT * FROM geonamelog")
    fun getAll(): LiveData<List<GeonameLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(geonameLog: GeonameLog)

    @Delete
    fun delete(geonameLog: GeonameLog)
}