package com.i.o.mob.dev.weather.di.component

import com.i.o.mob.dev.weather.di.modules.*
import com.i.o.mob.dev.weather.repository.RepositoryImpl
import com.i.o.mob.dev.weather.ui.chooseCity.ChooseCityViewModel
import com.i.o.mob.dev.weather.ui.home.WeatherViewModel
import com.i.o.mob.dev.weather.ui.splash.SplashViewModel
import com.i.o.mob.dev.weather.utils.Utils
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, NewsApiImpl::class, RepositoryModule::class, WeatherApiImpl::class])
interface AppComponent {
    fun inject(repositoryImpl: RepositoryImpl)
    fun inject(weatherViewModel: WeatherViewModel)
    fun inject(chooseCityViewModel: ChooseCityViewModel)
    fun inject(splashViewModel: SplashViewModel)
    fun inject(utils: Utils)
}