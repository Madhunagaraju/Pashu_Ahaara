package com.pashuaahar.presentation.screens.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pashuaahar.R
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pashuaahar.domain.model.Cow
import com.pashuaahar.presentation.components.SavingsBarChart
import com.pashuaahar.presentation.components.SavingsLineChart
import com.pashuaahar.presentation.components.SavingsPieChart

@Composable
fun RecipeScreen(viewModel: RecipeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCow = state.cows.firstOrNull { it.id == state.selectedCowId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.recipe_generator),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.recipe_tagline),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        item {
            Text(stringResource(R.string.select_cow), fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.cows, key = { it.id }) { cow ->
                    val isSelected = cow.id == state.selectedCowId
                    if (isSelected) {
                        Button(onClick = { viewModel.onEvent(RecipeUiEvent.SelectCow(cow.id)) }) {
                            Text(cow.name)
                        }
                    } else {
                        OutlinedButton(onClick = { viewModel.onEvent(RecipeUiEvent.SelectCow(cow.id)) }) {
                            Text(cow.name)
                        }
                    }
                }
            }
        }
        selectedCow?.let { cow ->
            item { SelectedCowCard(cow = cow, targetMilkYield = state.targetMilkYield) }
        }
        item {
            OutlinedTextField(
                value = state.targetMilkYield,
                onValueChange = { viewModel.onEvent(RecipeUiEvent.TargetMilkYieldChanged(it)) },
                label = { Text(stringResource(R.string.milk_yield_target)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.recipe_calc_info),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = state.marketPricePerKg,
                onValueChange = { viewModel.onEvent(RecipeUiEvent.MarketPriceChanged(it)) },
                label = { Text(stringResource(R.string.market_feed_price)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.onEvent(RecipeUiEvent.GenerateRecipe) }) { Text(stringResource(R.string.refresh_mix)) }
                Button(onClick = { viewModel.onEvent(RecipeUiEvent.SaveRecipe) }) { Text(stringResource(R.string.save_recipe)) }
            }
        }
        item {
            Text(stringResource(R.string.grain_prices), fontWeight = FontWeight.SemiBold)
        }
        items(state.grains, key = { it.id }) { grain ->
            val priceInput = state.grainPriceInputs[grain.id] ?: grain.pricePerKg.toString()
            OutlinedTextField(
                value = priceInput,
                onValueChange = { viewModel.onEvent(RecipeUiEvent.GrainPriceChanged(grain.id, it)) },
                label = { Text(stringResource(R.string.grain_price_label, grain.name)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        state.generatedRecipe?.let { recipe ->
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(recipe.recipeText, fontWeight = FontWeight.SemiBold)
                        Text(stringResource(R.string.recipe_protein, recipe.proteinPercent))
                        Text(stringResource(R.string.recipe_tdn, recipe.tdnPercent))
                        Text(stringResource(R.string.cost_per_kg, recipe.costPerKg))
                        Text(stringResource(R.string.daily_cost, recipe.dailyCost))
                        Text(
                            text = stringResource(R.string.offline_nutrition_info),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            items(recipe.ingredients) { ingredient ->
                val fodderType = fodderTypeFor(ingredient.grainName)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = fodderType.icon, contentDescription = fodderType.label)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("${ingredient.grainName}: ${"%.2f".format(ingredient.quantityKg)} kg")
                        AssistChip(onClick = {}, label = { Text(fodderType.label) })
                    }
                }
            }
        }
        state.costSummary?.let { cost ->
            item { SavingsBarChart(homemade = cost.homemadeDaily, market = cost.marketDaily) }
            item { SavingsPieChart(homemade = cost.homemadeDaily * 30.0, savings = cost.monthlySavings) }
            item { SavingsLineChart(monthlySavings = cost.monthlySavings) }
            item {
                Card {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(stringResource(R.string.homemade_per_day, cost.homemadeDaily))
                        Text(stringResource(R.string.market_per_day, cost.marketDaily))
                        Text(stringResource(R.string.monthly_savings, cost.monthlySavings))
                        Text(stringResource(R.string.yearly_savings, cost.yearlySavings))
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedCowCard(cow: Cow, targetMilkYield: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("${cow.name} (${cow.breed})", fontWeight = FontWeight.SemiBold)
            Text("Current yield: ${"%.1f".format(cow.milkYieldLitersPerDay)} L/day")
            Text("Target yield: ${if (targetMilkYield.isBlank()) "-" else targetMilkYield} L/day")
            Text("Weight: ${"%.0f".format(cow.weightKg)} kg | Age: ${cow.ageYears} years")
        }
    }
}

private fun fodderTypeFor(name: String): FodderType {
    val lower = name.lowercase()
    return when {
        lower.contains("cake") || lower.contains("meal") -> FodderType("Protein booster", Icons.Filled.Science)
        lower.contains("bran") -> FodderType("Fiber support", Icons.Filled.Eco)
        lower.contains("maize") || lower.contains("corn") -> FodderType("Energy grain", Icons.Filled.Bolt)
        else -> FodderType("General fodder", Icons.Filled.Pets)
    }
}

private data class FodderType(
    val label: String,
    val icon: ImageVector
)
