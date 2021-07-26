package com.i.o.mob.dev.weather.data.weather

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)