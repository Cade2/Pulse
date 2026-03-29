package com.cade2.pulse.ui.completion

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cade2.pulse.R
import com.cade2.pulse.data.repository.SessionRepository
import com.cade2.pulse.domain.model.EmotionCard
import com.cade2.pulse.ui.theme.Accent
import com.cade2.pulse.ui.theme.Positive
import com.cade2.pulse.util.Constants
import com.cade2.pulse.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CompletionUiState(
    val acceptedEmotions: List<EmotionCard> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class CompletionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompletionUiState())
    val uiState: StateFlow<CompletionUiState> = _uiState.asStateFlow()

    init {
        loadTodayEmotions()
    }

    private fun loadTodayEmotions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = sessionRepository.getTodaySession()) {
                is Result.Success -> {
                    val emotions = result.data?.acceptedEmotions ?: emptyList()
                    _uiState.update { it.copy(isLoading = false, acceptedEmotions = emotions) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompletionScreen(
    streakCount: Int,
    onNavigateToInsights: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: CompletionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pulseScale = remember { Animatable(0f) }
    val pulseAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        pulseScale.animateTo(1f, animationSpec = tween(800, easing = LinearEasing))
    }

    LaunchedEffect(Unit) {
        repeat(3) {
            pulseAlpha.animateTo(0.6f, animationSpec = tween(600))
            pulseAlpha.animateTo(0f, animationSpec = tween(600))
        }
    }

    val accentColor = Accent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Pulse ring animation background
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopCenter)
        ) {
            val maxRadius = size.minDimension / 2f
            listOf(0.4f, 0.65f, 0.9f).forEachIndexed { i, fraction ->
                val alpha = (pulseAlpha.value * (1f - i * 0.25f)).coerceIn(0f, 1f)
                drawCircle(
                    color = accentColor.copy(alpha = alpha),
                    radius = maxRadius * fraction * pulseScale.value,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "✅", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.completion_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (streakCount in Constants.STREAK_MILESTONES) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (streakCount) {
                        3 -> stringResource(R.string.milestone_3)
                        7 -> stringResource(R.string.milestone_7)
                        14 -> stringResource(R.string.milestone_14)
                        30 -> stringResource(R.string.milestone_30)
                        100 -> stringResource(R.string.milestone_100)
                        else -> ""
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = Accent,
                    textAlign = TextAlign.Center
                )
            }

            if (streakCount > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "🔥 ${stringResource(R.string.label_day_streak, streakCount)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Accent,
                    fontWeight = FontWeight.Bold
                )
            }

            if (uiState.acceptedEmotions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.label_you_felt),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.acceptedEmotions.forEach { emotion ->
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = "${emotion.emoji} ${emotion.name}",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Positive.copy(alpha = 0.15f),
                                labelColor = Positive
                            ),
                            border = null,
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onNavigateToInsights,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(
                    text = stringResource(R.string.label_see_my_week),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = stringResource(R.string.label_back_to_home),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
