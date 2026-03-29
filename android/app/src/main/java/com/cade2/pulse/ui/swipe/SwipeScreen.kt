package com.cade2.pulse.ui.swipe

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cade2.pulse.R
import com.cade2.pulse.ui.theme.Accent
import com.cade2.pulse.ui.theme.Divider
import com.cade2.pulse.ui.theme.SwipeRight

@Composable
fun SwipeScreen(
    onNavigateToContextTag: (sessionId: String, acceptedCardIds: String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SwipeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            val sessionId = uiState.sessionId
            val acceptedIds = uiState.acceptedCardIds.joinToString(",")
            onNavigateToContextTag(sessionId, acceptedIds)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Accent
                )
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "😕",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.error ?: stringResource(R.string.error_generic),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            uiState.cards.isNotEmpty() && !uiState.isComplete -> {
                SwipeContent(
                    uiState = uiState,
                    onSwipeRight = { cardId -> viewModel.onSwipe(cardId, true) },
                    onSwipeLeft = { cardId -> viewModel.onSwipe(cardId, false) },
                    onNavigateBack = onNavigateBack
                )
            }
        }
    }
}

@Composable
private fun SwipeContent(
    uiState: SwipeUiState,
    onSwipeRight: (String) -> Unit,
    onSwipeLeft: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = tween(durationMillis = 300),
        label = "progress"
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.label_back),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = stringResource(
                    R.string.label_card_progress,
                    uiState.currentIndex,
                    uiState.totalCards
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        // Progress bar
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(4.dp),
            color = Accent,
            trackColor = Divider
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Card stack
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            val visibleCards = uiState.cards.drop(uiState.currentIndex).take(3).reversed()
            visibleCards.forEachIndexed { revIndex, card ->
                val stackIndex = visibleCards.size - 1 - revIndex
                val isTop = stackIndex == 0
                val scaleTarget = when (stackIndex) {
                    0 -> 1f
                    1 -> 0.94f
                    else -> 0.88f
                }
                val backgroundScale by animateFloatAsState(
                    targetValue = scaleTarget,
                    animationSpec = tween(durationMillis = 250),
                    label = "scale_$stackIndex"
                )
                SwipeCardView(
                    card = card,
                    onSwipeRight = { onSwipeRight(card.cardId) },
                    onSwipeLeft = { onSwipeLeft(card.cardId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp),
                    isTop = isTop,
                    backgroundScale = backgroundScale
                )
            }
        }

        // Swipe hint labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.label_not_today),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.label_yes_this_is_me),
                style = MaterialTheme.typography.bodyMedium,
                color = SwipeRight
            )
        }
    }
}
