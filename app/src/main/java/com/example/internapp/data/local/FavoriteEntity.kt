package com.example.internapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val propertyId : String,
    val savedAt : Long = System.currentTimeMillis()
)
