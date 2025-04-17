package com.example.weatherapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.LocationEntity
import com.example.weatherapp.data.AppDatabase
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.network.WeatherApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class Location(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    var weatherInfo: WeatherInfo? = null
) {
    var observableWeatherInfo by mutableStateOf(weatherInfo)
}
class LocationsViewModel(private val database: AppDatabase) : ViewModel() {
    val locations = mutableStateListOf<Location>()

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            database.locationDao().getAllLocations().collect { locationEntities ->
                locations.clear()
                locationEntities.forEach { entity ->
                    val location = Location(
                        name = entity.name,
                        latitude = entity.latitude,
                        longitude = entity.longitude
                    )
                    locations.add(location)

                    // Fetch weather data for each location
                    fetchWeatherForLocation(location)
                }
            }
        }
    }

    fun deleteLocation(location: Location) {
        viewModelScope.launch {
            database.locationDao().deleteLocation(
                name = location.name,
                latitude = location.latitude,
                longitude = location.longitude
            )
            locations.remove(location) // Remove from the UI list
        }
    }

    private fun fetchWeatherForLocation(location: Location) {
        viewModelScope.launch {
            try {
                val weatherResponse = WeatherApi.retrofitService.getWeather(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                location.observableWeatherInfo = WeatherInfo(
                    coord = weatherResponse.coord,
                    weather = weatherResponse.weather,
                    main = weatherResponse.main,
                    wind = weatherResponse.wind,
                    name = weatherResponse.name
                )
            } catch (e: Exception) {
                location.observableWeatherInfo = WeatherInfo(
                    coord = null,
                    weather = null,
                    main = null,
                    wind = null,
                    name = "Error fetching weather"
                )
            }
        }
    }

    fun addLocation(latitude: Double, longitude: Double, onResult: (Boolean) -> Unit) {
        val location = Location(
            name = "Lat: $latitude, Lon: $longitude",
            latitude = latitude,
            longitude = longitude
        )

        viewModelScope.launch {
            try {
                // Fetch weather data for the new location
                val weatherResponse = WeatherApi.retrofitService.getWeather(
                    latitude = latitude,
                    longitude = longitude
                )
                location.observableWeatherInfo = WeatherInfo(
                    coord = weatherResponse.coord,
                    weather = weatherResponse.weather,
                    main = weatherResponse.main,
                    wind = weatherResponse.wind,
                    name = weatherResponse.name
                )

                // Save the location in the database only if weather data is valid
                database.locationDao().insertLocation(
                    LocationEntity(
                        name = location.name,
                        latitude = latitude,
                        longitude = longitude
                    )
                )
                locations.add(location) // Add to the UI list
                onResult(true) // Notify success
            } catch (e: Exception) {
                onResult(false) // Notify failure
            }
        }
    }


}
