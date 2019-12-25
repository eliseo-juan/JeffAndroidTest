package dev.eliseo.jeff.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.eliseo.jeff.data.model.Geoname
import dev.eliseo.jeff.data.model.GeonameSearchResult

@Dao
interface GeonameSearchResultDao {

    @Query("SELECT * FROM geonamesearchresult WHERE `query` = :query")
    fun search(query: String): LiveData<GeonameSearchResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(geonameSearchResults: List<GeonameSearchResult>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(geonameSearchResult: GeonameSearchResult)

    @Delete
    fun delete(geonameSearchResult: GeonameSearchResult)
}