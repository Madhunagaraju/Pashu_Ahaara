package com.pashuaahar.presentation.screens.cow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pashuaahar.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pashuaahar.domain.model.Cow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CowScreen(viewModel: CowViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.cow_profile),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.step_indicator, state.currentStep, 4),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            StepperForm(state = state, onEvent = viewModel::onEvent)
        }
        item {
            Text(
                text = stringResource(R.string.saved_cows),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        items(state.cows, key = { it.id }) { cow ->
            CowItem(
                cow = cow,
                onDelete = { viewModel.onEvent(CowUiEvent.DeleteCow(cow)) },
                onEdit = { viewModel.onEvent(CowUiEvent.StartEditCow(cow)) }
            )
        }
    }
}

@Composable
private fun StepperForm(state: CowUiState, onEvent: (CowUiEvent) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(12.dp)) {
            when (state.currentStep) {
                1 -> {
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onEvent(CowUiEvent.NameChanged(it)) },
                        label = { Text(stringResource(R.string.cow_name)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BreedDropdown(
                        selectedBreed = state.breed,
                        onBreedSelected = { onEvent(CowUiEvent.BreedChanged(it)) }
                    )
                }
                2 -> {
                    OutlinedTextField(
                        value = state.age,
                        onValueChange = { onEvent(CowUiEvent.AgeChanged(it)) },
                        label = { Text(stringResource(R.string.age_years)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.weight,
                        onValueChange = { onEvent(CowUiEvent.WeightChanged(it)) },
                        label = { Text(stringResource(R.string.weight_kg)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                3 -> {
                    OutlinedTextField(
                        value = state.milkYield,
                        onValueChange = { onEvent(CowUiEvent.MilkYieldChanged(it)) },
                        label = { Text(stringResource(R.string.milk_yield_liters)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                4 -> {
                    Text(stringResource(R.string.review))
                    Text("${stringResource(R.string.cow_name)}: ${state.name}")
                    Text("${stringResource(R.string.breed)}: ${state.breed}")
                    Text("${stringResource(R.string.age_years)}: ${state.age}")
                    Text("${stringResource(R.string.weight_kg)}: ${state.weight} kg")
                    Text("${stringResource(R.string.milk_yield_liters)}: ${state.milkYield} liters/day")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { onEvent(CowUiEvent.PreviousStep) },
                    enabled = state.currentStep > 1
                ) { Text(stringResource(R.string.back)) }

                if (state.currentStep < 4) {
                    TextButton(onClick = { onEvent(CowUiEvent.NextStep) }) { Text(stringResource(R.string.next)) }
                } else {
                    TextButton(onClick = { onEvent(CowUiEvent.SaveCow) }) {
                        Text(if (state.editingCowId == null) stringResource(R.string.add_cow) else stringResource(R.string.update_cow))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BreedDropdown(selectedBreed: String, onBreedSelected: (String) -> Unit) {
    val breeds = listOf("Desi", "Jersey")
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedBreed,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.breed)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            breeds.forEach { breed ->
                DropdownMenuItem(
                    text = { Text(breed) },
                    onClick = {
                        onBreedSelected(breed)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CowItem(cow: Cow, onDelete: () -> Unit, onEdit: () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { it * 0.5f },
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = onDelete,
                    label = { Text(stringResource(R.string.delete)) },
                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                )
            }
        }
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(cow.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    AssistChip(
                        onClick = onEdit,
                        label = { Text(stringResource(R.string.edit)) },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                    )
                }
                Text("${cow.breed} • ${cow.ageYears} years")
                Text("Weight: ${cow.weightKg} kg • Milk: ${cow.milkYieldLitersPerDay} L/day")
            }
        }
    }
}
