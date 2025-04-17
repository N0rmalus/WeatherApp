package com.example.weatherapp.ui.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weatherapp.ui.theme.WeatherAppTheme

private const val PREFS_NAME = "user_preferences"
private const val DARK_MODE_KEY = "dark_mode"

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Load the saved preference
    val darkModeEnabled = remember { mutableStateOf(sharedPreferences.getBoolean(DARK_MODE_KEY, false)) }

    WeatherAppTheme(darkTheme = darkModeEnabled.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {
            item {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineLarge
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    thickness = 2.dp
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dark Mode")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = darkModeEnabled.value,
                        onCheckedChange = { isChecked ->
                            darkModeEnabled.value = isChecked
                            saveDarkModePreference(sharedPreferences, isChecked)
                        }
                    )
                }
            }
            item {
                var expanded by remember { mutableStateOf(false) }
                var selectedLanguage by remember { mutableStateOf("English") }

                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text(
                        text = "Language",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                expanded = true
                            }
                            // border radius
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                shape = MaterialTheme.shapes.small
                            )

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedLanguage,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                }
            }
            item {
                // buttons to save or reset settings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            // Save the settings
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Save")
                    }
                    Button(
                        onClick = {
                            // Reset the settings
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Reset")
                    }
                }
            }
            item {
                // Column for the developer information at the bottom
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Developed by: Rokas Kartenis",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Version: 1.0",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "© 2025 Weather App",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

private fun saveDarkModePreference(sharedPreferences: SharedPreferences, isDarkMode: Boolean) {
    sharedPreferences.edit().putBoolean(DARK_MODE_KEY, isDarkMode).apply()
}
