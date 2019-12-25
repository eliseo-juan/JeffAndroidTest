package dev.eliseo.jeff.data.local

import androidx.room.migration.Migration

object AppDatabaseMigrations {

    const val version: Int = 1

    val migrations by lazy { arrayOf<Migration>() }
}