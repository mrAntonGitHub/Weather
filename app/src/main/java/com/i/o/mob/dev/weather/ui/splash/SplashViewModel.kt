package com.i.o.mob.dev.weather.ui.splash

import androidx.lifecycle.ViewModel
import com.i.o.mob.dev.weather.Application
import com.i.o.mob.dev.weather.data.enums.Cities
import com.i.o.mob.dev.weather.repository.Repository
import javax.inject.Inject

class SplashViewModel : ViewModel() {

    @Inject
    lateinit var repository: Repository

    init {
        Application.application.appComponent.inject(this)
    }

    fun getCurrentCity(): Cities {
        return repository.getCurrentCity()
    }

}