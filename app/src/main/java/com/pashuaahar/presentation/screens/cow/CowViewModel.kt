package com.pashuaahar.presentation.screens.cow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashuaahar.domain.model.Cow
import com.pashuaahar.domain.usecase.cow.AddCowUseCase
import com.pashuaahar.domain.usecase.cow.DeleteCowUseCase
import com.pashuaahar.domain.usecase.cow.ObserveCowsUseCase
import com.pashuaahar.domain.usecase.cow.UpdateCowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CowViewModel @Inject constructor(
    private val observeCowsUseCase: ObserveCowsUseCase,
    private val addCowUseCase: AddCowUseCase,
    private val deleteCowUseCase: DeleteCowUseCase,
    private val updateCowUseCase: UpdateCowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CowUiState())
    val uiState: StateFlow<CowUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeCowsUseCase().collect { cows ->
                _uiState.update { it.copy(cows = cows) }
            }
        }
    }

    fun onEvent(event: CowUiEvent) {
        when (event) {
            is CowUiEvent.NameChanged -> _uiState.update { it.copy(name = event.value) }
            is CowUiEvent.BreedChanged -> _uiState.update { it.copy(breed = event.value) }
            is CowUiEvent.AgeChanged -> _uiState.update { it.copy(age = event.value.filter(Char::isDigit)) }
            is CowUiEvent.WeightChanged -> _uiState.update { it.copy(weight = sanitizeDecimal(event.value)) }
            is CowUiEvent.MilkYieldChanged -> _uiState.update { it.copy(milkYield = sanitizeDecimal(event.value)) }
            CowUiEvent.NextStep -> _uiState.update { it.copy(currentStep = (it.currentStep + 1).coerceAtMost(4)) }
            CowUiEvent.PreviousStep -> _uiState.update { it.copy(currentStep = (it.currentStep - 1).coerceAtLeast(1)) }
            CowUiEvent.SaveCow -> saveCow()
            is CowUiEvent.StartEditCow -> startEdit(event.cow)
            is CowUiEvent.DeleteCow -> {
                viewModelScope.launch { deleteCowUseCase(event.cow) }
            }
        }
    }

    private fun saveCow() {
        val state = uiState.value
        val ageYears = state.age.toIntOrNull() ?: return
        val weightKg = state.weight.toDoubleOrNull() ?: return
        val milkYield = state.milkYield.toDoubleOrNull() ?: return
        if (state.name.isBlank()) return

        viewModelScope.launch {
            val editingId = state.editingCowId
            if (editingId == null) {
                addCowUseCase(
                    Cow(
                        name = state.name.trim(),
                        breed = state.breed,
                        ageYears = ageYears,
                        weightKg = weightKg,
                        milkYieldLitersPerDay = milkYield
                    )
                )
            } else {
                updateCowUseCase(
                    Cow(
                        id = editingId,
                        name = state.name.trim(),
                        breed = state.breed,
                        ageYears = ageYears,
                        weightKg = weightKg,
                        milkYieldLitersPerDay = milkYield
                    )
                )
            }
            _uiState.update {
                it.copy(
                    editingCowId = null,
                    currentStep = 1,
                    name = "",
                    breed = "Desi",
                    age = "",
                    weight = "",
                    milkYield = ""
                )
            }
        }
    }

    private fun startEdit(cow: Cow) {
        _uiState.update {
            it.copy(
                editingCowId = cow.id,
                currentStep = 1,
                name = cow.name,
                breed = cow.breed,
                age = cow.ageYears.toString(),
                weight = cow.weightKg.toString(),
                milkYield = cow.milkYieldLitersPerDay.toString()
            )
        }
    }

    private fun sanitizeDecimal(input: String): String {
        val filtered = input.filter { it.isDigit() || it == '.' }
        val firstDot = filtered.indexOf('.')
        return if (firstDot == -1) filtered else {
            filtered.substring(0, firstDot + 1) + filtered.substring(firstDot + 1).replace(".", "")
        }
    }
}
