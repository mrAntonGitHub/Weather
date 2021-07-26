package com.i.o.mob.dev.weather.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.i.o.mob.dev.weather.data.enums.WeatherCodes
import com.i.o.mob.dev.weather.data.enums.WeatherConditionAvatars
import com.i.o.mob.dev.weather.data.news.News
import com.i.o.mob.dev.weather.data.weather.Current
import com.i.o.mob.dev.weather.data.weather.WeatherForecast
import com.i.o.mob.dev.weather.repository.Repository
import com.i.o.mob.dev.weather.utils.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

private const val MAX_NEGATIVE_TEMPERATURE = -100
private const val MAX_POSITIVE_TEMPERATURE = 100

class WeatherViewModel : ViewModel() {

    @Inject
    lateinit var repository: Repository

    private val _weather = MutableStateFlow<State<WeatherForecast>>(State.loading())
    val weather = _weather as StateFlow<State<WeatherForecast>>

    private val _news = MutableStateFlow<State<News>>(State.loading())
    val news = _news as StateFlow<State<News>>

    init {
        com.i.o.mob.dev.weather.Application.application.appComponent.inject(this)
        updateWeather()
    }

    var currentUnitSystem = repository.getCurrentUnitSystem()

    fun getCurrentCity() = repository.getCurrentCity()

    fun updateWeather() {
        viewModelScope.launch {
            repository.weatherInCity().collect {
                _weather.value = it
            }
            repository.getNews("ru").collect {
                _news.value = it
            }
        }
    }

    fun determineWeatherCode(weatherCode: Int): WeatherCodes? {
        when (weatherCode) {
            in 200 until 300 -> {
                val code = "T$weatherCode"
                return WeatherCodes.valueOf(code)
            }
            in 300 until 400 -> {
                val code = "D$weatherCode"
                return WeatherCodes.valueOf(code)
            }
            in 500..531 -> {
                val code = "R$weatherCode"
                return WeatherCodes.valueOf(code)
            }

            in 600..622 -> {
                val code = "S$weatherCode"
                return WeatherCodes.valueOf(code)
            }

            in 701..781 -> {
                val code = "R$weatherCode"
                return WeatherCodes.valueOf(code)
            }
            in 800..804 -> {
                val code = "C$weatherCode"
                return WeatherCodes.valueOf(code)
            }
            else -> {
                return null
            }
        }
    }

    fun determineWeatherCondition(currentWeather: Current): WeatherConditionAvatars? {
        when (currentWeather.temp.roundToInt()) {
            in MAX_NEGATIVE_TEMPERATURE until -10 -> {
                return WeatherConditionAvatars.FIRST
            }
            in -10 until -5 -> {
                return WeatherConditionAvatars.SECOND
            }
            in -5 until 0 -> {
                return when (currentWeather.weather[0].id) {
                    in 600..622 -> {
                        WeatherConditionAvatars.SECOND
                    }
                    615 -> {
                        WeatherConditionAvatars.THIRD
                    }
                    800 -> {
                        WeatherConditionAvatars.FOURTH
                    }
                    else -> {
                        WeatherConditionAvatars.FOURTH
                    }
                }
            }
            in 0 until 5 -> {
                return when (currentWeather.weather[0].id) {
                    615 -> {
                        WeatherConditionAvatars.THIRD
                    }
                    800 -> {
                        WeatherConditionAvatars.FIFTH
                    }
                    else -> {
                        WeatherConditionAvatars.FIFTH
                    }
                }
            }

            in 5 until 10 -> {
                return when (currentWeather.weather[0].id) {
                    in 500..531, in 310..314 -> {
                        WeatherConditionAvatars.SIXTH
                    }
                    800 -> {
                        WeatherConditionAvatars.SEVENTH
                    }
                    else -> {
                        WeatherConditionAvatars.SEVENTH
                    }
                }
            }

            in 10 until 15 -> {
                return when (currentWeather.weather[0].id) {
                    in 500..531, in 310..314 -> {
                        WeatherConditionAvatars.EIGHTH
                    }
                    800 -> {
                        WeatherConditionAvatars.NINTH
                    }
                    else -> {
                        WeatherConditionAvatars.NINTH
                    }
                }
            }
            in 15 until 20 -> {
                return when (currentWeather.weather[0].id) {
                    in 500..531, in 310..314 -> {
                        WeatherConditionAvatars.TENTH
                    }
                    800 -> {
                        WeatherConditionAvatars.ELEVENTH
                    }
                    else -> {
                        WeatherConditionAvatars.ELEVENTH
                    }
                }
            }

            in 20 until 25 -> {
                return when (currentWeather.weather[0].id) {
                    in 500..531, in 310..314 -> {
                        WeatherConditionAvatars.TWELFTH
                    }
                    800 -> {
                        WeatherConditionAvatars.THIRTEENTH
                    }
                    else -> {
                        WeatherConditionAvatars.THIRTEENTH
                    }
                }
            }

            in 25..MAX_POSITIVE_TEMPERATURE -> {
                return when (currentWeather.weather[0].id) {
                    in 500..531, in 310..314 -> {
                        WeatherConditionAvatars.FOURTEENTH
                    }
                    800 -> {
                        WeatherConditionAvatars.FIFTEENTH
                    }
                    else -> {
                        WeatherConditionAvatars.FIFTEENTH
                    }
                }
            }
            else -> {
                return null
            }
        }
    }
}