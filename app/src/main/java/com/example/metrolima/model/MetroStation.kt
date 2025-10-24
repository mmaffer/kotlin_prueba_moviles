package com.example.metrolima.model

data class MetroStation(
    val id: String,
    val name: String,
    val line: String,
    val district: String,
    val latitude: Double,
    val longitude: Double,
    val schedule: String,
    val orderInLine: Int
)
