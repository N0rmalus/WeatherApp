package com.example.weatherapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {
    val isMetric = repository.isMetricFlow

    fun setMeasurementSystem(isMetric: Boolean) {
        viewModelScope.launch {
            repository.saveMeasurementSystem(isMetric)
        }
    }
}