package com.i.o.mob.dev.weather.di.modules

import com.i.o.mob.dev.weather.api.OpenWeatherApi
import com.i.o.mob.dev.weather.data.weather.WeatherForecast
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import javax.inject.Singleton

@Module
class WeatherApiImpl @Inject constructor() : OpenWeatherApi {
    @Singleton
    @Provides
    override fun getWeatherPackInCity(
        lat: Double,
        lon: Double,
        unitSystem: String,
        exclude: String
    ): Deferred<WeatherForecast> {
        return OpenWeatherApi().getWeatherPackInCity(lat, lon, unitSystem)
    }
}