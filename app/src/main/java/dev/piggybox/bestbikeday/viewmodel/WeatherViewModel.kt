package dev.piggybox.bestbikeday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.piggybox.bestbikeday.data.MockWeatherData
import dev.piggybox.bestbikeday.data.WeatherForecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherForecast = MutableStateFlow<WeatherForecast?>(null)
    val weatherForecast: StateFlow<WeatherForecast?> = _weatherForecast

    fun fetchWeatherForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _weatherForecast.value = MockWeatherData.getMockForecast()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}