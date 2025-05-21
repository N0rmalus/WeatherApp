package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.navbar.BottomBarNavGraph
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.weatherapp.ui.navbar.BottomNavItem
import com.example.weatherapp.ui.theme.ThemeViewModel
import com.example.weatherapp.ui.theme.ThemeViewModelFactory
import kotlin.getValue
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    internal val themeViewModel: ThemeViewModel by viewModels {
        ThemeViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()

        setContent {
            val themePreference by themeViewModel.themePreference.collectAsState()
            WeatherAppTheme(themePreference = themePreference) {
                val navController = rememberNavController()
                BottomBarNavigationSetup(navController, this)
            }
        }
    }

    private fun requestPermissions() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
        }

        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomBarNavigationSetup(navController: NavHostController, activity: ComponentActivity) {
    val themeViewModel: ThemeViewModel = (activity as MainActivity).themeViewModel

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) {
        BottomBarNavGraph(navController, activity, themeViewModel)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem(
            title = "Home",
            route = "home",
            selectedIcon = Icons.Rounded.Home,
            unselectedIcon = Icons.Outlined.Home,
            onClick = { navController.navigate("home") }
        ),
        BottomNavItem(
            title = "Forecast",
            route = "forecast",
            selectedIcon = Icons.Rounded.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            onClick = { navController.navigate("forecast") }
        ),
        BottomNavItem(
            title = "Locations",
            route = "locations",
            selectedIcon = Icons.Rounded.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            onClick = { navController.navigate("locations") }
        ),
        BottomNavItem(
            title = "Settings",
            route = "settings",
            selectedIcon = Icons.Rounded.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            onClick = { navController.navigate("settings") }
        ),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White.copy(alpha = 0f),
    ) {
        var selected by remember { mutableIntStateOf(0) }

        bottomNavItems.forEachIndexed { index, bottomNavItem ->
            NavigationBarItem(
                selected = currentDestination == bottomNavItem.route,
                onClick = {
                    // Prevent navigating to the same destination
                    if (currentDestination != bottomNavItem.route) {
                        navController.navigate(bottomNavItem.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector =
                        if (currentDestination == bottomNavItem.route)
                            bottomNavItem.selectedIcon
                        else
                            bottomNavItem.unselectedIcon,
                        contentDescription = bottomNavItem.title,
                    )
                },
                label = {
                    Text(bottomNavItem.title)
                }
            )
        }
    }
}

