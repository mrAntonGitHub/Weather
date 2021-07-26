package com.i.o.mob.dev.weather.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.i.o.mob.dev.weather.database.entities.DailyEntity

@Dao
interface DailyWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailyEntity: List<DailyEntity>)

    @Query("SELECT * FROM daily_weather")
    suspend fun getAll(): List<DailyEntity>
}