package com.pashuaahar.domain.model

data class Recipe(
    val id: Long = 0L,
    val cowId: Long,
    val recipeText: String,
    val ingredients: List<RecipeIngredient>,
    val proteinPercent: Double,
    val tdnPercent: Double,
    val costPerKg: Double,
    val dailyCost: Double
)

data class RecipeIngredient(
    val grainId: String,
    val grainName: String,
    val quantityKg: Double
)
