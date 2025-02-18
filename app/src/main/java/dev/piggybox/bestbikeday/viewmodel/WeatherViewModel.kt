package dev.piggybox.bestbikeday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.piggybox.bestbikeday.data.MockWeatherData
import dev.piggybox.bestbikeday.data.WeatherForecast
import dev.piggybox.bestbikeday.utils.BikeCondition
import dev.piggybox.bestbikeday.utils.WeatherScoring
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherForecast = MutableStateFlow<WeatherForecast?>(null)
    val weatherForecast: StateFlow<WeatherForecast?> = _weatherForecast

    private val _bikeConditions = MutableStateFlow<List<BikeCondition>>(emptyList())
    val bikeConditions: StateFlow<List<BikeCondition>> = _bikeConditions

    fun fetchWeatherForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val forecast = MockWeatherData.getMockForecast()
                _weatherForecast.value = forecast
                
                // Calculate bike conditions for each day
                _bikeConditions.value = forecast.daily.map { day ->
                    WeatherScoring.calculateBikeScore(
                        temperature = day.temp.day,
                        rainChance = 0.0, // You'll need to add this to your WeatherData classes
                        windSpeed = 0.0    // You'll need to add this to your WeatherData classes
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}