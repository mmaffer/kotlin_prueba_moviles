package com.example.metrolima.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.DirectionsTransit
import androidx.compose.material3.icons.filled.Route
import androidx.compose.material3.icons.filled.Settings
import androidx.compose.material3.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.metrolima.model.MetroAlert
import com.example.metrolima.ui.Language
import com.example.metrolima.ui.MetroUiState

@Composable
fun HomeScreen(
    uiState: MetroUiState,
    onNavigateToStations: () -> Unit,
    onNavigateToRoutes: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onRefreshAlerts: () -> Unit
) {
    val strings = when (uiState.language) {
        Language.SPANISH -> HomeStrings(
            welcome = "MetroLima GO",
            subtitle = "Planifica tus viajes de forma rápida",
            stationsTitle = "Ver estaciones",
            routesTitle = "Planificador de rutas",
            settingsTitle = "Configuración",
            alertsTitle = "Alertas del servicio",
            refresh = "Actualizar",
            emptyAlerts = "Sin alertas recientes",
            stationCount = "${uiState.stations.size} estaciones registradas"
        )
        Language.ENGLISH -> HomeStrings(
            welcome = "MetroLima GO",
            subtitle = "Plan your trips quickly",
            stationsTitle = "Stations",
            routesTitle = "Route planner",
            settingsTitle = "Settings",
            alertsTitle = "Service alerts",
            refresh = "Refresh",
            emptyAlerts = "No recent alerts",
            stationCount = "${uiState.stations.size} stations available"
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(strings.welcome, style = MaterialTheme.typography.headlineMedium)
                Text(strings.subtitle, style = MaterialTheme.typography.bodyLarge)
                ElevatedCard(colors = CardDefaults.elevatedCardColors()) {
                    Text(
                        text = strings.stationCount,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickAction(
                        icon = { Icon(Icons.Filled.DirectionsTransit, contentDescription = null) },
                        title = strings.stationsTitle,
                        onClick = onNavigateToStations
                    )
                    Divider()
                    QuickAction(
                        icon = { Icon(Icons.Filled.Route, contentDescription = null) },
                        title = strings.routesTitle,
                        onClick = onNavigateToRoutes
                    )
                    Divider()
                    QuickAction(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                        title = strings.settingsTitle,
                        onClick = onNavigateToSettings
                    )
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(strings.alertsTitle, style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onRefreshAlerts) {
                    Icon(Icons.Outlined.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(strings.refresh)
                }
            }
        }
        if (uiState.alerts.isEmpty()) {
            item {
                EmptyAlertsCard(text = strings.emptyAlerts)
            }
        } else {
            items(uiState.alerts, key = { it.id }) { alert ->
                AlertCard(alert = alert)
            }
        }
    }
}

@Composable
private fun QuickAction(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            icon()
            Text(title)
        }
    }
}

@Composable
private fun AlertCard(alert: MetroAlert) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(alert.title, style = MaterialTheme.typography.titleMedium)
            Text(alert.description, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Text(alert.publishedAt, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun EmptyAlertsCard(text: String) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

data class HomeStrings(
    val welcome: String,
    val subtitle: String,
    val stationsTitle: String,
    val routesTitle: String,
    val settingsTitle: String,
    val alertsTitle: String,
    val refresh: String,
    val emptyAlerts: String,
    val stationCount: String
)
