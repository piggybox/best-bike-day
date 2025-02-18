package dev.piggybox.bestbikeday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.piggybox.bestbikeday.data.MockWeatherData
import dev.piggybox.bestbikeday.data.WeatherForecast
import dev.piggybox.bestbikeday.utils.BikeCondition
import dev.piggybox.bestbikeday.utils.BikeScore
import dev.piggybox.bestbikeday.utils.WeatherScoring
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherForecast = MutableStateFlow<WeatherForecast?>(null)
    val weatherForecast: StateFlow<WeatherForecast?> = _weatherForecast

    private val _bikeScores = MutableStateFlow<List<BikeScore>>(emptyList())
    val bikeScores: StateFlow<List<BikeScore>> = _bikeScores

    fun fetchWeatherForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val forecast = MockWeatherData.getMockForecast()
                _weatherForecast.value = forecast
                
                // Calculate bike scores for each day
                _bikeScores.value = forecast.daily.map { day ->
                    WeatherScoring.calculateBikeScore(
                        temperature = day.temp.day,
                        rainChance = if (day.weather.firstOrNull()?.main == "Rain") 80.0 else 10.0,
                        windSpeed = (2..12).random().toDouble() // Random wind speed between 2-12 m/s
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}