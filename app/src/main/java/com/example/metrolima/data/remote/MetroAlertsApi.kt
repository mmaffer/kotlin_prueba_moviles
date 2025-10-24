package com.example.metrolima.data.remote

import com.example.metrolima.model.MetroAlert
import retrofit2.http.GET

interface MetroAlertsApi {
    @GET("alerts")
    suspend fun fetchAlerts(): AlertResponse
}

data class AlertResponse(
    val alerts: List<MetroAlert>
)
