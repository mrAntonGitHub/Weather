package com.i.o.mob.dev.weather.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.i.o.mob.dev.weather.database.dao.CurrentWeatherDao
import com.i.o.mob.dev.weather.database.dao.DailyWeatherDao
import com.i.o.mob.dev.weather.database.dao.HourlyWeatherDao
import com.i.o.mob.dev.weather.database.entities.CurrentEntity
import com.i.o.mob.dev.weather.database.entities.DailyEntity
import com.i.o.mob.dev.weather.database.entities.HourlyEntity

@Database(entities = [CurrentEntity::class, HourlyEntity::class, DailyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun hourlyWeatherDao(): HourlyWeatherDao
    abstract fun daileWeatherDao(): DailyWeatherDao
}