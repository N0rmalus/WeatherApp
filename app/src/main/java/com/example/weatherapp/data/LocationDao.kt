package com.example.weatherapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("DELETE FROM locations WHERE name = :name AND latitude = :latitude AND longitude = :longitude")
    suspend fun deleteLocation(name: String, latitude: Double, longitude: Double)
}