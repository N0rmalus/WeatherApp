package com.example.weatherapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    private val MEASUREMENT_SYSTEM_KEY = booleanPreferencesKey("measurement_system")

    val isMetricFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[MEASUREMENT_SYSTEM_KEY] ?: true // Default to metric if not set
        }

    suspend fun saveMeasurementSystem(isMetric: Boolean) {
        dataStore.edit { preferences ->
            preferences[MEASUREMENT_SYSTEM_KEY] = isMetric
        }
    }
}