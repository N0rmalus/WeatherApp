// Updated LocationsScreen.kt with error alert for invalid coordinates
package com.example.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.compose.backgroundDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.weatherapp.model.WeatherInfo

@Composable
fun LocationsScreen(
    navController: NavHostController,
    locationsViewModel: LocationsViewModel
) {
    var showErrorAlert by remember { mutableStateOf(false) }

    SearchBar(
        locationsViewModel = locationsViewModel,
        onInvalidCoordinates = {
            showErrorAlert = true
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.91f)
                .padding(top = 100.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {

            Text(
                modifier = Modifier
                    .padding(top = 20.dp, start = 15.dp, end = 15.dp),
                text = "Saved Locations (${locationsViewModel.locations.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            LazyColumn(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                when (locationsViewModel.locations.isEmpty()) {
                    true -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No locations saved yet",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    false -> {
                        items(locationsViewModel.locations) { location ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            modifier = Modifier.weight(1f),
                                            fontWeight = FontWeight.Bold,
                                            text = location.observableWeatherInfo?.name ?: "Unknown",
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                        IconButton(
                                            modifier = Modifier
                                                // rounded corners
                                                .clip(MaterialTheme.shapes.medium)
                                                .background(Color.Gray.copy(alpha = 0.4f))
                                                .padding(2.dp),
                                            onClick = { locationsViewModel.deleteLocation(location) }
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(25.dp),
                                                imageVector = Icons.Rounded.Delete,
                                                contentDescription = "Delete Location",
                                            )
                                        }
                                    }
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(top = 8.dp, bottom = 8.dp),
                                        thickness = 1.dp,
                                        color = Color.Black.copy(alpha = 0.2f)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                            .size(30.dp),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleLarge,
                                        text = "${location.observableWeatherInfo?.main?.temp?.toInt() ?: "N/A"}°C",
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                            .size(20.dp),
                                        text = "${location.observableWeatherInfo?.weather?.firstOrNull()?.main ?: "Unknown"}",
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Error Alert overlay
        if (showErrorAlert) {
            ErrorAlert(onDismiss = { showErrorAlert = false })
        }
    }
}

@Composable
fun SearchBar(
    locationsViewModel: LocationsViewModel,
    onInvalidCoordinates: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        var inputCoordinates by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        BasicTextField(
            value = inputCoordinates,
            onValueChange = { inputCoordinates = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 0.dp),
            cursorBrush = SolidColor(Color.Black),
            keyboardOptions = KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                imeAction = androidx.compose.ui.text.input.ImeAction.Done
            ),
            maxLines = 1,
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(onDone = {
                if (inputCoordinates.isNotBlank()) {
                    val coords = inputCoordinates.split(",").map { it.trim() }
                    if (coords.size == 2) {
                        val latitude = coords[0].toDoubleOrNull()
                        val longitude = coords[1].toDoubleOrNull()
                        if (latitude != null && longitude != null) {
                            locationsViewModel.addLocation(latitude, longitude) { success ->
                                if (!success) {
                                    onInvalidCoordinates() // Trigger alert for invalid coordinates
                                }
                            }
                            inputCoordinates = "" // Clear input field
                        } else {
                            onInvalidCoordinates()
                        }
                    } else {
                        onInvalidCoordinates()
                    }
                }
            }),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier
                            .alpha(0.5f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier.weight(1f),
                    ) {
                        if (inputCoordinates.isEmpty()) {
                            Text(
                                text = "Enter coordinates",
                                color = Color.Gray,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        } else {
                            innerTextField()
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        onClick = { inputCoordinates = "" },
                        modifier = Modifier.alpha(0.5f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                        )
                    }
                }
            }
        )
    }
}
@Composable
fun ErrorAlert(
    onDismiss: () -> Unit
) {
    // Box in the middle of the screen with a card inside it to display the error message
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(20.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "Error",
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = "Invalid coordinates",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundDark
                    ),
                    onClick = onDismiss
                ) {
                    Text(
                        text = "Dismiss",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

// ErrorAlert preview
//@Preview
//@Composable
//fun ErrorAlertPreview() {
//    ErrorAlert(onDismiss = {})
//}