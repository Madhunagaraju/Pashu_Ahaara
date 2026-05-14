package com.pashuaahar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grains")
data class GrainEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val proteinPercent: Double,
    val tdnPercent: Double,
    val pricePerKg: Double,
    val isDefault: Boolean = true
)
