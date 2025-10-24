package com.example.metrolima.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.metrolima.data.MetroRepository
import com.example.metrolima.model.MetroAlert
import com.example.metrolima.model.MetroStation
import com.example.metrolima.model.RoutePlan
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class MetroAppViewModel(private val repository: MetroRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MetroUiState())
    val uiState: StateFlow<MetroUiState> = _uiState.asStateFlow()

    init {
        observeStations()
        refreshAlerts()
    }

    private fun observeStations() {
        viewModelScope.launch {
            repository.ensureDefaultStations()
            repository.getStationsStream().collect { stations ->
                _uiState.value = _uiState.value.copy(
                    stations = stations,
                    filteredStations = applyFilters(stations, _uiState.value.searchQuery, _uiState.value.selectedLine),
                    isLoading = false
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredStations = applyFilters(_uiState.value.stations, query, _uiState.value.selectedLine)
        )
    }

    fun onLineFilterSelected(line: String?) {
        _uiState.value = _uiState.value.copy(
            selectedLine = line,
            filteredStations = applyFilters(_uiState.value.stations, _uiState.value.searchQuery, line)
        )
    }

    fun toggleFavorite(stationId: String) {
        val favorites = _uiState.value.favorites.toMutableSet()
        if (!favorites.add(stationId)) {
            favorites.remove(stationId)
        }
        _uiState.value = _uiState.value.copy(favorites = favorites)
    }

    fun toggleTheme() {
        _uiState.value = _uiState.value.copy(isDarkTheme = !_uiState.value.isDarkTheme)
    }

    fun switchLanguage() {
        val nextLanguage = when (_uiState.value.language) {
            Language.SPANISH -> Language.ENGLISH
            Language.ENGLISH -> Language.SPANISH
        }
        _uiState.value = _uiState.value.copy(language = nextLanguage)
    }

    fun planRoute(originId: String, destinationId: String) {
        val stations = _uiState.value.stations
        val origin = stations.firstOrNull { it.id == originId }
        val destination = stations.firstOrNull { it.id == destinationId }
        if (origin == null || destination == null || originId == destinationId) {
            _uiState.value = _uiState.value.copy(routePlan = null)
            return
        }
        val sameLineStations = stations.filter { it.line == origin.line }
        val sorted = sameLineStations.sortedBy { it.orderInLine }
        val originIndex = sorted.indexOfFirst { it.id == originId }
        val destinationIndex = sorted.indexOfFirst { it.id == destinationId }
        if (originIndex == -1 || destinationIndex == -1) {
            _uiState.value = _uiState.value.copy(routePlan = null)
            return
        }
        val range = if (originIndex <= destinationIndex) {
            originIndex..destinationIndex
        } else {
            destinationIndex..originIndex
        }
        val path = sorted.slice(range)
        val estimatedMinutes = (abs(destination.orderInLine - origin.orderInLine) * 4).coerceAtLeast(3)
        val plan = if (originIndex <= destinationIndex) {
            RoutePlan(origin = origin, destination = destination, estimatedMinutes = estimatedMinutes, stops = path)
        } else {
            RoutePlan(origin = origin, destination = destination, estimatedMinutes = estimatedMinutes, stops = path.reversed())
        }
        _uiState.value = _uiState.value.copy(routePlan = plan)
    }

    fun clearRoute() {
        _uiState.value = _uiState.value.copy(routePlan = null)
    }

    fun refreshAlerts() {
        viewModelScope.launch {
            runCatching { repository.fetchAlerts() }
                .onSuccess { alerts ->
                    _uiState.value = _uiState.value.copy(alerts = alerts, errorMessage = null)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(errorMessage = error.localizedMessage)
                }
        }
    }

    private fun applyFilters(
        stations: List<MetroStation>,
        query: String,
        line: String?
    ): List<MetroStation> {
        return stations.filter { station ->
            val matchesQuery = query.isBlank() || station.name.contains(query, ignoreCase = true)
            val matchesLine = line.isNullOrBlank() || station.line == line
            matchesQuery && matchesLine
        }
    }

    companion object {
        fun provideFactory(repository: MetroRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(MetroAppViewModel::class.java))
                return MetroAppViewModel(repository) as T
            }
        }
    }
}

enum class Language(val displayName: String, val locale: Locale) {
    SPANISH("Español", Locale("es")),
    ENGLISH("English", Locale.ENGLISH)
}

data class MetroUiState(
    val isLoading: Boolean = true,
    val stations: List<MetroStation> = emptyList(),
    val filteredStations: List<MetroStation> = emptyList(),
    val searchQuery: String = "",
    val selectedLine: String? = null,
    val favorites: Set<String> = emptySet(),
    val alerts: List<MetroAlert> = emptyList(),
    val routePlan: RoutePlan? = null,
    val isDarkTheme: Boolean = false,
    val language: Language = Language.SPANISH,
    val errorMessage: String? = null
)
