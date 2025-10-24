package com.example.metrolima.model

data class RoutePlan(
    val origin: MetroStation,
    val destination: MetroStation,
    val estimatedMinutes: Int,
    val stops: List<MetroStation>
)
