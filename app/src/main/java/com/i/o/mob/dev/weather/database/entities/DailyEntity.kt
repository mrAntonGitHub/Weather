package com.i.o.mob.dev.weather.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.i.o.mob.dev.weather.data.weather.FeelsLike
import com.i.o.mob.dev.weather.data.weather.Temp
import com.i.o.mob.dev.weather.data.weather.Weather

@Entity(tableName = "daily_weather")
data class DailyEntity(
    @field:SerializedName("clouds")
    val clouds: Int,

    @field:SerializedName("dew_point")
    val dewPoint: Double,

    @field:SerializedName("dt")
    val dt: Int,

    @field:SerializedName("feels_like")
    @field:Embedded(prefix = "feels_like_")
    val feelsLike: FeelsLike,

    @field:SerializedName("humidity")
    val humidity: Int,

    @field:SerializedName("pop")
    val pop: Double,

    @field:SerializedName("pressure")
    val pressure: Int,

    @field:SerializedName("rain")
    val rain: Double,

    @field:SerializedName("sunrise")
    val sunrise: Int,

    @field:SerializedName("sunset")
    val sunset: Int,

    @field:SerializedName("temp")
    @field:Embedded(prefix = "temp_")
    val temp: Temp,

    @field:SerializedName("uvi")
    val uvi: Double,

    @field:SerializedName("weather")
    @field:Embedded(prefix = "weather_")
    val weather: Weather,

    @field:SerializedName("wind_deg")
    val windDeg: Int,

    @field:SerializedName("wind_speed")
    val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = true)
    var tableKey = 0
}