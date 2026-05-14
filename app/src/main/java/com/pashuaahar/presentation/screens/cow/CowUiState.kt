package com.pashuaahar.presentation.screens.cow

import com.pashuaahar.domain.model.Cow

data class CowUiState(
    val cows: List<Cow> = emptyList(),
    val editingCowId: Long? = null,
    val currentStep: Int = 1,
    val name: String = "",
    val breed: String = "Desi",
    val age: String = "",
    val weight: String = "",
    val milkYield: String = ""
)

sealed class CowUiEvent {
    data class NameChanged(val value: String) : CowUiEvent()
    data class BreedChanged(val value: String) : CowUiEvent()
    data class AgeChanged(val value: String) : CowUiEvent()
    data class WeightChanged(val value: String) : CowUiEvent()
    data class MilkYieldChanged(val value: String) : CowUiEvent()
    data object NextStep : CowUiEvent()
    data object PreviousStep : CowUiEvent()
    data object SaveCow : CowUiEvent()
    data class StartEditCow(val cow: Cow) : CowUiEvent()
    data class DeleteCow(val cow: Cow) : CowUiEvent()
}
