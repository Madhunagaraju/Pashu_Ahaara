package com.pashuaahar.presentation.screens.recipe

import com.pashuaahar.domain.model.CostSummary
import com.pashuaahar.domain.model.Cow
import com.pashuaahar.domain.model.Grain
import com.pashuaahar.domain.model.Recipe

data class RecipeUiState(
    val cows: List<Cow> = emptyList(),
    val grains: List<Grain> = emptyList(),
    val grainPriceInputs: Map<String, String> = emptyMap(),
    val selectedCowId: Long? = null,
    val targetMilkYield: String = "",
    val generatedRecipe: Recipe? = null,
    val marketPricePerKg: String = "40",
    val costSummary: CostSummary? = null
)

sealed class RecipeUiEvent {
    data class SelectCow(val cowId: Long) : RecipeUiEvent()
    data object GenerateRecipe : RecipeUiEvent()
    data object SaveRecipe : RecipeUiEvent()
    data class TargetMilkYieldChanged(val value: String) : RecipeUiEvent()
    data class MarketPriceChanged(val value: String) : RecipeUiEvent()
    data class GrainPriceChanged(val grainId: String, val price: String) : RecipeUiEvent()
}
