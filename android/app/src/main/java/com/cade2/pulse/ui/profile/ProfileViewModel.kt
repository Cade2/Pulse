package com.cade2.pulse.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cade2.pulse.data.repository.UserRepository
import com.cade2.pulse.domain.model.User
import com.cade2.pulse.util.EncryptedPrefs
import com.cade2.pulse.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val selectedColor: String = "#A78BFA",
    val notificationHour: Int = 20,
    val notificationMinute: Int = 0,
    val isSuccess: Boolean = false,
    val isDeleted: Boolean = false,
    val isLoggedOut: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val encryptedPrefs: EncryptedPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                selectedColor = encryptedPrefs.avatarColor,
                notificationHour = encryptedPrefs.notificationHour,
                notificationMinute = encryptedPrefs.notificationMinute
            )
        }
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = userRepository.getProfile()) {
                is Result.Success -> {
                    val user = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user,
                            selectedColor = user.avatarColor,
                            notificationHour = user.notificationHour,
                            notificationMinute = user.notificationMinute
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                Result.Loading -> Unit
            }
        }
    }

    fun selectColor(hex: String) {
        _uiState.update { it.copy(selectedColor = hex) }
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(notificationHour = hour, notificationMinute = minute) }
    }

    fun saveSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            when (userRepository.updateSettings(
                state.selectedColor,
                state.notificationHour,
                state.notificationMinute
            )) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                is Result.Error -> _uiState.update { it.copy(isLoading = false, isSuccess = false) }
                Result.Loading -> Unit
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (userRepository.deleteAccount()) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, isDeleted = true) }
                is Result.Error -> _uiState.update { it.copy(isLoading = false) }
                Result.Loading -> Unit
            }
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
