package dev.eliseo.jeff.data.local.dao

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.*
import dev.eliseo.jeff.data.model.Geoname

@Dao
abstract class GeonameDao {

    @Query("SELECT DISTINCT * FROM geoname INNER JOIN geonamelog ON geonamelog.geonameId = geoname.geonameId ORDER BY geonamelog.date DESC")
    abstract fun getRecent(): LiveData<List<Geoname>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(geoname: Geoname)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(geonames: List<Geoname>)

    @Delete
    abstract fun delete(geoname: Geoname)

    fun loadOrdered(geonameIds: List<Int>): LiveData<List<Geoname>> {
        val order = SparseIntArray()
        geonameIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(geonameIds)) { repositories ->
            repositories.sortedWith(Comparator { o1, o2 ->
                val pos1 = order.get(o1.geonameId)
                val pos2 = order.get(o2.geonameId)
                pos1 - pos2
            })
        }
    }

    @Query("SELECT * FROM geoname WHERE geonameId in (:geonameIds)")
    protected abstract fun loadById(geonameIds: List<Int>): LiveData<List<Geoname>>

    @Query("SELECT * FROM geoname WHERE geonameId == :geonameId")
    abstract fun get(geonameId: Int): LiveData<Geoname?>

}