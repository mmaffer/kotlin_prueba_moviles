package com.example.metrolima

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metrolima.ui.MetroAppViewModel
import com.example.metrolima.ui.MetroLimaGoApp
import com.example.metrolima.ui.screens.NoWifiScreen
import com.example.metrolima.ui.theme.MetroTheme
import java.util.Locale
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val app = application as MetroApplication
            val viewModel: MetroAppViewModel = viewModel(
                factory = MetroAppViewModel.provideFactory(app.container.repository)
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val wifiConnectedState = remember { mutableStateOf(isWifiConnected()) }

            LaunchedEffect(uiState.language) {
                updateLocale(this@MainActivity, uiState.language.locale)
            }

            LaunchedEffect(Unit) {
                while (true) {
                    wifiConnectedState.value = isWifiConnected()
                    delay(2_000)
                }
            }

            MetroTheme(darkTheme = uiState.isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (wifiConnectedState.value) {
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
                    } else {
                        NoWifiScreen(
                            language = uiState.language,
                            onRetry = { wifiConnectedState.value = isWifiConnected() }
                        )
                    }
                }
            }
        }
    }

    private fun isWifiConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    private fun updateLocale(context: Context, locale: Locale) {
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
}
