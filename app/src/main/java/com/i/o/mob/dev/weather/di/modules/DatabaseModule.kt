package com.i.o.mob.dev.weather.di.modules

import android.content.Context
import androidx.room.Room
import com.i.o.mob.dev.weather.database.AppDatabase
import com.i.o.mob.dev.weather.database.dao.CurrentWeatherDao
import com.i.o.mob.dev.weather.database.dao.DailyWeatherDao
import com.i.o.mob.dev.weather.database.dao.HourlyWeatherDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val application: Context) {

    @Singleton
    @Provides
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "weatherInCity.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(appDatabase: AppDatabase): CurrentWeatherDao {
        return appDatabase.currentWeatherDao()
    }

    @Singleton
    @Provides
    fun provideHourlyWeatherDao(appDatabase: AppDatabase): HourlyWeatherDao {
        return appDatabase.hourlyWeatherDao()
    }

    @Singleton
    @Provides
    fun provideDailyWeatherDao(appDatabase: AppDatabase): DailyWeatherDao {
        return appDatabase.daileWeatherDao()
    }
}