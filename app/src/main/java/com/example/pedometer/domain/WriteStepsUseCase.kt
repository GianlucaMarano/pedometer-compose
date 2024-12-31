package com.example.pedometer.domain

import com.example.pedometer.data.HealthRepository
import java.time.Instant
import javax.inject.Inject

class WriteStepsUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(count: Long, startTime: Instant, endTime: Instant) {
        repository.addSteps(count, startTime, endTime)
    }
}