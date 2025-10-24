package com.example.metrolima.model

data class MetroAlert(
    val id: String,
    val title: String,
    val description: String,
    val severity: String,
    val publishedAt: String
)
