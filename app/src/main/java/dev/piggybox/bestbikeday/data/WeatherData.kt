package dev.piggybox.bestbikeday.data

data class WeatherForecast(
    val daily: List<DayForecast>
)

data class DayForecast(
    val dt: Long,
    val temp: Temperature,
    val weather: List<Weather>
)

data class Temperature(
    val day: Double,
    val min: Double,
    val max: Double
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)