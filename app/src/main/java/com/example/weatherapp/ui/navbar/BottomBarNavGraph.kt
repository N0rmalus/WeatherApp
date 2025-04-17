package com.example.weatherapp.ui.navbar

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.data.DatabaseModule
import com.example.weatherapp.ui.screens.ForecastScreen
import com.example.weatherapp.ui.screens.HomeScreen
import com.example.weatherapp.ui.screens.LocationsScreen
import com.example.weatherapp.ui.screens.SettingsScreen
import com.example.weatherapp.ui.screens.HomeViewModel
import com.example.weatherapp.ui.screens.LocationsViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun BottomBarNavGraph(
    navController: NavHostController,
    activity: ComponentActivity
) {
    val database = DatabaseModule.getDatabase(activity)
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            val homeViewModel = HomeViewModel(activity)
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }
        composable("forecast") { ForecastScreen(navController) }
        composable("locations") {
            val locationsViewModel = LocationsViewModel(database)
            LocationsScreen(
                navController = navController,
                locationsViewModel = locationsViewModel
            )
        }
        composable("settings") {
            SettingsScreen(
                navController = navController
            )
        }
    }
}
