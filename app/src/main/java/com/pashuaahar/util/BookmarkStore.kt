package com.pashuaahar.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.bookmarkDataStore by preferencesDataStore(name = "tip_bookmarks")

class BookmarkStore(
    private val context: Context
) {
    private val bookmarksKey = stringSetPreferencesKey("bookmarked_tip_ids")

    val bookmarks: Flow<Set<String>> = context.bookmarkDataStore.data.map { preferences ->
        preferences[bookmarksKey] ?: emptySet()
    }

    suspend fun toggleBookmark(tipId: String) {
        context.bookmarkDataStore.edit { preferences ->
            val current = preferences[bookmarksKey]?.toMutableSet() ?: mutableSetOf()
            if (!current.add(tipId)) current.remove(tipId)
            preferences[bookmarksKey] = current
        }
    }
}
