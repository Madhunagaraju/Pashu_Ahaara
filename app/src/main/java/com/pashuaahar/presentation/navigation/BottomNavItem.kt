package com.pashuaahar.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
) {
    companion object {
        const val HOME_ROUTE = "home"
        const val COW_ROUTE = "cow"
        const val RECIPE_ROUTE = "recipe"
        const val TIPS_ROUTE = "tips"

        val items: List<BottomNavItem> = listOf(
            BottomNavItem(HOME_ROUTE, com.pashuaahar.R.string.home, Icons.Filled.Home),
            BottomNavItem(COW_ROUTE, com.pashuaahar.R.string.my_cows, Icons.Filled.Pets),
            BottomNavItem(RECIPE_ROUTE, com.pashuaahar.R.string.recipe, Icons.Filled.List),
            BottomNavItem(TIPS_ROUTE, com.pashuaahar.R.string.tips, Icons.Filled.Info)
        )
    }
}
