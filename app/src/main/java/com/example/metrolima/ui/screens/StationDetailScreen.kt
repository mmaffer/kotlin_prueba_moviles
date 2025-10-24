package com.example.metrolima.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.metrolima.model.MetroStation

@Composable
fun StationDetailScreen(
    station: MetroStation?,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (station == null) {
            Text("Estación no encontrada", style = MaterialTheme.typography.titleMedium)
            Button(onClick = onBack) {
                Text("Volver")
            }
        } else {
            Text(station.name, style = MaterialTheme.typography.headlineMedium)
            Divider()
            DetailRow(label = "Línea", value = station.line)
            DetailRow(label = "Distrito", value = station.district)
            DetailRow(label = "Horario", value = station.schedule)
            DetailRow(label = "Coordenadas", value = "${station.latitude}, ${station.longitude}")
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Regresar")
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
