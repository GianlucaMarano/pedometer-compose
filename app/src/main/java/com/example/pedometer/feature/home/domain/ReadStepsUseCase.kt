package com.example.pedometer.feature.home.domain

import androidx.health.connect.client.records.StepsRecord
import com.example.pedometer.data.HealthRepository
import com.example.pedometer.model.DayData
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

class ReadStepsUseCase @Inject constructor(private val repository: HealthRepository) {
    operator fun invoke(startTime: Instant, endTime: Instant): Flow<List<DayData>> {
        return repository.getSteps(startTime, endTime)
    }
}