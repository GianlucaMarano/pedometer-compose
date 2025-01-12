package com.example.pedometer.data.network

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.pedometer.data.Timeframe
import com.example.pedometer.model.DayData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject


class HealthDataSource @Inject constructor(private val healthConnectClient: HealthConnectClient) {

    fun readMonthSteps(date: LocalDateTime): Flow<List<DayData>> = flow {
        val startTime =
            date.withDayOfMonth(1)
                .minusDays(1)
                .atZone(ZoneId.systemDefault())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .toInstant()
        val endTime =
            date.with(TemporalAdjusters.lastDayOfMonth())
                .atZone(ZoneId.systemDefault())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .toInstant()

        val request = AggregateGroupByDurationRequest(
            metrics = setOf(StepsRecord.COUNT_TOTAL),
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            timeRangeSlicer = Duration.ofDays(1)
        )

        val response = healthConnectClient.aggregateGroupByDuration(request)
        val list = mutableListOf<DayData>()

        response.forEach { periodAggregate ->
            val steps =
                periodAggregate.result[StepsRecord.COUNT_TOTAL] ?: 0L

            val startDateTime =
                periodAggregate.startTime.atZone(ZoneId.systemDefault()).toLocalDateTime()
            list.add(
                DayData(
                    date = startDateTime,
                    steps = steps
                )
            )
        }
        emit(list)
    }

    suspend fun getAvailableDataRange(): Pair<Instant?, Instant?> {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(
                    Instant.EPOCH,
                    Instant.now()
                ),
            )
        )

        val startTime = response.records.minOfOrNull {
            it.startTime.atZone(it.startZoneOffset)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .toInstant()
        }
        val endTime = response.records.maxOfOrNull { it.endTime }
        return startTime to endTime
    }


    /** These data have an aggregation problem, day's steps are wrong when near to midnight
     */
    fun readYearStepsAggregateByPeriod(period: Timeframe): Flow<List<DayData>> = flow {

        val (startTime, _) = getAvailableDataRange()

        val request = AggregateGroupByDurationRequest(
            metrics = setOf(StepsRecord.COUNT_TOTAL),
            timeRangeFilter = TimeRangeFilter.after(
                startTime
                    ?.atZone(ZoneId.systemDefault())
                    ?.plusDays(1)?.toInstant()
                    ?: Instant.EPOCH
            ),
            timeRangeSlicer = Duration.ofDays(1)
        )

        val response = healthConnectClient.aggregateGroupByDuration(request)

        val results = when (period) {
            Timeframe.DAY -> {
                response.map { periodAggregate ->
                    val steps =
                        periodAggregate.result[StepsRecord.COUNT_TOTAL] ?: 0L

                    val startDateTime =
                        periodAggregate.startTime.atZone(periodAggregate.zoneOffset)
                            .toLocalDateTime()
                    DayData(
                        date = startDateTime,
                        steps = steps
                    )
                }

            }

            Timeframe.WEEK -> {
                response.groupBy { periodAggregate ->
                    val weekFields = WeekFields.of(Locale.getDefault())
                    periodAggregate.startTime.atZone(periodAggregate.zoneOffset).toLocalDate()
                        .with(weekFields.firstDayOfWeek)

                }.map { (weekStart, weeklyRecords) ->
                    val totalSteps = weeklyRecords.sumOf { it.result[StepsRecord.COUNT_TOTAL] ?: 0 }
                    DayData(
                        date = weekStart.atStartOfDay(),
                        steps = totalSteps
                    )
                }
            }

            Timeframe.MONTH -> {
                response.groupBy { periodAggregate ->
                    YearMonth.from(
                        periodAggregate.startTime.atZone(periodAggregate.zoneOffset).toLocalDate()
                    )

                }.map { (month, monthlyRecords) ->
                    val totalSteps =
                        monthlyRecords.sumOf { it.result[StepsRecord.COUNT_TOTAL] ?: 0 }
                    DayData(
                        date = month.atDay(1)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toLocalDateTime(),
                        steps = totalSteps
                    )
                }
            }
        }
        emit(results)
    }

    suspend fun writeSteps(count: Long, startTime: Instant, endTime: Instant) {
        val stepsRecord = StepsRecord(
            count = count,
            startTime = startTime,
            startZoneOffset = ZoneOffset.UTC,
            endTime = endTime,
            endZoneOffset = ZoneOffset.UTC
        )
        healthConnectClient.insertRecords(
            records = listOf(stepsRecord)
        )
    }
}