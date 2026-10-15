package com.example.internapp.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface Dao {

    @Query("SELECT propertyId FROM favorites")
    suspend fun getPropertyIds() : List<String>

}
