package com.i.o.mob.dev.weather.data.weather

import com.google.gson.annotations.SerializedName

data class WeatherForecast(
    @SerializedName("current")
    val current: Current,

    @SerializedName("hourly")
    val hourly: List<Hourly>,

    @SerializedName("daily")
    val daily: List<Daily>,

    @SerializedName("lat")
    val lat: Double? = null,

    @SerializedName("lon")
    val lon: Double? = null,

    @SerializedName("timezone")
    val timezone: String? = null,


    @SerializedName("timezone_offset")
    val timezoneOffset: Int? = null
)