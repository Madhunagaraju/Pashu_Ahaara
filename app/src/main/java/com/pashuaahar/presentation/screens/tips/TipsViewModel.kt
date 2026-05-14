package com.pashuaahar.presentation.screens.tips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashuaahar.domain.usecase.tips.GetVetTipsUseCase
import com.pashuaahar.util.BookmarkStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TipsViewModel @Inject constructor(
    getVetTipsUseCase: GetVetTipsUseCase,
    private val bookmarkStore: BookmarkStore
) : ViewModel() {

    private val allTips = getVetTipsUseCase()

    private val _uiState = MutableStateFlow(TipsUiState(tips = allTips))
    val uiState: StateFlow<TipsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            bookmarkStore.bookmarks.collect { saved ->
                _uiState.update { it.copy(bookmarks = saved) }
            }
        }
    }

    fun onEvent(event: TipsUiEvent) {
        when (event) {
            is TipsUiEvent.SearchChanged -> {
                val q = event.value
                _uiState.update {
                    it.copy(
                        query = q,
                        tips = if (q.isBlank()) {
                            allTips
                        } else {
                            allTips.filter { tip ->
                                tip.title.contains(q, ignoreCase = true) ||
                                    tip.content.contains(q, ignoreCase = true) ||
                                    tip.category.name.contains(q, ignoreCase = true)
                            }
                        }
                    )
                }
            }
            is TipsUiEvent.ToggleBookmark -> viewModelScope.launch {
                bookmarkStore.toggleBookmark(event.tipId)
            }
            is TipsUiEvent.ToggleExpand -> _uiState.update {
                val updated = it.expandedTips.toMutableSet()
                if (!updated.add(event.tipId)) updated.remove(event.tipId)
                it.copy(expandedTips = updated)
            }
        }
    }
}
