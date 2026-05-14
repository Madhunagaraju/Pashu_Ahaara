package com.pashuaahar.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipes",
    foreignKeys = [
        ForeignKey(
            entity = CowEntity::class,
            parentColumns = ["id"],
            childColumns = ["cowId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cowId"])]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val cowId: Long,
    val recipeText: String,
    val ingredients: List<RecipeIngredientEntity>,
    val proteinPercent: Double,
    val tdnPercent: Double,
    val costPerKg: Double,
    val dailyCost: Double,
    val createdAtMillis: Long = System.currentTimeMillis()
)
