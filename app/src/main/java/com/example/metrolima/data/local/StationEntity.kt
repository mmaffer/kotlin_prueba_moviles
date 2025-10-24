package com.example.metrolima.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val line: String,
    val district: String,
    val latitude: Double,
    val longitude: Double,
    val schedule: String,
    @ColumnInfo(name = "order_in_line") val orderInLine: Int
)
