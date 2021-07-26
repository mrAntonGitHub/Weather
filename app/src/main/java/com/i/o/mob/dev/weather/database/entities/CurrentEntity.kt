package com.i.o.mob.dev.weather.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.i.o.mob.dev.weather.data.weather.Weather

@Entity(tableName = "current_weather")
data class CurrentEntity(
    @field:SerializedName("clouds")
    val clouds: Int,

    @field:SerializedName("dew_point")
    val dewPoint: Double,

    @field:SerializedName("dt")
    val dt: Int,

    @field:SerializedName("feels_like")
    val feelsLike: Double,

    @field:SerializedName("humidity")
    val humidity: Int,

    @field:SerializedName("pressure")
    val pressure: Int,

    @field:SerializedName("sunrise")
    val sunrise: Int,

    @field:SerializedName("sunset")
    val sunset: Int,

    @field:SerializedName("temp")
    val temp: Double,

    @field:SerializedName("uvi")
    val uvi: Double,

    @field:SerializedName("visibility")
    val visibility: Int,

    @field:Embedded(prefix = "weather_")
    val weather: Weather,

    @field:SerializedName("wind_deg")
    val windDeg: Int,

    @field:SerializedName("wind_speed")
    val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = false)
    var tableId = 0

}