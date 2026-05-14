package com.pashuaahar.domain.model

enum class TipCategory {
    HYGIENE,
    FODDER_STORAGE,
    DISEASE_PREVENTION,
    WATER_MANAGEMENT
}

data class VetTip(
    val id: String,
    val title: String,
    val content: String,
    val category: TipCategory,
    val durationLabel: String,
    val videoUrl: String
)
