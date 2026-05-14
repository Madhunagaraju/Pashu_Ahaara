package com.pashuaahar.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pashuaahar.presentation.screens.cow.CowScreen
import com.pashuaahar.presentation.screens.home.HomeScreen
import com.pashuaahar.presentation.screens.recipe.RecipeScreen
import com.pashuaahar.presentation.screens.tips.TipsScreen

@Composable
fun PashuAaharNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.HOME_ROUTE,
        modifier = modifier
    ) {
        composable(BottomNavItem.HOME_ROUTE) {
            HomeScreen(
                onAddCowClick = { navController.navigate(BottomNavItem.COW_ROUTE) },
                onNewRecipeClick = { navController.navigate(BottomNavItem.RECIPE_ROUTE) }
            )
        }
        composable(BottomNavItem.COW_ROUTE) { CowScreen() }
        composable(BottomNavItem.RECIPE_ROUTE) { RecipeScreen() }
        composable(BottomNavItem.TIPS_ROUTE) { TipsScreen() }
    }
}
