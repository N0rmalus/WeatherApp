package com.example.weatherapp.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(context: Context) : ViewModel() {
    private val sharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    private val _themePreference = MutableStateFlow(sharedPreferences.getString("theme_mode", "system") ?: "system")
    val themePreference: StateFlow<String> = _themePreference

    fun updateThemePreference(newPreference: String) {
        _themePreference.value = newPreference
        viewModelScope.launch {
            sharedPreferences.edit().putString("theme_mode", newPreference).apply()
        }
    }
}