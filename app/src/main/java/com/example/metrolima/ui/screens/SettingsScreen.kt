package com.example.metrolima.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.metrolima.ui.Language
import com.example.metrolima.ui.MetroUiState

@Composable
fun SettingsScreen(
    uiState: MetroUiState,
    onToggleTheme: () -> Unit,
    onSwitchLanguage: () -> Unit
) {
    val strings = when (uiState.language) {
        Language.SPANISH -> SettingsStrings(
            title = "Configuración",
            theme = "Modo oscuro",
            language = "Idioma",
            languageValue = "Español / Inglés",
            credits = "MetroLima GO - Proyecto académico"
        )
        Language.ENGLISH -> SettingsStrings(
            title = "Settings",
            theme = "Dark mode",
            language = "Language",
            languageValue = "Spanish / English",
            credits = "MetroLima GO - Academic project"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(strings.title, style = MaterialTheme.typography.headlineSmall)
        ElevatedCard(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                RowSetting(
                    label = strings.theme,
                    action = {
                        Switch(checked = uiState.isDarkTheme, onCheckedChange = { onToggleTheme() })
                    }
                )
                RowSetting(
                    label = strings.language,
                    action = {
                        TextButton(onClick = onSwitchLanguage) {
                            Text(strings.languageValue)
                        }
                    }
                )
            }
        }
        Text(strings.credits, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun RowSetting(label: String, action: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        action()
    }
}

private data class SettingsStrings(
    val title: String,
    val theme: String,
    val language: String,
    val languageValue: String,
    val credits: String
)
