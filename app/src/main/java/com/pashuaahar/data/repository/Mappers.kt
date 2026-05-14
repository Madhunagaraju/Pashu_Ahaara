package com.pashuaahar.data.repository

import com.pashuaahar.data.local.entity.CowEntity
import com.pashuaahar.data.local.entity.GrainEntity
import com.pashuaahar.data.local.entity.RecipeEntity
import com.pashuaahar.data.local.entity.RecipeIngredientEntity
import com.pashuaahar.domain.model.Cow
import com.pashuaahar.domain.model.Grain
import com.pashuaahar.domain.model.Recipe
import com.pashuaahar.domain.model.RecipeIngredient

fun CowEntity.toDomain(): Cow = Cow(
    id = id,
    name = name,
    breed = breed,
    ageYears = ageYears,
    weightKg = weightKg,
    milkYieldLitersPerDay = milkYieldLitersPerDay
)

fun Cow.toEntity(): CowEntity = CowEntity(
    id = id,
    name = name,
    breed = breed,
    ageYears = ageYears,
    weightKg = weightKg,
    milkYieldLitersPerDay = milkYieldLitersPerDay
)

fun GrainEntity.toDomain(): Grain = Grain(
    id = id,
    name = name,
    proteinPercent = proteinPercent,
    tdnPercent = tdnPercent,
    pricePerKg = pricePerKg
)

fun Grain.toEntity(): GrainEntity = GrainEntity(
    id = id,
    name = name,
    proteinPercent = proteinPercent,
    tdnPercent = tdnPercent,
    pricePerKg = pricePerKg
)

fun RecipeEntity.toDomain(): Recipe = Recipe(
    id = id,
    cowId = cowId,
    recipeText = recipeText,
    ingredients = ingredients.map { it.toDomain() },
    proteinPercent = proteinPercent,
    tdnPercent = tdnPercent,
    costPerKg = costPerKg,
    dailyCost = dailyCost
)

fun Recipe.toEntity(): RecipeEntity = RecipeEntity(
    id = id,
    cowId = cowId,
    recipeText = recipeText,
    ingredients = ingredients.map { it.toEntity() },
    proteinPercent = proteinPercent,
    tdnPercent = tdnPercent,
    costPerKg = costPerKg,
    dailyCost = dailyCost
)

private fun RecipeIngredientEntity.toDomain(): RecipeIngredient = RecipeIngredient(
    grainId = grainId,
    grainName = grainName,
    quantityKg = quantityKg
)

private fun RecipeIngredient.toEntity(): RecipeIngredientEntity = RecipeIngredientEntity(
    grainId = grainId,
    grainName = grainName,
    quantityKg = quantityKg
)
