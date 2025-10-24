package com.example.metrolima.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.metrolima.model.MetroStation
import com.example.metrolima.ui.screens.HomeScreen
import com.example.metrolima.ui.screens.RoutePlannerScreen
import com.example.metrolima.ui.screens.SettingsScreen
import com.example.metrolima.ui.screens.StationDetailScreen
import com.example.metrolima.ui.screens.StationListScreen

object MetroDestinations {
    const val HOME = "home"
    const val STATIONS = "stations"
    const val STATION_DETAIL = "stationDetail"
    const val ROUTE = "route"
    const val SETTINGS = "settings"
}

@Composable
fun MetroLimaGoApp(
    uiState: MetroUiState,
    onSearchQueryChange: (String) -> Unit,
    onLineFilterSelected: (String?) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onRefreshAlerts: () -> Unit,
    onToggleTheme: () -> Unit,
    onSwitchLanguage: () -> Unit,
    onPlanRoute: (String, String) -> Unit,
    onClearRoute: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination == MetroDestinations.HOME,
                    onClick = {
                        navController.navigate(MetroDestinations.HOME) {
                            launchSingleTop = true
                            popUpTo(MetroDestinations.HOME) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = currentDestination == MetroDestinations.STATIONS,
                    onClick = { navController.navigate(MetroDestinations.STATIONS) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Map, contentDescription = null) },
                    label = { Text("Estaciones") }
                )
                NavigationBarItem(
                    selected = currentDestination == MetroDestinations.SETTINGS,
                    onClick = { navController.navigate(MetroDestinations.SETTINGS) { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Ajustes") }
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = MetroDestinations.HOME,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(MetroDestinations.HOME) {
                    HomeScreen(
                        uiState = uiState,
                        onNavigateToStations = { navController.navigate(MetroDestinations.STATIONS) { launchSingleTop = true } },
                        onNavigateToRoutes = { navController.navigate(MetroDestinations.ROUTE) { launchSingleTop = true } },
                        onNavigateToSettings = { navController.navigate(MetroDestinations.SETTINGS) { launchSingleTop = true } },
                        onRefreshAlerts = onRefreshAlerts
                    )
                }
                composable(MetroDestinations.STATIONS) {
                    StationListScreen(
                        uiState = uiState,
                        onSearchQueryChange = onSearchQueryChange,
                        onLineFilterSelected = onLineFilterSelected,
                        onStationSelected = { station ->
                            navController.navigate("${MetroDestinations.STATION_DETAIL}/${station.id}")
                        },
                        onToggleFavorite = onToggleFavorite
                    )
                }
                composable(
                    route = "${MetroDestinations.STATION_DETAIL}/{stationId}",
                    arguments = listOf(navArgument("stationId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val stationId = backStackEntry.arguments?.getString("stationId")
                    val station: MetroStation? = uiState.stations.firstOrNull { it.id == stationId }
                    StationDetailScreen(station = station, onBack = { navController.navigateUp() })
                }
                composable(MetroDestinations.ROUTE) {
                    RoutePlannerScreen(
                        uiState = uiState,
                        onPlanRoute = onPlanRoute,
                        onClearRoute = onClearRoute
                    )
                }
                composable(MetroDestinations.SETTINGS) {
                    SettingsScreen(
                        uiState = uiState,
                        onToggleTheme = onToggleTheme,
                        onSwitchLanguage = onSwitchLanguage
                    )
                }
            }
        }
    }
}
