package com.i.o.mob.dev.weather.api

import com.i.o.mob.dev.weather.BuildConfig
import com.i.o.mob.dev.weather.data.weather.WeatherForecast
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/"
private const val API_KEY = BuildConfig.WEATHER_API_KEY

interface OpenWeatherApi {

    @GET("/data/2.5/onecall")
    fun getWeatherPackInCity(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unitSystem: String,
        @Query("exclude") exclude: String = "minutely"
    ): Deferred<WeatherForecast>


    companion object {
        operator fun invoke(): OpenWeatherApi {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(OpenWeatherApi::class.java)
        }
    }
}