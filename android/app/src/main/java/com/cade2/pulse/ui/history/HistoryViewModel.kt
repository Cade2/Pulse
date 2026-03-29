package com.cade2.pulse.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cade2.pulse.data.repository.InsightsRepository
import com.cade2.pulse.data.repository.SessionRepository
import com.cade2.pulse.domain.model.CalendarDay
import com.cade2.pulse.domain.model.EmotionCard
import com.cade2.pulse.domain.model.Session
import com.cade2.pulse.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class HistoryUiState(
    val isLoading: Boolean = false,
    val calendarDays: List<CalendarDay> = emptyList(),
    val displayMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val displayYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val selectedDayEmotions: List<EmotionCard>? = null,
    val sessions: List<Session> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val insightsRepository: InsightsRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadCalendar()
        loadSessions()
    }

    fun loadCalendar() {
        val month = _uiState.value.displayMonth
        val year = _uiState.value.displayYear
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = insightsRepository.getCalendar(month, year)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, calendarDays = result.data) }
                is Result.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                Result.Loading -> Unit
            }
        }
    }

    private fun loadSessions() {
        viewModelScope.launch {
            when (val result = sessionRepository.getSessionHistory()) {
                is Result.Success -> _uiState.update { it.copy(sessions = result.data) }
                else -> Unit
            }
        }
    }

    fun previousMonth() {
        val current = _uiState.value
        val newMonth = if (current.displayMonth == 1) 12 else current.displayMonth - 1
        val newYear = if (current.displayMonth == 1) current.displayYear - 1 else current.displayYear
        _uiState.update { it.copy(displayMonth = newMonth, displayYear = newYear) }
        loadCalendar()
    }

    fun nextMonth() {
        val current = _uiState.value
        val newMonth = if (current.displayMonth == 12) 1 else current.displayMonth + 1
        val newYear = if (current.displayMonth == 12) current.displayYear + 1 else current.displayYear
        _uiState.update { it.copy(displayMonth = newMonth, displayYear = newYear) }
        loadCalendar()
    }

    fun selectDay(date: String) {
        val session = _uiState.value.sessions.find { it.date == date }
        _uiState.update { it.copy(selectedDayEmotions = session?.acceptedEmotions) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedDayEmotions = null) }
    }
}
