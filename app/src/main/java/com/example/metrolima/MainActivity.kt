package com.example.metrolima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metrolima.ui.MetroAppViewModel
import com.example.metrolima.ui.MetroLimaGoApp
import com.example.metrolima.ui.theme.MetroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val app = application as MetroApplication
            val viewModel: MetroAppViewModel = viewModel(
                factory = MetroAppViewModel.provideFactory(app.container.repository)
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MetroTheme(darkTheme = uiState.isDarkTheme) {
                MetroLimaGoApp(
                    uiState = uiState,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onLineFilterSelected = viewModel::onLineFilterSelected,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onRefreshAlerts = viewModel::refreshAlerts,
                    onToggleTheme = viewModel::toggleTheme,
                    onSwitchLanguage = viewModel::switchLanguage,
                    onPlanRoute = viewModel::planRoute,
                    onClearRoute = viewModel::clearRoute
                )
            }
        }
    }
}
