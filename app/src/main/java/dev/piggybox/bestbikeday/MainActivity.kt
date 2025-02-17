package dev.piggybox.bestbikeday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.piggybox.bestbikeday.data.DayForecast
import dev.piggybox.bestbikeday.ui.theme.BestBikeDayTheme
import dev.piggybox.bestbikeday.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BestBikeDayTheme {
                WeatherScreen()
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val forecast by viewModel.weatherForecast.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.fetchWeatherForecast(
            lat = 47.6062,
            lon = -122.3321
        )
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "7-Day Weather Forecast",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            
            forecast?.let { weatherForecast ->
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(weatherForecast.daily) { day ->
                        DayForecastCard(day)
                    }
                }
            }
        }
    }
}

@Composable
fun DayForecastCard(forecast: DayForecast) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()  // Make cards full width
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatDate(forecast.dt),
                style = MaterialTheme.typography.bodyLarge
            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${forecast.temp.day}°C",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = forecast.weather.firstOrNull()?.main ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("EEE\nMMM d", Locale.getDefault())
        .format(Date(timestamp * 1000))
}