package com.example.metrolima.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.metrolima.model.MetroStation
import com.example.metrolima.ui.Language
import com.example.metrolima.ui.MetroUiState

@Composable
fun StationListScreen(
    uiState: MetroUiState,
    onSearchQueryChange: (String) -> Unit,
    onLineFilterSelected: (String?) -> Unit,
    onStationSelected: (MetroStation) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val lines = remember(uiState.stations) {
        listOf<String?>(null) + uiState.stations.map { it.line }.distinct()
    }
    val searchLabel = when (uiState.language) {
        Language.SPANISH -> "Buscar estación"
        Language.ENGLISH -> "Search station"
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text(searchLabel) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lines.forEach { line ->
                val isSelected = uiState.selectedLine == line
                TextButton(onClick = { onLineFilterSelected(line) }) {
                    Text(text = line ?: (if (uiState.language == Language.SPANISH) "Todas" else "All"), color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        if (uiState.filteredStations.isEmpty()) {
            Text(
                text = if (uiState.language == Language.SPANISH) "No encontramos resultados" else "No stations found",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.filteredStations, key = { it.id }) { station ->
                    StationItem(
                        station = station,
                        isFavorite = uiState.favorites.contains(station.id),
                        onClick = { onStationSelected(station) },
                        onToggleFavorite = { onToggleFavorite(station.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StationItem(
    station: MetroStation,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(station.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(station.district, style = MaterialTheme.typography.bodySmall)
                    Text(station.line, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onToggleFavorite) {
                    if (isFavorite) {
                        Icon(Icons.Filled.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    } else {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
                    }
                }
            }
            Divider()
            Text("Horario: ${station.schedule}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
