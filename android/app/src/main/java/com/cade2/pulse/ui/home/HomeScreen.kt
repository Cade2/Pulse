package com.cade2.pulse.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cade2.pulse.R
import com.cade2.pulse.ui.theme.Accent
import com.cade2.pulse.ui.theme.Divider
import com.cade2.pulse.ui.theme.Positive
import com.cade2.pulse.ui.theme.Surface
import java.util.Calendar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onNavigateToSwipe: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            PulseBottomNav(
                onHome = {},
                onHistory = onNavigateToHistory,
                onInsights = onNavigateToInsights,
                onProfile = onNavigateToProfile,
                currentRoute = "home"
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Accent
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    // Greeting
                    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                        in 5..11 -> stringResource(R.string.greeting_morning)
                        in 12..17 -> stringResource(R.string.greeting_afternoon)
                        else -> stringResource(R.string.greeting_evening)
                    }
                    Text(
                        text = "$greeting${if (uiState.userName.isNotBlank()) ", ${uiState.userName}" else ""}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    if (uiState.hasCompletedToday) {
                        // Session completed state
                        if (uiState.streakCount > 0) {
                            StreakBadge(streak = uiState.streakCount)
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        Text(
                            text = stringResource(R.string.label_you_felt_today),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.acceptedEmotions.forEach { emotion ->
                                SuggestionChip(
                                    onClick = {},
                                    label = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = emotion.emoji, fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = emotion.name,
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
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
                    } else {
                        // No session yet — CTA card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "💜", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.label_how_are_you_feeling),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.label_cta_description),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(
                                    onClick = onNavigateToSwipe,
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
                                        text = stringResource(R.string.label_check_pulse),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StreakBadge(streak: Int) {
    Row(
        modifier = Modifier
            .background(
                color = Accent.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "🔥", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.label_day_streak, streak),
            style = MaterialTheme.typography.labelLarge,
            color = Accent,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PulseBottomNav(
    onHome: () -> Unit,
    onHistory: () -> Unit,
    onInsights: () -> Unit,
    onProfile: () -> Unit,
    currentRoute: String
) {
    NavigationBar(
        containerColor = Surface,
        tonalElevation = 0.dp
    ) {
        BottomNavItem(
            icon = Icons.Default.Home,
            label = stringResource(R.string.label_home),
            selected = currentRoute == "home",
            onClick = onHome
        )
        BottomNavItem(
            icon = Icons.Default.DateRange,
            label = stringResource(R.string.label_history),
            selected = currentRoute == "history",
            onClick = onHistory
        )
        BottomNavItem(
            icon = Icons.Default.Star,
            label = stringResource(R.string.label_insights),
            selected = currentRoute == "insights",
            onClick = onInsights
        )
        BottomNavItem(
            icon = Icons.Default.Person,
            label = stringResource(R.string.label_profile),
            selected = currentRoute == "profile",
            onClick = onProfile
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        },
        label = { Text(text = label, style = MaterialTheme.typography.labelSmall) },
        selected = selected,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Accent,
            selectedTextColor = Accent,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = Accent.copy(alpha = 0.15f)
        )
    )
}
