package com.cade2.pulse.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cade2.pulse.R
import com.cade2.pulse.ui.theme.Accent
import com.cade2.pulse.ui.theme.EmptyDay
import com.cade2.pulse.ui.theme.Mixed
import com.cade2.pulse.ui.theme.Negative
import com.cade2.pulse.ui.theme.Positive
import com.cade2.pulse.ui.theme.Surface
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val WEEK_DAYS = listOf("S", "M", "T", "W", "T", "F", "S")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bottomSheetState = rememberModalBottomSheetState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.label_history),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Month navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.previousMonth() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous month",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    .format(
                        Calendar.getInstance().apply {
                            set(Calendar.YEAR, uiState.displayYear)
                            set(Calendar.MONTH, uiState.displayMonth - 1)
                        }.time
                    )
                Text(
                    text = monthName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { viewModel.nextMonth() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next month",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Day of week headers
            Row(modifier = Modifier.fillMaxWidth()) {
                WEEK_DAYS.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Build calendar cells
            val calendarCells = buildCalendarCells(
                month = uiState.displayMonth,
                year = uiState.displayYear,
                calendarDays = uiState.calendarDays.associate { it.date to it }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(calendarCells) { cell ->
                    CalendarCell(
                        cell = cell,
                        onClick = {
                            if (cell.date != null) {
                                viewModel.selectDay(cell.date)
                            }
                        }
                    )
                }
            }

            // Legend
            Spacer(modifier = Modifier.height(24.dp))
            CalendarLegend()
        }

        // Bottom sheet for selected day
        if (uiState.selectedDayEmotions != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.clearSelection() },
                sheetState = bottomSheetState,
                containerColor = Surface,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_emotions_this_day),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (uiState.selectedDayEmotions!!.isEmpty()) {
                        Text(
                            text = stringResource(R.string.label_no_emotions_recorded),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.selectedDayEmotions!!.forEach { emotion ->
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
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

private data class CalendarCell(
    val day: Int?,
    val date: String?,
    val sentiment: String?
)

private fun buildCalendarCells(
    month: Int,
    year: Int,
    calendarDays: Map<String, com.cade2.pulse.domain.model.CalendarDay>
): List<CalendarCell> {
    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val cells = mutableListOf<CalendarCell>()
    repeat(firstDayOfWeek) { cells.add(CalendarCell(null, null, null)) }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    for (day in 1..daysInMonth) {
        cal.set(Calendar.DAY_OF_MONTH, day)
        val dateStr = sdf.format(cal.time)
        val calDay = calendarDays[dateStr]
        cells.add(CalendarCell(day = day, date = dateStr, sentiment = calDay?.dominantSentiment))
    }
    return cells
}

@Composable
private fun CalendarCell(cell: CalendarCell, onClick: () -> Unit) {
    val bgColor = when {
        cell.day == null -> Color.Transparent
        cell.sentiment == null -> EmptyDay
        cell.sentiment.equals("positive", ignoreCase = true) -> Positive
        cell.sentiment.equals("negative", ignoreCase = true) -> Negative
        else -> Mixed
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(enabled = cell.day != null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (cell.day != null) {
            Text(
                text = cell.day.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = if (bgColor == EmptyDay) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CalendarLegend() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LegendItem(color = Positive, label = "Positive")
        LegendItem(color = Negative, label = "Negative")
        LegendItem(color = Mixed, label = "Mixed")
        LegendItem(color = EmptyDay, label = "No session")
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
