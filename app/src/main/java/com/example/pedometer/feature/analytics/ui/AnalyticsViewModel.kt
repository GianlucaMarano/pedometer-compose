@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pedometer.feature.analytics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedometer.feature.analytics.domain.ActiveDaysPercentageUseCase
import com.example.pedometer.feature.analytics.domain.ActiveDaysUseCase
import com.example.pedometer.feature.analytics.domain.AverageKmUseCase
import com.example.pedometer.feature.analytics.domain.GenerateCalendarUseCase
import com.example.pedometer.feature.analytics.domain.GetMonthStepsUseCase
import com.example.pedometer.feature.analytics.domain.LongestActiveStreakUseCase
import com.example.pedometer.feature.analytics.domain.LongestRestStreakUseCase
import com.example.pedometer.feature.analytics.domain.MaxDayUseCase
import com.example.pedometer.feature.analytics.domain.MinDayUseCase
import com.example.pedometer.feature.analytics.domain.RestDaysUseCase
import com.example.pedometer.feature.analytics.domain.TotalKmUseCase
import com.example.pedometer.feature.analytics.domain.WeekdayAverageDistanceUseCase
import com.example.pedometer.feature.analytics.domain.WeekdayPercentageUseCase
import com.example.pedometer.feature.analytics.domain.WeekendAverageDistanceUseCase
import com.example.pedometer.feature.analytics.domain.WeekendPercentageUseCase
import com.example.pedometer.feature.analytics.model.CalendarDay
import com.example.pedometer.util.toKm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getMonthStepsUseCase: GetMonthStepsUseCase,
    private val generateCalendarUseCase: GenerateCalendarUseCase,
    private val totalKmUseCase: TotalKmUseCase,
    private val averageDailyKilometers: AverageKmUseCase,
    private val maxDayUseCase: MaxDayUseCase,
    private val minDayUseCase: MinDayUseCase,
    private val activeDaysUseCase: ActiveDaysUseCase,
    private val restDaysUseCase: RestDaysUseCase,
    private val activeDaysPercentageUseCase: ActiveDaysPercentageUseCase,
    private val longestActiveStreakUseCase: LongestActiveStreakUseCase,
    private val longestRestStreakUseCase: LongestRestStreakUseCase,
    private val averageWeekendDistanceUseCase: WeekendAverageDistanceUseCase,
    private val averageWeekdayDistanceUseCase: WeekdayAverageDistanceUseCase,
    private val weekendPercentageUseCase: WeekendPercentageUseCase,
    private val weekdayPercentageUseCase: WeekdayPercentageUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<AnalyticsUiState> = MutableStateFlow(AnalyticsUiState())
    val state: StateFlow<AnalyticsUiState> =
        _state.flatMapLatest { uiState ->
            generateCalendarUseCase(uiState.currentDate)
                .zip(getMonthStepsUseCase(uiState.currentDate)) { calendarDays, monthSteps ->
                    uiState.copy(
                        calendarDays = calendarDays.map { day ->
                            val steps =
                                monthSteps.find { it.date.dayOfYear == day.date.dayOfYear }?.steps
                                    ?: 0
                            day.copy(
                                isToday = day.date.toLocalDate() == LocalDateTime.now()
                                    .toLocalDate(),
                                noData = steps.toInt() == 0,
                                steps = steps.toInt(),
                                km = steps.toKm()
                            )
                        },
                        stats = MonthlyStatsUiState(
                            totalKilometers = totalKmUseCase(monthSteps),
                            averageDailyKilometers = averageDailyKilometers(monthSteps),
                            maxDay = maxDayUseCase(monthSteps),
                            minDay = minDayUseCase(monthSteps),
                            activeDays = activeDaysUseCase(monthSteps),
                            restDays = restDaysUseCase(monthSteps),
                            activeDaysPercentage = activeDaysPercentageUseCase(
                                activeDaysUseCase(monthSteps),
                                calendarDays.filter { it.isInCurrentMonth }.size
                            ),
                            longestActiveStreak = longestActiveStreakUseCase(monthSteps),
                            longestRestStreak = longestRestStreakUseCase(monthSteps),
                            averageWeekendDistance = averageWeekendDistanceUseCase(monthSteps),
                            averageWeekdayDistance = averageWeekdayDistanceUseCase(monthSteps),
                            weekendPercentage = weekendPercentageUseCase(monthSteps),
                            weekdayPercentage = weekdayPercentageUseCase(monthSteps),
                        )
                    )
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AnalyticsUiState()
        )


    fun onEvent(intent: AnalyticsUiIntent) {
        when (intent) {
            AnalyticsUiIntent.NextMonth -> _state.update {
                it.copy(
                    currentDate = _state.value.currentDate.plusMonths(
                        1
                    )
                )
            }

            AnalyticsUiIntent.PreviousMonth -> _state.update {
                it.copy(
                    currentDate = _state.value.currentDate.minusMonths(
                        1
                    )
                )
            }
        }
    }
}

sealed interface AnalyticsUiIntent {
    object NextMonth : AnalyticsUiIntent
    object PreviousMonth : AnalyticsUiIntent
}

data class AnalyticsUiState(
    val currentDate: LocalDateTime = LocalDateTime.now(),
    val calendarDays: List<CalendarDay> = emptyList(),
    val stats: MonthlyStatsUiState = MonthlyStatsUiState()
)

data class MonthlyStatsUiState(
    val totalKilometers: Float = 0f,
    val maxDay: DayStat = DayStat(),
    val minDay: DayStat = DayStat(),

    val activeDays: Int = 0,
    val restDays: Int = 0,
    val longestActiveStreak: Int = 0,
    val longestRestStreak: Int = 0,

    val averageDailyKilometers: Float = 0f,
    val averageWeekendDistance: Float = 0f,
    val averageWeekdayDistance: Float = 0f,

    val activeDaysPercentage: Float = 0f,
    val weekendPercentage: Float = 0f,
    val weekdayPercentage: Float = 0f,
)

data class DayStat(
    val date: LocalDateTime = LocalDateTime.now(),
    val kilometers: Float = 0f
)