package com.pashuaahar.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.pashuaahar.R
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    onAddCowClick: () -> Unit,
    onNewRecipeClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val monthlySavings = state.recentRecipes.sumOf { it.dailyCost * 30.0 * 0.2 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(stringResource(R.string.namaste_raita), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Text(stringResource(R.string.tagline))
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard(stringResource(R.string.total_cows), state.cows.size.toString(), Modifier.weight(1f))
                StatCard(stringResource(R.string.monthly_savings), "Rs ${"%.0f".format(monthlySavings)}", Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onAddCowClick, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.add_cow)) }
                Button(onClick = onNewRecipeClick, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.new_recipe)) }
            }
        }
        item { Text(stringResource(R.string.recent_activity), fontWeight = FontWeight.SemiBold) }
        items(state.recentRecipes) { recipe ->
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    val cowName = state.cows.find { it.id == recipe.cowId }?.name ?: "Unknown Cow"
                    Text(stringResource(R.string.recipe_for, cowName), fontWeight = FontWeight.SemiBold)
                    Text(recipe.recipeText, style = MaterialTheme.typography.bodySmall)
                    Text(stringResource(R.string.daily_cost, recipe.dailyCost), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}
