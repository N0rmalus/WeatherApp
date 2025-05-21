package com.example.weatherapp.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.weatherapp.ui.theme.ThemeViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    themeViewModel: ThemeViewModel,
    settingsViewModel: SettingsViewModel
) {
    val themePreference by themeViewModel.themePreference.collectAsState()
    val isMetric by settingsViewModel.isMetric.collectAsState(initial = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ThemeSelection(
            themeViewModel = themeViewModel,
            themePreference = themePreference
        )

        // Measurement System Section
        MeasurementSystemSelection(
            settingsViewModel = settingsViewModel,
            isMetric = isMetric
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            thickness = 1.dp
        )

        // About Section
        AboutSection()

    }
}

@Composable
fun ThemeSelection(
    themeViewModel: ThemeViewModel,
    themePreference: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            RadioButtonGroup(
                options = listOf("System", "Light", "Dark"),
                selectedOption = themePreference,
                onOptionSelected = { theme ->
                    themeViewModel.updateThemePreference(theme.lowercase())
                }
            )
        }
    }
}

@Composable
fun MeasurementSystemSelection(
    settingsViewModel: SettingsViewModel,
    isMetric: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Measurement System",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Imperial",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Switch(
                    checked = isMetric,
                    onCheckedChange = { isChecked ->
                        settingsViewModel.setMeasurementSystem(isChecked)
                    }
                )
                Text(
                    text = "Metric",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
fun RadioButtonGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    options.forEach { option ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = selectedOption == option.lowercase(),
                onClick = { onOptionSelected(option) }
            )
            Text(
                text = option,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun AboutSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "WeatherApp",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)

            )
            Text(
                text = "Version: 1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Developed by: Rokas Kartenis",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}