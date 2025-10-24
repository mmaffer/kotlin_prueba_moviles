package com.example.metrolima.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.metrolima.model.MetroStation
import com.example.metrolima.ui.Language
import com.example.metrolima.ui.MetroUiState

@Composable
fun RoutePlannerScreen(
    uiState: MetroUiState,
    onPlanRoute: (String, String) -> Unit,
    onClearRoute: () -> Unit
) {
    val originState = remember { mutableStateOf<MetroStation?>(null) }
    val destinationState = remember { mutableStateOf<MetroStation?>(null) }

    LaunchedEffect(uiState.routePlan) {
        uiState.routePlan?.let { plan ->
            originState.value = plan.origin
            destinationState.value = plan.destination
        }
    }

    val strings = when (uiState.language) {
        Language.SPANISH -> RouteStrings(
            title = "Planificador de rutas",
            origin = "Origen",
            destination = "Destino",
            calculate = "Calcular",
            clear = "Limpiar",
            resultTitle = "Resumen de la ruta",
            minutes = { minutos -> "Tiempo estimado: $minutos min" },
            stops = "Paradas"
        )
        Language.ENGLISH -> RouteStrings(
            title = "Route planner",
            origin = "Origin",
            destination = "Destination",
            calculate = "Calculate",
            clear = "Clear",
            resultTitle = "Route summary",
            minutes = { minutes -> "Estimated time: $minutes min" },
            stops = "Stops"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(strings.title, style = MaterialTheme.typography.headlineSmall)
        StationSelector(
            stations = uiState.stations,
            label = strings.origin,
            selectionState = originState
        )
        StationSelector(
            stations = uiState.stations,
            label = strings.destination,
            selectionState = destinationState
        )
        RowButtons(
            onCalculate = {
                val origin = originState.value
                val destination = destinationState.value
                if (origin != null && destination != null) {
                    onPlanRoute(origin.id, destination.id)
                }
            },
            onClear = {
                originState.value = null
                destinationState.value = null
                onClearRoute()
            },
            strings = strings
        )
        uiState.routePlan?.let { plan ->
            Divider()
            Text(strings.resultTitle, style = MaterialTheme.typography.titleMedium)
            Text(strings.minutes(plan.estimatedMinutes), style = MaterialTheme.typography.bodyLarge)
            Text(strings.stops, style = MaterialTheme.typography.titleSmall)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(plan.stops) { station ->
                    Text("• ${station.name}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun StationSelector(
    stations: List<MetroStation>,
    label: String,
    selectionState: MutableState<MetroStation?>
) {
    val expanded = remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Box {
            OutlinedButton(
                onClick = { expanded.value = !expanded.value },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectionState.value?.name ?: label)
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                stations.forEach { station ->
                    DropdownMenuItem(
                        text = { Text(station.name) },
                        onClick = {
                            selectionState.value = station
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowButtons(
    onCalculate: () -> Unit,
    onClear: () -> Unit,
    strings: RouteStrings
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(onClick = onCalculate, modifier = Modifier.weight(1f)) {
            Text(strings.calculate)
        }
        TextButton(onClick = onClear) {
            Text(strings.clear)
        }
    }
}

private data class RouteStrings(
    val title: String,
    val origin: String,
    val destination: String,
    val calculate: String,
    val clear: String,
    val resultTitle: String,
    val minutes: (Int) -> String,
    val stops: String
)
