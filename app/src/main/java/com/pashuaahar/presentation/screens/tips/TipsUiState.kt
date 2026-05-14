package com.pashuaahar.presentation.screens.tips

import com.pashuaahar.domain.model.VetTip

data class TipsUiState(
    val query: String = "",
    val tips: List<VetTip> = emptyList(),
    val bookmarks: Set<String> = emptySet(),
    val expandedTips: Set<String> = emptySet()
)

sealed class TipsUiEvent {
    data class SearchChanged(val value: String) : TipsUiEvent()
    data class ToggleBookmark(val tipId: String) : TipsUiEvent()
    data class ToggleExpand(val tipId: String) : TipsUiEvent()
}
