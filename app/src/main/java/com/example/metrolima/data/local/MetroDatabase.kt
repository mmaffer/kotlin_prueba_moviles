package com.example.metrolima.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MetroDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}
