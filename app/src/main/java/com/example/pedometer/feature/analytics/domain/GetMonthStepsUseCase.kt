package com.example.pedometer.feature.analytics.domain

import com.example.pedometer.data.HealthRepository
import com.example.pedometer.model.DayData
import com.example.pedometer.util.toKm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class GetMonthStepsUseCase @Inject constructor(private val repository: HealthRepository) {
    operator fun invoke(month: LocalDateTime): Flow<List<DayData>> {
        return repository.readMonthSteps(month).map { list ->
            list.map{dayData ->
                dayData.copy(km = dayData.steps.toKm())
            }
        }
    }
}