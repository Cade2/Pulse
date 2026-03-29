package com.cade2.pulse.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cade2.pulse.data.repository.InsightsRepository
import com.cade2.pulse.data.repository.SessionRepository
import com.cade2.pulse.domain.model.Pattern
import com.cade2.pulse.domain.model.Streak
import com.cade2.pulse.domain.model.WeeklyInsight
import com.cade2.pulse.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsightsUiState(
    val isLoading: Boolean = false,
    val weeklyInsight: WeeklyInsight? = null,
    val patterns: List<Pattern> = emptyList(),
    val streak: Streak? = null,
    val sessionCount: Int = 0,
    val error: String? = null
) {
    val hasEnoughData: Boolean get() = sessionCount >= 14
}

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val insightsRepository: InsightsRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init {
        loadInsights()
    }

    fun loadInsights() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            launch {
                when (val result = sessionRepository.getStreak()) {
                    is Result.Success -> _uiState.update { it.copy(streak = result.data) }
                    else -> Unit
                }
            }

            launch {
                when (val result = insightsRepository.getWeeklyInsights()) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(weeklyInsight = result.data, isLoading = false)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                    Result.Loading -> Unit
                }
            }

            launch {
                when (val result = insightsRepository.getPatterns()) {
                    is Result.Success -> _uiState.update { it.copy(patterns = result.data) }
                    else -> Unit
                }
            }

            launch {
                when (val result = sessionRepository.getSessionHistory()) {
                    is Result.Success -> _uiState.update { it.copy(sessionCount = result.data.size) }
                    else -> Unit
                }
            }
        }
    }
}
