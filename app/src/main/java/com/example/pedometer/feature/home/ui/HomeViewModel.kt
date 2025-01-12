@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pedometer.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedometer.data.Timeframe
import com.example.pedometer.feature.home.domain.ReadStepsUseCase
import com.example.pedometer.feature.home.domain.TopUseCase
import com.example.pedometer.model.DayData
import com.example.pedometer.util.toKm
import com.example.pedometer.util.toStepsThisMonth
import com.example.pedometer.util.toStepsToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val readStepsUseCase: ReadStepsUseCase,
    private val topUseCase: TopUseCase,
) : ViewModel() {
    companion object {
        val today = LocalDateTime.now()
        val startOfMonth = today.withDayOfMonth(1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .atZone(ZoneId.systemDefault()).toInstant()
    }

    private val _uiState = MutableStateFlow(StepsUiState())
    val uiState: StateFlow<StepsUiState> =
        _uiState
            .flatMapLatest { state ->
                combine(
                    flowOf(state),
                    readStepsUseCase(),
                    topUseCase(state.timeSelected),
                ) { currentState, steps, top ->

                    currentState.copy(
                        stepsToday = steps.toStepsToday(),
                        kmToday = steps.toStepsToday().toKm(),
                        kmThisMonth = steps.toStepsThisMonth().toKm(),
                        steps = steps,
                        top = top,
                        isLoading = false
                    )
                }
            }
            .mapLatest { state ->
                state.copy(steps = state.steps)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StepsUiState())

    fun handleIntent(intent: StepsUiIntent) {
        when (intent) {
            is StepsUiIntent.PeriodSelected -> setPeriod(intent.period)
        }
    }

    private fun setPeriod(period: Timeframe) {
        _uiState.update { it.copy(timeSelected = period) }
    }
}

sealed class StepsUiIntent {
    data class PeriodSelected(val period: Timeframe) : StepsUiIntent()
}

data class StepsUiState(
    val stepsToday: Int = 0,
    val kmToday: Float = 0f,
    val kmThisMonth: Float = 0f,
    val steps: List<DayData> = emptyList(),
    val top: List<DayData> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val timeSelected: Timeframe = Timeframe.DAY
)