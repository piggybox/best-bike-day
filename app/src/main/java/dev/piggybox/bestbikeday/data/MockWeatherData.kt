package dev.piggybox.bestbikeday.data

import java.util.Calendar
import java.util.Random
import java.util.concurrent.TimeUnit

object MockWeatherData {
    fun getMockForecast(): WeatherForecast {
        val today = Calendar.getInstance().timeInMillis / 1000

        return WeatherForecast(
            daily = List(7) { dayIndex ->
                val isRainy = Random().nextDouble() < 0.3 // 30% chance of rain
                DayForecast(
                    dt = today + TimeUnit.DAYS.toSeconds(dayIndex.toLong()),
                    temp = Temperature(
                        day = when(Random().nextInt(4)) {
                            0 -> 10.0 + (-2..2).random() // Cool day
                            1 -> 20.0 + (-2..2).random() // Perfect day
                            2 -> 28.0 + (-2..2).random() // Hot day
                            else -> 15.0 + (-2..2).random() // Mild day
                        },
                        min = 10.0 + (-2..2).random(),
                        max = 25.0 + (-2..2).random()
                    ),
                    weather = listOf(
                        Weather(
                            main = if (isRainy) "Rain" else listOf(
                                "Clear",
                                "Cloudy",
                                "Partly Cloudy"
                            ).random(),
                            description = "Mock weather description",
                            icon = "01d"
                        )
                    )
                )
            }
        )
    }
}