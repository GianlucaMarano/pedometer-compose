package com.example.pedometer.data


import com.example.pedometer.data.network.HealthDataSource
import com.example.pedometer.model.DayData
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject

class HealthRepository @Inject constructor(private val dataSource: HealthDataSource) {

    fun getSteps(startTime: Instant, endTime: Instant): Flow<List<DayData>> {
        return dataSource.readSteps(startTime, endTime)
    }

    fun readMonthSteps(month: LocalDateTime): Flow<List<DayData>> {
        return dataSource.readMonthSteps(month)
    }

    fun readAllSteps(period: Timeframe): Flow<List<DayData>> {
        return dataSource.readYearStepsAggregateByPeriod(period)
    }

    suspend fun addSteps(count: Long, startTime: Instant, endTime: Instant) {
        dataSource.writeSteps(count, startTime, endTime)
    }
}