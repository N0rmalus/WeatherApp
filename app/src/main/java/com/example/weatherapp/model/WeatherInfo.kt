package com.example.weatherapp.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherInfo(
    val coord: Coord?,
    val weather: List<Weather>?,
    val main: Main?,
    val wind: Wind?,
    val name: String
)

@Serializable
data class Coord(
    val lon: Double,
    val lat: Double
)

@Serializable
data class Weather(
    val main: String,
    val description: String
)

@Serializable
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

@Serializable
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)
