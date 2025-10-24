package com.example.metrolima.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Query("SELECT * FROM stations ORDER BY name")
    fun getStations(): Flow<List<StationEntity>>

    @Query("SELECT * FROM stations WHERE id = :id")
    suspend fun getStationById(id: String): StationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Query("DELETE FROM stations")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM stations")
    suspend fun count(): Int
}
