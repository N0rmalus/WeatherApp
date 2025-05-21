package com.example.weatherapp.ui.navbar

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.data.DatabaseModule
import com.example.weatherapp.data.SettingsRepository
import com.example.weatherapp.data.SettingsViewModelFactory
import com.example.weatherapp.ui.screens.ForecastScreen
import com.example.weatherapp.ui.screens.HomeScreen
import com.example.weatherapp.ui.screens.LocationsScreen
import com.example.weatherapp.ui.screens.SettingsScreen
import com.example.weatherapp.ui.screens.HomeViewModel
import com.example.weatherapp.ui.screens.LocationsViewModel
import com.example.weatherapp.ui.screens.SettingsViewModel
import com.example.weatherapp.ui.theme.ThemeViewModel

val Context.dataStore by preferencesDataStore("settings")

@Composable
fun BottomBarNavGraph(
    navController: NavHostController,
    activity: ComponentActivity,
    themeViewModel: ThemeViewModel
) {
    val database = DatabaseModule.getDatabase(activity)
    val settingsRepository = SettingsRepository(activity.dataStore)
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(settingsRepository)
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            val homeViewModel = HomeViewModel(activity)
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                settingsViewModel = settingsViewModel
            )
        }
        composable("forecast") {
            ForecastScreen(navController)
        }
        composable("locations") {
            val locationsViewModel = LocationsViewModel(database)
            LocationsScreen(
                navController = navController,
                locationsViewModel = locationsViewModel,
                settingsViewModel = settingsViewModel
            )
        }
        composable("settings") {
            SettingsScreen(
                navController, themeViewModel,
                settingsViewModel = settingsViewModel
            )
        }
    }
}
