package com.cade2.pulse.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cade2.pulse.data.repository.AuthRepository
import com.cade2.pulse.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                is Result.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                Result.Loading -> Unit
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.register(email, password, name)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                is Result.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                Result.Loading -> Unit
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
