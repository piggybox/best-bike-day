package dev.piggybox.bestbikeday.utils

object WeatherScoring {
    // Temperature ranges (in Celsius)
    private const val IDEAL_TEMP_MIN = 15.0
    private const val IDEAL_TEMP_MAX = 25.0
    private const val MIN_ACCEPTABLE_TEMP = 5.0
    private const val MAX_ACCEPTABLE_TEMP = 30.0

    // Wind speed ranges (in m/s)
    private const val IDEAL_WIND_MAX = 5.0
    private const val MAX_ACCEPTABLE_WIND = 10.0

    // Rain probability ranges (in percentage)
    private const val MAX_IDEAL_RAIN_CHANCE = 10.0
    private const val MAX_ACCEPTABLE_RAIN_CHANCE = 30.0

    fun calculateBikeScore(
        temperature: Double,
        rainChance: Double,
        windSpeed: Double
    ): BikeScore {
        val tempScore = calculateTemperatureScore(temperature)
        val rainScore = calculateRainScore(rainChance)
        val windScore = calculateWindScore(windSpeed)
    
        // Weighted average (temperature is most important, followed by rain, then wind)
        val totalScore = (tempScore * 0.4 + rainScore * 0.4 + windScore * 0.2)
    
        val condition = when {
            totalScore >= 80 -> BikeCondition.EXCELLENT
            totalScore >= 60 -> BikeCondition.GOOD
            totalScore >= 40 -> BikeCondition.FAIR
            else -> BikeCondition.POOR
        }
    
        return BikeScore(condition, totalScore)
    }
    private fun calculateTemperatureScore(temp: Double): Double = when {
        temp in IDEAL_TEMP_MIN..IDEAL_TEMP_MAX -> 100.0
        temp < MIN_ACCEPTABLE_TEMP || temp > MAX_ACCEPTABLE_TEMP -> 0.0
        temp < IDEAL_TEMP_MIN -> {
            val range = IDEAL_TEMP_MIN - MIN_ACCEPTABLE_TEMP
            ((temp - MIN_ACCEPTABLE_TEMP) / range) * 100
        }
        else -> {
            val range = MAX_ACCEPTABLE_TEMP - IDEAL_TEMP_MAX
            ((MAX_ACCEPTABLE_TEMP - temp) / range) * 100
        }
    }

    private fun calculateRainScore(rainChance: Double): Double = when {
        rainChance <= MAX_IDEAL_RAIN_CHANCE -> 100.0
        rainChance >= MAX_ACCEPTABLE_RAIN_CHANCE -> 0.0
        else -> {
            val range = MAX_ACCEPTABLE_RAIN_CHANCE - MAX_IDEAL_RAIN_CHANCE
            ((MAX_ACCEPTABLE_RAIN_CHANCE - rainChance) / range) * 100
        }
    }

    private fun calculateWindScore(windSpeed: Double): Double = when {
        windSpeed <= IDEAL_WIND_MAX -> 100.0
        windSpeed >= MAX_ACCEPTABLE_WIND -> 0.0
        else -> {
            val range = MAX_ACCEPTABLE_WIND - IDEAL_WIND_MAX
            ((MAX_ACCEPTABLE_WIND - windSpeed) / range) * 100
        }
    }
}

enum class BikeCondition {
    EXCELLENT,
    GOOD,
    FAIR,
    POOR
}