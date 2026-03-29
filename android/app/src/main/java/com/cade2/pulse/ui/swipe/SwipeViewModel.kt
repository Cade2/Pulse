package com.cade2.pulse.ui.swipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cade2.pulse.data.repository.SessionRepository
import com.cade2.pulse.domain.model.EmotionCard
import com.cade2.pulse.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SwipeUiState(
    val isLoading: Boolean = false,
    val cards: List<EmotionCard> = emptyList(),
    val sessionId: String = "",
    val currentIndex: Int = 0,
    val acceptedCardIds: List<String> = emptyList(),
    val isComplete: Boolean = false,
    val error: String? = null
) {
    val totalCards: Int get() = cards.size
    val cardsRemaining: Int get() = (totalCards - currentIndex).coerceAtLeast(0)
    val progress: Float get() = if (totalCards == 0) 0f else currentIndex.toFloat() / totalCards
}

@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SwipeUiState())
    val uiState: StateFlow<SwipeUiState> = _uiState.asStateFlow()

    init {
        loadSession()
    }

    fun loadSession() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = sessionRepository.getTodaySession()) {
                is Result.Success -> {
                    val session = result.data
                    when (val cardsResult = sessionRepository.getEmotionCards()) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    cards = cardsResult.data.shuffled(),
                                    sessionId = session?.sessionId ?: "",
                                    error = null
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(isLoading = false, error = cardsResult.message)
                            }
                        }
                        Result.Loading -> Unit
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                Result.Loading -> Unit
            }
        }
    }

    fun onSwipe(cardId: String, accepted: Boolean) {
        viewModelScope.launch {
            val sessionId = _uiState.value.sessionId
            if (sessionId.isNotBlank()) {
                sessionRepository.saveSwipe(sessionId, cardId, accepted)
            }
            val newAccepted = if (accepted) {
                _uiState.value.acceptedCardIds + cardId
            } else {
                _uiState.value.acceptedCardIds
            }
            val newIndex = _uiState.value.currentIndex + 1
            val isComplete = newIndex >= _uiState.value.cards.size

            _uiState.update {
                it.copy(
                    currentIndex = newIndex,
                    acceptedCardIds = newAccepted,
                    isComplete = isComplete
                )
            }
        }
    }
}
