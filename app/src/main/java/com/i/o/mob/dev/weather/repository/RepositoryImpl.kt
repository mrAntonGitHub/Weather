package com.i.o.mob.dev.weather.repository

import android.content.Context
import com.i.o.mob.dev.weather.data.enums.Cities
import com.i.o.mob.dev.weather.data.enums.UnitSystem
import com.i.o.mob.dev.weather.data.news.News
import com.i.o.mob.dev.weather.data.weather.Current
import com.i.o.mob.dev.weather.data.weather.Daily
import com.i.o.mob.dev.weather.data.weather.Hourly
import com.i.o.mob.dev.weather.data.weather.WeatherForecast
import com.i.o.mob.dev.weather.database.AppDatabase
import com.i.o.mob.dev.weather.database.entities.CurrentEntity
import com.i.o.mob.dev.weather.database.entities.DailyEntity
import com.i.o.mob.dev.weather.database.entities.HourlyEntity
import com.i.o.mob.dev.weather.di.modules.NewsApiImpl
import com.i.o.mob.dev.weather.di.modules.WeatherApiImpl
import com.i.o.mob.dev.weather.utils.State
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


interface Repository {

    fun getCurrentCity(): Cities

    fun getCurrentUnitSystem(): UnitSystem

    fun weatherInCity(): Flow<State<WeatherForecast>>

    fun changeCity(city: Cities)

    fun changeUnitSystem(unitSystem: UnitSystem)

    suspend fun getNews(country: String): Flow<State<News>>
}

const val APP_SHARED_PREFERENCES = "APP_SHARED_PREFERENCES"

const val CURRENT_CITY_CODE = "CURRENT_CITY_CODE"
const val CURRENT_METRIC_SYSTEM_CODE = "CURRENT_METRIC_SYSTEM_CODE"

@Singleton
class RepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApiImpl,
    private val newsApi: NewsApiImpl,
    private val appDatabase: AppDatabase,
    private val context: Context
) : Repository {

    private var currentCity = Cities.DEFAULT
    private var currentUnitSystem = UnitSystem.STANDARD

    init {
        loadCity()
        loadUnitSystem()
    }

    private fun loadCity() {
        /* load city name from shared preferences */
        val editor = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val city: String = editor.getString(CURRENT_CITY_CODE, "DEFAULT") ?: "DEFAULT"
        currentCity = Cities.valueOf(city)
    }

    private fun loadUnitSystem() {
        /* load unit system from shared preferences */
        val editor = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val unitSystem: String =
            editor.getString(CURRENT_METRIC_SYSTEM_CODE, "STANDARD") ?: "STANDARD"
        currentUnitSystem = UnitSystem.valueOf(unitSystem)
    }

    override fun getCurrentCity(): Cities = currentCity

    override fun getCurrentUnitSystem(): UnitSystem = currentUnitSystem

    override fun weatherInCity(): Flow<State<WeatherForecast>> = flow<State<WeatherForecast>> {
        emit(State.loading())
        when (val dataFromApi = getDataFromApi(currentCity, currentUnitSystem)) {
            is State.Success -> {
                insertToDatabase(dataFromApi.data)
                emit(State.success(dataFromApi.data))
            }
            is State.Error -> {
                when (val dataFromDatabase = getDataFromDatabase()) {
                    is State.Success -> {
                        emit(State.successFromDb(dataFromDatabase.data))
                    }
                    is State.Error -> {
                        emit(State.error(Exception("Can't load data"), "Ошибка загрузки данных"))
                    }
                    else -> { /* No need */
                    }
                }
            }
            else -> { /* No need */
            }
        }
    }

    override fun changeCity(city: Cities) {
        val editor =
            context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
        currentCity = city
        editor.putString(CURRENT_CITY_CODE, city.toString()).apply()
    }

    override fun changeUnitSystem(unitSystem: UnitSystem) {
        val editor =
            context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
        currentUnitSystem = unitSystem
        editor.putString(CURRENT_METRIC_SYSTEM_CODE, unitSystem.toString()).apply()
    }

    override suspend fun getNews(country: String) = flow<State<News>> {
        emit(State.loading())
        try {
            val news = newsApi.getNews(country).await()
            emit(State.success(news))
        } catch (e: Exception) {
            emit(State.error(e, "Ошибка при загрузке новостей"))
        }
    }

    private suspend fun getDataFromApi(
        city: Cities,
        unitSystem: UnitSystem
    ): State<WeatherForecast> {
        return try {
            val weather =
                weatherApi.getWeatherPackInCity(city.lat, city.lon, unitSystem.properName).await()
            State.success(weather)
        } catch (e: Exception) {
            State.error(e, "Error while sending a request. Maybe there is no Internet")
        }
    }


    private suspend fun getDataFromDatabase(): State<WeatherForecast> {
        return try {
            val weather = WeatherForecast(
                appDatabase.currentWeatherDao().getAll().toCurrent(),
                appDatabase.hourlyWeatherDao().getAll().toHourly(),
                appDatabase.daileWeatherDao().getAll().toDaily()
            )
            State.success(weather)
        } catch (e: Exception) {
            State.error(e, "Can't get data from database")
        }
    }

    private suspend fun insertToDatabase(weather: WeatherForecast) {
        withContext(IO) {
            appDatabase.currentWeatherDao().insert(weather.current.toCurrentEntity())
            appDatabase.hourlyWeatherDao().insert(weather.hourly.toHourlyEntity())
            appDatabase.daileWeatherDao().insert(weather.daily.toDailyEntity())
        }
    }

    private fun CurrentEntity.toCurrent(): Current {
        this.apply {
            return Current(
                clouds = clouds,
                dewPoint = dewPoint,
                feelsLike = feelsLike,
                dt = dt,
                humidity = humidity,
                pressure = pressure,
                sunrise = sunrise,
                sunset = sunset,
                temp = temp,
                uvi = uvi,
                visibility = visibility,
                weather = listOf(
                    weather
                ),
                windDeg = windDeg,
                windSpeed = windSpeed
            )
        }
    }

    private fun List<HourlyEntity>.toHourly(): List<Hourly> {
        val returnList = mutableListOf<Hourly>()
        forEach {
            it.apply {
                returnList.add(
                    Hourly(
                        clouds = clouds,
                        dewPoint = dewPoint,
                        feelsLike = feelsLike,
                        dt = dt,
                        humidity = humidity,
                        pressure = pressure,
                        temp = temp,
                        visibility = visibility,
                        weather = listOf(
                            weather
                        ),
                        windDeg = windDeg,
                        windSpeed = windSpeed,
                        pop = pop,
                        rain = rain
                    )
                )
            }
        }
        return returnList
    }

    private fun List<DailyEntity>.toDaily(): List<Daily> {
        val returnList = mutableListOf<Daily>()
        forEach {
            it.apply {
                returnList.add(
                    Daily(
                        clouds = clouds,
                        dewPoint = dewPoint,
                        feelsLike = feelsLike,
                        dt = dt,
                        humidity = humidity,
                        pressure = pressure,
                        sunrise = sunrise,
                        sunset = sunset,
                        temp = temp,
                        uvi = uvi,
                        weather = listOf(
                            weather
                        ),
                        windDeg = windDeg,
                        windSpeed = windSpeed,
                        pop = pop,
                        rain = rain
                    )
                )
            }
        }
        return returnList
    }

    private fun Current.toCurrentEntity(): CurrentEntity {
        this.apply {
            return CurrentEntity(
                clouds = clouds,
                dewPoint = dewPoint,
                feelsLike = feelsLike,
                dt = dt,
                humidity = humidity,
                pressure = pressure,
                sunrise = sunrise,
                sunset = sunset,
                temp = temp,
                uvi = uvi,
                visibility = visibility,
                weather = weather[0],
                windDeg = windDeg,
                windSpeed = windSpeed
            )
        }
    }

    private fun List<Hourly>.toHourlyEntity(): List<HourlyEntity> {
        val returnList = mutableListOf<HourlyEntity>()
        forEach {
            it.apply {
                returnList.add(
                    HourlyEntity(
                        clouds = clouds,
                        dewPoint = dewPoint,
                        feelsLike = feelsLike,
                        dt = dt,
                        humidity = humidity,
                        pressure = pressure,
                        temp = temp,
                        visibility = visibility,
                        weather = weather[0],
                        windDeg = windDeg,
                        windSpeed = windSpeed,
                        pop = pop,
                        rain = rain
                    )
                )
            }
        }
        return returnList
    }

    private fun List<Daily>.toDailyEntity(): List<DailyEntity> {
        val returnList = mutableListOf<DailyEntity>()
        forEach {
            it.apply {
                returnList.add(
                    DailyEntity(
                        clouds = clouds,
                        dewPoint = dewPoint,
                        feelsLike = feelsLike,
                        dt = dt,
                        humidity = humidity,
                        pressure = pressure,
                        sunrise = sunrise,
                        sunset = sunset,
                        temp = temp,
                        uvi = uvi,
                        weather = weather[0],
                        windDeg = windDeg,
                        windSpeed = windSpeed,
                        pop = pop,
                        rain = rain
                    )
                )
            }
        }
        return returnList
    }

}
