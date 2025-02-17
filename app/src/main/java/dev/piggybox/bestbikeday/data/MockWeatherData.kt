package dev.piggybox.bestbikeday.data

import java.util.Calendar
import java.util.concurrent.TimeUnit

object MockWeatherData {
    fun getMockForecast(): WeatherForecast {
        val today = Calendar.getInstance().timeInMillis / 1000 // Convert to seconds for Unix timestamp
        
        return WeatherForecast(
            daily = List(7) { dayIndex ->
                DayForecast(
                    dt = today + TimeUnit.DAYS.toSeconds(dayIndex.toLong()),
                    temp = Temperature(
                        day = 20.0 + (-2..2).random(),
                        min = 15.0 + (-2..2).random(),
                        max = 25.0 + (-2..2).random()
                    ),
                    weather = listOf(
                        Weather(
                            main = listOf("Sunny", "Cloudy", "Partly Cloudy", "Rain").random(),
                            description = "Mock weather description",
                            icon = "01d"
                        )
                    )
                )
            }
        )
    }
}