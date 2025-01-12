package com.example.pedometer.feature.home.domain

import com.example.pedometer.data.HealthRepository
import com.example.pedometer.model.DayData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class ReadStepsUseCase @Inject constructor(private val repository: HealthRepository) {
    operator fun invoke(): Flow<List<DayData>> {
        return repository.readMonthSteps(LocalDateTime.now())
    }
}