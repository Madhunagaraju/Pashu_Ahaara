package com.pashuaahar.presentation.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashuaahar.domain.usecase.cost.CalculateSavingsUseCase
import com.pashuaahar.domain.usecase.cow.ObserveCowsUseCase
import com.pashuaahar.domain.usecase.grain.ObserveGrainsUseCase
import com.pashuaahar.domain.usecase.grain.UpdateGrainUseCase
import com.pashuaahar.domain.usecase.recipe.GenerateRecipeUseCase
import com.pashuaahar.domain.usecase.recipe.SaveRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val observeCowsUseCase: ObserveCowsUseCase,
    private val observeGrainsUseCase: ObserveGrainsUseCase,
    private val updateGrainUseCase: UpdateGrainUseCase,
    private val generateRecipeUseCase: GenerateRecipeUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val calculateSavingsUseCase: CalculateSavingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private var grainsCache = emptyList<com.pashuaahar.domain.model.Grain>()

    init {
        viewModelScope.launch {
            observeCowsUseCase().collect { cows ->
                _uiState.update { state ->
                    val selectedCow = cows.firstOrNull { it.id == state.selectedCowId } ?: cows.firstOrNull()
                    val shouldResetTarget = state.selectedCowId == null || selectedCow?.id != state.selectedCowId
                    state.copy(
                        cows = cows,
                        selectedCowId = selectedCow?.id,
                        targetMilkYield = if (shouldResetTarget && selectedCow != null) {
                            formatDecimal(selectedCow.milkYieldLitersPerDay)
                        } else {
                            state.targetMilkYield
                        }
                    )
                }
                refreshRecipeIfPossible()
            }
        }
        viewModelScope.launch {
            observeGrainsUseCase().collect { grains ->
                grainsCache = grains
                _uiState.update { state ->
                    val inputs = state.grainPriceInputs.toMutableMap()
                    grains.forEach { grain ->
                        if (!inputs.containsKey(grain.id)) {
                            inputs[grain.id] = formatDecimal(grain.pricePerKg)
                        }
                    }
                    state.copy(grains = grains, grainPriceInputs = inputs)
                }
                refreshRecipeIfPossible()
            }
        }
    }

    fun onEvent(event: RecipeUiEvent) {
        when (event) {
            is RecipeUiEvent.SelectCow -> {
                val cow = uiState.value.cows.firstOrNull { it.id == event.cowId } ?: return
                _uiState.update {
                    it.copy(
                        selectedCowId = event.cowId,
                        targetMilkYield = formatDecimal(cow.milkYieldLitersPerDay)
                    )
                }
                refreshRecipeIfPossible()
            }
            is RecipeUiEvent.TargetMilkYieldChanged -> {
                _uiState.update { it.copy(targetMilkYield = sanitizeDecimal(event.value)) }
                refreshRecipeIfPossible()
            }
            is RecipeUiEvent.MarketPriceChanged -> {
                _uiState.update {
                    it.copy(marketPricePerKg = sanitizeDecimal(event.value))
                }
                refreshRecipeIfPossible()
            }
            is RecipeUiEvent.GrainPriceChanged -> updateGrainPrice(event.grainId, event.price)
            RecipeUiEvent.GenerateRecipe -> refreshRecipeIfPossible()
            RecipeUiEvent.SaveRecipe -> saveRecipe()
        }
    }

    private fun refreshRecipeIfPossible() {
        val state = uiState.value
        val cow = state.cows.firstOrNull { it.id == state.selectedCowId } ?: return
        val targetMilkYield = state.targetMilkYield.toDoubleOrNull()
        if (grainsCache.isEmpty() || targetMilkYield == null) {
            _uiState.update { it.copy(generatedRecipe = null, costSummary = null) }
            return
        }
        val recipe = generateRecipeUseCase(cow, grainsCache, targetMilkYield)
        val intake = recipe.ingredients.sumOf { it.quantityKg }
        val marketPrice = state.marketPricePerKg.toDoubleOrNull() ?: 40.0
        val summary = calculateSavingsUseCase(
            homemadeDaily = recipe.dailyCost,
            marketPricePerKg = marketPrice,
            intakeKg = intake
        )
        _uiState.update { it.copy(generatedRecipe = recipe, costSummary = summary) }
    }

    private fun saveRecipe() {
        val recipe = uiState.value.generatedRecipe ?: return
        viewModelScope.launch { saveRecipeUseCase(recipe) }
    }

    private fun updateGrainPrice(grainId: String, input: String) {
        val sanitized = sanitizeDecimal(input)
        _uiState.update { state ->
            val updatedInputs = state.grainPriceInputs.toMutableMap()
            updatedInputs[grainId] = sanitized
            state.copy(grainPriceInputs = updatedInputs)
        }

        val value = sanitized.toDoubleOrNull() ?: return
        val grain = grainsCache.firstOrNull { it.id == grainId } ?: return
        if (grain.pricePerKg == value) return

        viewModelScope.launch {
            updateGrainUseCase(grain.copy(pricePerKg = value))
        }
    }

    private fun sanitizeDecimal(input: String): String {
        val filtered = input.filter { it.isDigit() || it == '.' }
        val firstDot = filtered.indexOf('.')
        return if (firstDot == -1) filtered else {
            filtered.substring(0, firstDot + 1) + filtered.substring(firstDot + 1).replace(".", "")
        }
    }

    private fun formatDecimal(value: Double): String =
        if (value == value.toInt().toDouble()) value.toInt().toString() else "%.1f".format(value)
}
