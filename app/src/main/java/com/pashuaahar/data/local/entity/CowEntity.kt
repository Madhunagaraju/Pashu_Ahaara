package com.pashuaahar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cows")
data class CowEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val breed: String,
    val ageYears: Int,
    val weightKg: Double,
    val milkYieldLitersPerDay: Double,
    val createdAtMillis: Long = System.currentTimeMillis()
)
