package com.example.pedometer.feature.home.domain

import com.example.pedometer.data.HealthRepository
import com.example.pedometer.data.Timeframe
import com.example.pedometer.model.DayData
import com.example.pedometer.util.toKm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TopUseCase @Inject constructor(private val repository: HealthRepository) {
    operator fun invoke(period: Timeframe): Flow<List<DayData>> {
        return repository.readAllSteps(period).map { list ->
            list.sortedByDescending { it.steps }.take(10).map{day ->
                day.copy(km = day.steps.toKm())
            }
        }
    }
}

