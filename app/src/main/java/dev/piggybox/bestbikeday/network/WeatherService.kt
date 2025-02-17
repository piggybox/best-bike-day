package dev.piggybox.bestbikeday.network

import dev.piggybox.bestbikeday.data.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/forecast/daily")
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") days: Int = 7,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherForecast
}