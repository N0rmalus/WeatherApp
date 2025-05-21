package com.example.weatherapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weatherapp.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.WeatherInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val isMetric by settingsViewModel.isMetric.collectAsState(initial = true)

    @Suppress("UnnecessaryVariable")
    LaunchedEffect(Unit) {
        // Ensure the periodic fetching has already started
        homeViewModel.startPeriodicLocationFetching()
    }

    when (val state = homeViewModel.homeUiState) {
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Success -> ResultScreen(
            data = state.data,
            isMetric = isMetric,
        )
        is HomeUiState.Error -> ErrorScreen()
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = "Connection Error"
                )
                Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
                Text(text = error ?: "", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun ResultScreen(
    data: WeatherInfo,
    isMetric: Boolean,
) {
    LazyColumn(
        modifier = Modifier
            .height(790.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(660.dp)
                    .padding(top = 16.dp, bottom = 0.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text( // City Name
                    text = data.name ?: "Unknown City", // Display the city name or a default value
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text( // Temperature Data
                    text = if (isMetric) {
                        "${data.main?.temp?.toInt() ?: "N/A"}°C"
                    } else {
                        "${((data.main?.temp ?: 0.0) * 9 / 5 + 32).toInt()}°F"
                    },
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text( // Weather Condition
                    text = data.weather?.firstOrNull()?.main ?: "Unknown Condition", // Display the weather condition or a default value
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(0.8f),
                    thickness = 2.dp,
                    color = Color.Black.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isMetric) {
                        "${data.main?.temp_max?.toInt()}°C / ${data.main?.temp_min?.toInt()}°C " +
                        "Feels like ${data.main?.feels_like?.toInt() ?: "N/A"}°C"
                    } else {
                        "${((data.main?.temp_max ?: 0.0) * 9 / 5 + 32).toInt()}°F / ${((data.main?.temp_min ?: 0.0) * 9 / 5 + 32).toInt()}°F " +
                        "Feels like ${((data.main?.feels_like ?: 0.0) * 9 / 5 + 32).toInt() ?: "N/A"}°F"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
        item {
            AdditionalContent(
                data = data,
                isMetric = isMetric
            )
        }
    }
}

@Composable
fun AdditionalContent(
    data: WeatherInfo,
    isMetric: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        InfoCard(
            title = "Wind Speed",
            description = if (isMetric) {
                "${data.wind?.speed?.toInt() ?: "N/A"} m/s"
            } else {
                "${((data.wind?.speed ?: 0.0) * 2.23694).toInt()} mph"
            },
            imageResId = R.drawable.round_air_24
        )
        InfoCard(
            title = "Humidity",
            description = "${data.main?.humidity ?: "N/A"} %",
            imageResId = R.drawable.rounded_humidity_percentage_24
        )
    }
}

@Composable
fun InfoCard(title: String, description: String, imageResId: Int) {
    Card(
        modifier = Modifier
            .width(190.dp)
            .height(120.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.outlinedCardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 0.dp, bottom = 5.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
