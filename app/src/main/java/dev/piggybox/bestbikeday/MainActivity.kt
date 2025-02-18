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
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import dev.piggybox.bestbikeday.data.DayForecast
import dev.piggybox.bestbikeday.ui.theme.BestBikeDayTheme
import dev.piggybox.bestbikeday.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*
import dev.piggybox.bestbikeday.utils.BikeScore
// Add this import at the top with other imports
import dev.piggybox.bestbikeday.utils.BikeCondition

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
    val bikeScores by viewModel.bikeScores.collectAsState()
    
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
            
            if (forecast != null && bikeScores.size > 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(forecast!!.daily.indices.toList()) { index ->
                        DayForecastCard(
                            forecast = forecast!!.daily[index],
                            bikeScore = bikeScores[index]
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DayForecastCard(forecast: DayForecast, bikeScore: BikeScore) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formatDate(forecast.dt),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${forecast.temp.day}°C",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = forecast.weather.firstOrNull()?.main ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Box(
                modifier = Modifier.size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(60.dp)) {
                    val strokeWidth = 4.dp.toPx()
                    val center = size.width / 2
                    val radius = (size.width - strokeWidth) / 2
                    
                    // Background circle
                    drawCircle(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        radius = radius,
                        style = Stroke(width = strokeWidth)
                    )
                    
                    // Progress arc
                    val sweepAngle = (bikeScore.score.toFloat() / 100f) * 360f
                    drawArc(
                        color = when (bikeScore.condition) {
                            BikeCondition.EXCELLENT -> Color.Green
                            BikeCondition.GOOD -> Color(0xFF8BC34A)
                            BikeCondition.FAIR -> Color(0xFFFFC107)
                            BikeCondition.POOR -> Color(0xFFF44336)
                        },
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "${bikeScore.score.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = when (bikeScore.condition) {
                        BikeCondition.EXCELLENT -> Color.Green
                        BikeCondition.GOOD -> Color(0xFF8BC34A)
                        BikeCondition.FAIR -> Color(0xFFFFC107)
                        BikeCondition.POOR -> Color(0xFFF44336)
                    }
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("EEE\nMMM d", Locale.getDefault())
        .format(Date(timestamp * 1000))
}