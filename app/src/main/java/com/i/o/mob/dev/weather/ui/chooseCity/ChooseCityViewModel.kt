package com.i.o.mob.dev.weather.ui.chooseCity

import androidx.lifecycle.ViewModel
import com.i.o.mob.dev.weather.Application
import com.i.o.mob.dev.weather.data.enums.Cities
import com.i.o.mob.dev.weather.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

class ChooseCityViewModel : ViewModel() {

    @Inject
    lateinit var repository: Repository

    private val _cities: MutableStateFlow<List<Cities>> = MutableStateFlow(listOf())
    val cities = _cities as StateFlow<List<Cities>>

    private val citiesList = Cities.values().toList()

    init {
        Application.application.appComponent.inject(this)
        _cities.value = citiesList
    }

    fun search(query: CharSequence) {
        val result = citiesList.filter {
            it.city.toLowerCase(Locale.ROOT).contains(query)
        }
        _cities.value = result
    }

    fun changeCity(city: Cities) {
        repository.changeCity(city)
    }

}