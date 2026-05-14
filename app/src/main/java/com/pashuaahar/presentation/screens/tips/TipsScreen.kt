package com.pashuaahar.presentation.screens.tips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import com.pashuaahar.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TipsScreen(viewModel: TipsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(stringResource(R.string.vet_tips_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.onEvent(TipsUiEvent.SearchChanged(it)) },
                label = { Text(stringResource(R.string.search_tips)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(state.tips, key = { it.id }) { tip ->
            val expanded = state.expandedTips.contains(tip.id)
            val bookmarked = state.bookmarks.contains(tip.id)
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(tip.title, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = tip.category.name.lowercase().replaceFirstChar { it.uppercase() }.replace("_", " "),
                                style = MaterialTheme.typography.bodySmall
                            )
                            AssistChip(onClick = {}, label = { Text(tip.durationLabel) })
                        }
                        Row {
                            IconButton(onClick = { viewModel.onEvent(TipsUiEvent.ToggleBookmark(tip.id)) }) {
                                Icon(
                                    imageVector = if (bookmarked) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = if (bookmarked) stringResource(R.string.remove_bookmark) else stringResource(R.string.add_bookmark)
                                )
                            }
                            IconButton(onClick = { viewModel.onEvent(TipsUiEvent.ToggleExpand(tip.id)) }) {
                                Icon(Icons.Filled.ExpandMore, contentDescription = stringResource(R.string.expand))
                            }
                        }
                    }
                    if (expanded) {
                        Text(tip.content, modifier = Modifier.padding(top = 8.dp))
                        Text(
                            text = stringResource(R.string.offline_video_info),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        if (tip.videoUrl.isNotBlank()) {
                            TextButton(
                                onClick = { uriHandler.openUri(tip.videoUrl) },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircleOutline,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(stringResource(R.string.open_reference_video))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
