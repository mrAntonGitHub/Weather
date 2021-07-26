package com.i.o.mob.dev.weather.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.i.o.mob.dev.weather.data.weather.Rain
import com.i.o.mob.dev.weather.data.weather.Weather

@Entity(tableName = "hourly_weather")
data class HourlyEntity(
    @SerializedName("clouds")
    @field:ColumnInfo(name = "clouds")
    val clouds: Int,

    @SerializedName("dew_point")
    @field:ColumnInfo(name = "dewPoint")
    val dewPoint: Double,

    @SerializedName("dt")
    @field:ColumnInfo(name = "dt")
    val dt: Int,

    @SerializedName("feels_like")
    @field:ColumnInfo(name = "feels_like")
    val feelsLike: Double,

    @SerializedName("humidity")
    @field:ColumnInfo(name = "humidity")
    val humidity: Int,

    @SerializedName("pop")
    @field:ColumnInfo(name = "pop")
    val pop: Double,

    @SerializedName("pressure")
    @field:ColumnInfo(name = "pressure")
    val pressure: Int,

    @SerializedName("rain")
    @field:Embedded(prefix = "rain_")
    val rain: Rain?,

    @SerializedName("temp")
    @field:ColumnInfo(name = "temp")
    val temp: Double,

    @SerializedName("visibility")
    @field:ColumnInfo(name = "visibility")
    val visibility: Int,

    @SerializedName("weather")
    @field:Embedded(prefix = "weather_")
    val weather: Weather,

    @SerializedName("wind_deg")
    @field:ColumnInfo(name = "wind_deg")
    val windDeg: Int,

    @SerializedName("wind_speed")
    @field:ColumnInfo(name = "wind_speed")
    val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = true)
    var tableId = 0
}