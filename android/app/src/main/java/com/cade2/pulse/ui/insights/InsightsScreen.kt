package com.cade2.pulse.ui.insights

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cade2.pulse.R
import com.cade2.pulse.domain.model.EmotionFrequency
import com.cade2.pulse.domain.model.Pattern
import com.cade2.pulse.ui.theme.Accent
import com.cade2.pulse.ui.theme.Divider
import com.cade2.pulse.ui.theme.Surface
import com.cade2.pulse.ui.theme.TextSecondary
import com.cade2.pulse.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    onNavigateBack: () -> Unit,
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.label_insights),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.label_back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Accent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Streak card
                uiState.streak?.let { streak ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StreakStat(
                                label = stringResource(R.string.label_current_streak),
                                value = streak.currentStreak,
                                emoji = "🔥"
                            )
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(60.dp)
                                    .background(Divider)
                            )
                            StreakStat(
                                label = stringResource(R.string.label_longest_streak),
                                value = streak.longestStreak,
                                emoji = "🏆"
                            )
                        }
                    }
                }

                // Weekly emotions bar chart or unlock progress
                if (!uiState.hasEnoughData) {
                    UnlockProgressCard(sessionCount = uiState.sessionCount)
                } else {
                    // Top emotions this week
                    uiState.weeklyInsight?.let { insight ->
                        if (insight.topEmotions.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Surface),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.label_top_emotions),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    EmotionBarChart(emotions = insight.topEmotions)
                                }
                            }
                        }
                    }

                    // Pattern cards
                    if (uiState.patterns.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.label_patterns),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        uiState.patterns.forEach { pattern ->
                            PatternCard(pattern = pattern)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun StreakStat(label: String, value: Int, emoji: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$value",
            style = MaterialTheme.typography.headlineLarge,
            color = Accent,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun UnlockProgressCard(sessionCount: Int) {
    val progress = sessionCount.toFloat() / Constants.INSIGHTS_MIN_DAYS
    val accentColor = Accent

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(modifier = Modifier.size(100.dp)) {
                val stroke = 8.dp.toPx()
                val radius = size.minDimension / 2f - stroke / 2f
                drawCircle(
                    color = Divider,
                    radius = radius,
                    style = Stroke(width = stroke)
                )
                drawArc(
                    color = accentColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = stroke)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.label_days_to_unlock, sessionCount, Constants.INSIGHTS_MIN_DAYS),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${stringResource(R.string.label_insights_unlock_description)} (${Constants.INSIGHTS_MIN_DAYS} sessions)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmotionBarChart(emotions: List<EmotionFrequency>) {
    val maxPercentage = emotions.maxOfOrNull { it.percentage } ?: 1f
    val barColor = Accent
    val trackColor = Divider

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        emotions.take(5).forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.card.emoji,
                    fontSize = 18.sp,
                    modifier = Modifier.width(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.card.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(80.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    val cornerRadius = CornerRadius(4.dp.toPx())
                    drawRoundRect(
                        color = trackColor,
                        cornerRadius = cornerRadius
                    )
                    drawRoundRect(
                        color = barColor,
                        size = Size(
                            width = size.width * (item.percentage / maxPercentage),
                            height = size.height
                        ),
                        cornerRadius = cornerRadius
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${item.count}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.width(24.dp)
                )
            }
        }
    }
}

@Composable
private fun PatternCard(pattern: Pattern) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = pattern.emoji,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = pattern.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pattern.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
