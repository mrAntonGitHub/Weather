package com.i.o.mob.dev.weather

import android.app.Application
import com.i.o.mob.dev.weather.di.component.AppComponent
import com.i.o.mob.dev.weather.di.component.DaggerAppComponent
import com.i.o.mob.dev.weather.di.modules.AppModule
import com.i.o.mob.dev.weather.di.modules.DatabaseModule

class Application : Application() {

    companion object {
        lateinit var application: com.i.o.mob.dev.weather.Application
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        application = this
        setupDagger()
    }

    private fun setupDagger() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .databaseModule(DatabaseModule(applicationContext))
            .build()
    }

}