package com.cade2.pulse.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cade2.pulse.data.repository.SessionRepository
import com.cade2.pulse.domain.model.EmotionCard
import com.cade2.pulse.domain.model.Session
import com.cade2.pulse.util.EncryptedPrefs
import com.cade2.pulse.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val todaySession: Session? = null,
    val streakCount: Int = 0,
    val acceptedEmotions: List<EmotionCard> = emptyList(),
    val error: String? = null
) {
    val hasCompletedToday: Boolean get() = todaySession?.isCompleted == true
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val encryptedPrefs: EncryptedPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    userName = encryptedPrefs.userName ?: ""
                )
            }
            when (val result = sessionRepository.getTodaySession()) {
                is Result.Success -> {
                    val session = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            todaySession = session,
                            acceptedEmotions = session?.acceptedEmotions ?: emptyList()
                        )
                    }
                    loadStreak()
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                Result.Loading -> Unit
            }
        }
    }

    private fun loadStreak() {
        viewModelScope.launch {
            when (val result = sessionRepository.getStreak()) {
                is Result.Success -> {
                    _uiState.update { it.copy(streakCount = result.data.currentStreak) }
                }
                else -> Unit
            }
        }
    }
}
