package com.pashuaahar.domain.usecase.recipe

import com.pashuaahar.domain.model.Cow
import com.pashuaahar.domain.model.Grain
import com.pashuaahar.domain.model.Recipe
import com.pashuaahar.domain.model.RecipeIngredient
import javax.inject.Inject

class GenerateRecipeUseCase @Inject constructor() {

    operator fun invoke(cow: Cow, grains: List<Grain>, targetMilkYieldLitersPerDay: Double): Recipe {
        val breedProfile = breedProfileFor(cow.breed)
        val targetYield = targetMilkYieldLitersPerDay.coerceAtLeast(1.0)
        val currentYield = cow.milkYieldLitersPerDay.coerceAtLeast(0.0)
        val yieldGap = (targetYield - currentYield).coerceAtLeast(0.0)

        val proteinGrain = grains
            .filter { isProteinSource(it) }
            .maxByOrNull { it.proteinPercent / it.pricePerKg.coerceAtLeast(1.0) }
            ?: grains.maxByOrNull { it.proteinPercent / it.pricePerKg.coerceAtLeast(1.0) }
            ?: grains.first()
        val energyGrain = grains
            .filter { isEnergySource(it) }
            .maxByOrNull { it.tdnPercent / it.pricePerKg.coerceAtLeast(1.0) }
            ?: grains.maxByOrNull { it.tdnPercent / it.pricePerKg.coerceAtLeast(1.0) }
            ?: grains.first()
        val fiberGrain = grains
            .filter { isFiberSource(it) }
            .minByOrNull { it.pricePerKg }
            ?: grains.minByOrNull { it.pricePerKg }
            ?: grains.first()

        var totalMixKg = (cow.weightKg * breedProfile.maintenanceDmiFactor) +
            (targetYield * breedProfile.productionDmiFactor) +
            (yieldGap * 0.18)
        totalMixKg = totalMixKg.coerceIn(cow.weightKg * 0.025, cow.weightKg * 0.04)

        val proteinShare = when {
            targetYield >= 14.0 -> 0.30
            targetYield >= 10.0 -> 0.27
            else -> 0.24
        } + breedProfile.proteinShareBias
        val energyShare = when {
            targetYield >= 14.0 -> 0.46
            targetYield >= 10.0 -> 0.43
            else -> 0.40
        } + breedProfile.energyShareBias

        var proteinQty = totalMixKg * proteinShare
        var energyQty = totalMixKg * energyShare
        var fiberQty = (totalMixKg - proteinQty - energyQty).coerceAtLeast(totalMixKg * 0.18)

        val cpRequiredKg = (targetYield * breedProfile.cpPerLiterKg) + (cow.weightKg * breedProfile.maintenanceCpFactor)
        val tdnRequiredKg = (targetYield * breedProfile.tdnPerLiterKg) + (cow.weightKg * breedProfile.maintenanceTdnFactor)

        val initialIngredients = listOf(
            RecipeIngredient(proteinGrain.id, proteinGrain.name, proteinQty),
            RecipeIngredient(fiberGrain.id, fiberGrain.name, fiberQty),
            RecipeIngredient(energyGrain.id, energyGrain.name, energyQty)
        )

        val proteinDeficitKg = cpRequiredKg - nutrientKg(initialIngredients, grains) { it.proteinPercent }
        if (proteinDeficitKg > 0.0) {
            proteinQty += proteinDeficitKg / (proteinGrain.proteinPercent / 100.0).coerceAtLeast(0.01)
        }

        val tdnCheckIngredients = listOf(
            RecipeIngredient(proteinGrain.id, proteinGrain.name, proteinQty),
            RecipeIngredient(fiberGrain.id, fiberGrain.name, fiberQty),
            RecipeIngredient(energyGrain.id, energyGrain.name, energyQty)
        )
        val energyDeficitKg = tdnRequiredKg - nutrientKg(tdnCheckIngredients, grains) { it.tdnPercent }
        if (energyDeficitKg > 0.0) {
            energyQty += energyDeficitKg / (energyGrain.tdnPercent / 100.0).coerceAtLeast(0.01)
        }

        val ingredients = listOf(
            RecipeIngredient(proteinGrain.id, proteinGrain.name, proteinQty),
            RecipeIngredient(fiberGrain.id, fiberGrain.name, fiberQty),
            RecipeIngredient(energyGrain.id, energyGrain.name, energyQty)
        )
            .groupBy { it.grainId }
            .map { (_, values) ->
                val first = values.first()
                first.copy(quantityKg = values.sumOf { it.quantityKg })
            }
            .filter { it.quantityKg > 0.0 }

        val totalQty = ingredients.sumOf { it.quantityKg }.coerceAtLeast(1.0)
        val proteinPct = ingredients.sumOf { ingredient ->
            val grain = grains.first { it.id == ingredient.grainId }
            ingredient.quantityKg * grain.proteinPercent
        } / totalQty
        val tdnPct = ingredients.sumOf { ingredient ->
            val grain = grains.first { it.id == ingredient.grainId }
            ingredient.quantityKg * grain.tdnPercent
        } / totalQty
        val costPerKg = ingredients.sumOf { ingredient ->
            val grain = grains.first { it.id == ingredient.grainId }
            ingredient.quantityKg * grain.pricePerKg
        } / totalQty
        val dailyCost = costPerKg * totalQty

        val recipeText = ingredients.joinToString(separator = " + ") {
            "${"%.2f".format(it.quantityKg)}kg ${it.grainName}"
        }
        val targetText = "%.1f".format(targetYield)

        return Recipe(
            cowId = cow.id,
            recipeText = "For ${cow.breed} cow targeting $targetText L/day, mix $recipeText",
            ingredients = ingredients,
            proteinPercent = proteinPct,
            tdnPercent = tdnPct,
            costPerKg = costPerKg,
            dailyCost = dailyCost
        )
    }

    private fun nutrientKg(
        ingredients: List<RecipeIngredient>,
        grains: List<Grain>,
        selector: (Grain) -> Double
    ): Double = ingredients.sumOf { ingredient ->
        val grain = grains.first { it.id == ingredient.grainId }
        ingredient.quantityKg * (selector(grain) / 100.0)
    }

    private fun isProteinSource(grain: Grain): Boolean {
        val name = grain.name.lowercase()
        return name.contains("cake") || name.contains("meal")
    }

    private fun isEnergySource(grain: Grain): Boolean {
        val name = grain.name.lowercase()
        return name.contains("maize") || name.contains("corn") || grain.tdnPercent >= 75.0
    }

    private fun isFiberSource(grain: Grain): Boolean {
        val name = grain.name.lowercase()
        return name.contains("bran")
    }

    private fun breedProfileFor(breed: String): BreedProfile {
        val normalized = breed.lowercase()
        return if (normalized.contains("jersey")) {
            BreedProfile(
                maintenanceDmiFactor = 0.0205,
                productionDmiFactor = 0.31,
                cpPerLiterKg = 0.090,
                tdnPerLiterKg = 0.38,
                maintenanceCpFactor = 0.0011,
                maintenanceTdnFactor = 0.010,
                proteinShareBias = 0.02,
                energyShareBias = 0.01
            )
        } else {
            BreedProfile(
                maintenanceDmiFactor = 0.0225,
                productionDmiFactor = 0.28,
                cpPerLiterKg = 0.082,
                tdnPerLiterKg = 0.34,
                maintenanceCpFactor = 0.0010,
                maintenanceTdnFactor = 0.009,
                proteinShareBias = 0.0,
                energyShareBias = 0.0
            )
        }
    }

    private data class BreedProfile(
        val maintenanceDmiFactor: Double,
        val productionDmiFactor: Double,
        val cpPerLiterKg: Double,
        val tdnPerLiterKg: Double,
        val maintenanceCpFactor: Double,
        val maintenanceTdnFactor: Double,
        val proteinShareBias: Double,
        val energyShareBias: Double
    )
}
