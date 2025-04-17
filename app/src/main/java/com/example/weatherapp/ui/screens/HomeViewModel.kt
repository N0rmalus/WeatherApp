package com.example.weatherapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.network.WeatherApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data class Success(val data: WeatherInfo) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(context: Context) : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationFetchJob: Job? = null
    private var lastFetchTime: Long = 0L

    init {
        startPeriodicLocationFetching() // Start fetching on ViewModel creation
    }

    fun startPeriodicLocationFetching() {
        if (locationFetchJob == null || locationFetchJob?.isActive == false) {
            locationFetchJob = viewModelScope.launch {
                while (true) { // Fetch location periodically
                    fetchLocationAndWeather()
                    delay(2 * 60 * 1000)
//                    val currentTime = System.currentTimeMillis()
//                    if (currentTime - lastFetchTime >= 2 * 60 * 1000) { // Fetch every 2 minutes
//                        lastFetchTime = currentTime
//                    }
                     // Check every 2 minutes
                }
            }
        }
    }

    fun stopFetchingLocation() {
        locationFetchJob?.cancel()
        locationFetchJob = null
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocationAndWeather() {
        Log.d("HomeViewModel", "Fetching location...")
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.d("HomeViewModel", "Location: ${location.latitude}, ${location.longitude}")
                fetchWeather(location.latitude, location.longitude)
            } else {
                Log.e("HomeViewModel", "Location is null")
                homeUiState = HomeUiState.Error
            }
        }.addOnFailureListener { exception ->
            Log.e("HomeViewModel", "Failed to fetch location", exception)
            homeUiState = HomeUiState.Error
        }
    }

    private fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            homeUiState = try {
                Log.d("HomeViewModel", "Fetching weather data...")
                val result = WeatherApi.retrofitService.getWeather(
                    latitude = latitude, longitude = longitude
                )
                Log.d("HomeViewModel", "Weather API call success")
                HomeUiState.Success(result)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Weather API call failed", e)
                HomeUiState.Error
            }
        }
    }
}
