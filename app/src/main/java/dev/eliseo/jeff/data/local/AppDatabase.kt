package dev.eliseo.jeff.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.eliseo.jeff.App
import dev.eliseo.jeff.data.local.converters.DateTypeConverter
import dev.eliseo.jeff.data.local.converters.IntListTypeConverter
import dev.eliseo.jeff.data.local.dao.GeonameDao
import dev.eliseo.jeff.data.local.dao.GeonameLogDao
import dev.eliseo.jeff.data.local.dao.GeonameSearchResultDao
import dev.eliseo.jeff.data.model.Geoname
import dev.eliseo.jeff.data.model.GeonameLog
import dev.eliseo.jeff.data.model.GeonameSearchResult

@Database(entities = [
    Geoname::class,
    GeonameLog::class,
    GeonameSearchResult::class
], version = AppDatabaseMigrations.version)
@TypeConverters(DateTypeConverter::class, IntListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun geonameDao(): GeonameDao

    abstract fun geonameSearchResult(): GeonameSearchResultDao

    abstract fun geonameLog(): GeonameLogDao

    companion object {

            val db by lazy {
            Room.databaseBuilder(App.context, AppDatabase::class.java, "database-name")
                .allowMainThreadQueries()
                .addMigrations(*AppDatabaseMigrations.migrations)
                .build()
        }
    }
}