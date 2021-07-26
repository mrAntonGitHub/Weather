package com.i.o.mob.dev.weather.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.i.o.mob.dev.weather.database.entities.CurrentEntity

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentEntity: CurrentEntity)

    @Query("SELECT * FROM current_weather")
    suspend fun getAll(): CurrentEntity
}