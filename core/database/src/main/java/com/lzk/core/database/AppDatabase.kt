package com.lzk.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()

object RoomManager {
    private var database: AppDatabase? = null

    fun init(context: Context, databaseClass: Class<out AppDatabase>, name: String) {
        database = Room.databaseBuilder(
            context.applicationContext,
            databaseClass,
            name
        ).build()
    }

    fun <T : AppDatabase> getDatabase(): T? {
        @Suppress("UNCHECKED_CAST")
        return database as? T
    }

    fun close() {
        database?.close()
        database = null
    }
}