package com.i.o.mob.dev.weather.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.i.o.mob.dev.weather.database.entities.HourlyEntity

@Dao
interface HourlyWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hourly: List<HourlyEntity>)

    @Query("SELECT * FROM hourly_weather")
    suspend fun getAll(): List<HourlyEntity>
}