package dev.piggybox.bestbikeday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.piggybox.bestbikeday.data.WeatherForecast
import dev.piggybox.bestbikeday.network.WeatherService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    private val _weatherForecast = MutableStateFlow<WeatherForecast?>(null)
    val weatherForecast: StateFlow<WeatherForecast?> = _weatherForecast

    private val weatherService = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherService::class.java)

    fun fetchWeatherForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val forecast = weatherService.getWeatherForecast(
                    lat = lat,
                    lon = lon,
                    apiKey = "YOUR_API_KEY" // Replace with your OpenWeatherMap API key
                )
                _weatherForecast.value = forecast
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}