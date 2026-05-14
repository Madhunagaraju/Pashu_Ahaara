package com.pashuaahar.domain.model

data class Cow(
    val id: Long = 0L,
    val name: String,
    val breed: String,
    val ageYears: Int,
    val weightKg: Double,
    val milkYieldLitersPerDay: Double
)
