package com.example.metrolima.data

import com.example.metrolima.data.local.StationDao
import com.example.metrolima.data.local.StationEntity
import com.example.metrolima.data.remote.MetroAlertsApi
import com.example.metrolima.model.MetroAlert
import com.example.metrolima.model.MetroStation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface MetroRepository {
    fun getStationsStream(): Flow<List<MetroStation>>
    suspend fun getStationById(id: String): MetroStation?
    suspend fun ensureDefaultStations()
    suspend fun fetchAlerts(): List<MetroAlert>
}

class MetroRepositoryImpl(
    private val stationDao: StationDao,
    private val alertsApi: MetroAlertsApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MetroRepository {

    override fun getStationsStream(): Flow<List<MetroStation>> =
        stationDao.getStations().map { entities ->
            entities.map { it.toModel() }
        }

    override suspend fun getStationById(id: String): MetroStation? = withContext(ioDispatcher) {
        stationDao.getStationById(id)?.toModel()
    }

    override suspend fun ensureDefaultStations() = withContext(ioDispatcher) {
        if (stationDao.count() == 0) {
            stationDao.insertStations(DEFAULT_STATIONS.map { it.toEntity() })
        }
    }

    override suspend fun fetchAlerts(): List<MetroAlert> = withContext(ioDispatcher) {
        alertsApi.fetchAlerts().alerts
    }

    private fun StationEntity.toModel(): MetroStation =
        MetroStation(
            id = id,
            name = name,
            line = line,
            district = district,
            latitude = latitude,
            longitude = longitude,
            schedule = schedule,
            orderInLine = orderInLine
        )

    private fun MetroStation.toEntity(): StationEntity =
        StationEntity(
            id = id,
            name = name,
            line = line,
            district = district,
            latitude = latitude,
            longitude = longitude,
            schedule = schedule,
            orderInLine = orderInLine
        )

    companion object {
        private val DEFAULT_STATIONS = listOf(
            MetroStation(
                id = "ves",
                name = "Villa El Salvador",
                line = "Línea 1",
                district = "Villa El Salvador",
                latitude = -12.190836,
                longitude = -76.996314,
                schedule = "05:30 - 23:00",
                orderInLine = 0
            ),
            MetroStation(
                id = "mat",
                name = "Matazango",
                line = "Línea 1",
                district = "Villa María del Triunfo",
                latitude = -12.17052,
                longitude = -76.98594,
                schedule = "05:30 - 23:00",
                orderInLine = 1
            ),
            MetroStation(
                id = "atocongo",
                name = "Atocongo",
                line = "Línea 1",
                district = "San Juan de Miraflores",
                latitude = -12.150255,
                longitude = -76.98971,
                schedule = "05:30 - 23:00",
                orderInLine = 2
            ),
            MetroStation(
                id = "sanjuan",
                name = "San Juan",
                line = "Línea 1",
                district = "San Juan de Miraflores",
                latitude = -12.13683,
                longitude = -76.98745,
                schedule = "05:30 - 23:00",
                orderInLine = 3
            ),
            MetroStation(
                id = "angamos",
                name = "Angamos",
                line = "Línea 1",
                district = "Surquillo",
                latitude = -12.11813,
                longitude = -76.98924,
                schedule = "05:30 - 23:00",
                orderInLine = 4
            ),
            MetroStation(
                id = "cabitos",
                name = "Cabitos",
                line = "Línea 1",
                district = "Miraflores",
                latitude = -12.11012,
                longitude = -76.99213,
                schedule = "05:30 - 23:00",
                orderInLine = 5
            ),
            MetroStation(
                id = "gamarra",
                name = "Gamarra",
                line = "Línea 1",
                district = "La Victoria",
                latitude = -12.07363,
                longitude = -77.01011,
                schedule = "05:30 - 23:00",
                orderInLine = 6
            ),
            MetroStation(
                id = "plazar",
                name = "Plaza de Flores",
                line = "Línea 1",
                district = "Lima",
                latitude = -12.06091,
                longitude = -77.01356,
                schedule = "05:30 - 23:00",
                orderInLine = 7
            ),
            MetroStation(
                id = "tarata",
                name = "Ayacucho",
                line = "Línea 1",
                district = "Lima",
                latitude = -12.0521,
                longitude = -77.0212,
                schedule = "05:30 - 23:00",
                orderInLine = 8
            ),
            MetroStation(
                id = "bayovar",
                name = "Bayóvar",
                line = "Línea 1",
                district = "San Juan de Lurigancho",
                latitude = -12.0151,
                longitude = -76.9973,
                schedule = "05:30 - 23:00",
                orderInLine = 9
            )
        )
    }
}
