package com.cade2.pulse.ui.swipe

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cade2.pulse.R
import com.cade2.pulse.data.repository.SessionRepository
import com.cade2.pulse.ui.theme.Accent
import com.cade2.pulse.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModel

data class ContextTagUiState(
    val isLoading: Boolean = false,
    val streakCount: Int = 0,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ContextTagViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContextTagUiState())
    val uiState: StateFlow<ContextTagUiState> = _uiState.asStateFlow()

    fun completeSession(
        sessionId: String,
        acceptedCardIds: List<String>,
        contextSocial: String?,
        contextEnergy: String?,
        contextSleep: String?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = sessionRepository.completeSession(
                sessionId, acceptedCardIds, contextSocial, contextEnergy, contextSleep
            )) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true, streakCount = result.data)
                    }
                }
                is Result.Error -> {
                    // Even if API fails, we saved locally — navigate as success
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true, streakCount = 0)
                    }
                }
                Result.Loading -> Unit
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContextTagScreen(
    sessionId: String,
    acceptedCardIdsRaw: String,
    onNavigateToCompletion: (streakCount: Int) -> Unit,
    onSkip: () -> Unit,
    viewModel: ContextTagViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val acceptedCardIds = remember(acceptedCardIdsRaw) {
        acceptedCardIdsRaw.split(",").filter { it.isNotBlank() }
    }

    var selectedSocial by remember { mutableStateOf<String?>(null) }
    var selectedEnergy by remember { mutableStateOf<String?>(null) }
    var selectedSleep by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToCompletion(uiState.streakCount)
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 48.dp)
        ) {
            Text(
                text = stringResource(R.string.context_tag_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.context_tag_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Social context
            ContextTagGroup(
                label = stringResource(R.string.label_social_context),
                options = listOf(
                    stringResource(R.string.label_alone),
                    stringResource(R.string.label_friends),
                    stringResource(R.string.label_family),
                    stringResource(R.string.label_work)
                ),
                selected = selectedSocial,
                onSelect = { selectedSocial = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Energy context
            ContextTagGroup(
                label = stringResource(R.string.label_energy_context),
                options = listOf(
                    stringResource(R.string.label_energy_low),
                    stringResource(R.string.label_energy_medium),
                    stringResource(R.string.label_energy_high)
                ),
                selected = selectedEnergy,
                onSelect = { selectedEnergy = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sleep context
            ContextTagGroup(
                label = stringResource(R.string.label_sleep_context),
                options = listOf(
                    stringResource(R.string.label_sleep_bad),
                    stringResource(R.string.label_sleep_okay),
                    stringResource(R.string.label_sleep_good)
                ),
                selected = selectedSleep,
                onSelect = { selectedSleep = it }
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    viewModel.completeSession(
                        sessionId = sessionId,
                        acceptedCardIds = acceptedCardIds,
                        contextSocial = selectedSocial,
                        contextEnergy = selectedEnergy,
                        contextSleep = selectedSleep
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.background, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = stringResource(R.string.label_save_finish),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onSkip,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.label_skip),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ContextTagGroup(
    label: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = option == selected,
                    onClick = { onSelect(option) },
                    label = { Text(text = option, style = MaterialTheme.typography.labelMedium) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Accent,
                        selectedLabelColor = MaterialTheme.colorScheme.background,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
