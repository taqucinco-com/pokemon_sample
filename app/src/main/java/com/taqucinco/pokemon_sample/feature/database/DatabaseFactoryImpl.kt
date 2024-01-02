package com.taqucinco.pokemon_sample.feature.database

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DatabaseFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): DatabaseFactory {
    override fun build(): AppDatabase {
        val dbName = "application-database"

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            dbName
        ).build()
    }
}
