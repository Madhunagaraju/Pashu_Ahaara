package com.pashuaahar.data.local.database

import com.pashuaahar.data.local.entity.GrainEntity

object DefaultGrains {
    val items: List<GrainEntity> = listOf(
        GrainEntity(
            id = "maize",
            name = "Maize",
            proteinPercent = 9.0,
            tdnPercent = 80.0,
            pricePerKg = 25.0
        ),
        GrainEntity(
            id = "cottonseed_cake",
            name = "Cottonseed Cake",
            proteinPercent = 23.0,
            tdnPercent = 75.0,
            pricePerKg = 35.0
        ),
        GrainEntity(
            id = "mustard_cake",
            name = "Mustard Cake",
            proteinPercent = 38.0,
            tdnPercent = 70.0,
            pricePerKg = 40.0
        ),
        GrainEntity(
            id = "wheat_bran",
            name = "Wheat Bran",
            proteinPercent = 15.0,
            tdnPercent = 65.0,
            pricePerKg = 20.0
        ),
        GrainEntity(
            id = "rice_bran",
            name = "Rice Bran",
            proteinPercent = 12.0,
            tdnPercent = 70.0,
            pricePerKg = 18.0
        ),
        GrainEntity(
            id = "soybean_meal",
            name = "Soybean Meal",
            proteinPercent = 45.0,
            tdnPercent = 85.0,
            pricePerKg = 55.0
        )
    )
}
