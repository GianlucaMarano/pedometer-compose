package com.example.pedometer.data

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.pedometer.model.DayData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject


class HealthDataSource @Inject constructor(private val healthConnectClient: HealthConnectClient) {

    fun readSteps(startTime: Instant, endTime: Instant): Flow<List<DayData>> = flow {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        )
        val result =response.records.distinctBy { it.startTime }
            .groupBy { record ->
                record.startTime.atZone(record.startZoneOffset).toLocalDate()
            }.map { (date, dailyRecords) ->
                val totalSteps = dailyRecords.sumOf { it.count }
                DayData(
                    date = date.atStartOfDay(ZoneId.systemDefault())
                        .toLocalDateTime(),
                    steps = totalSteps
                )
            }
        emit(result)
    }

    fun readDistance(startTime: Instant, endTime: Instant): Flow<List<DistanceRecord>> = flow {
        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = DistanceRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        )
        emit(response.records)
    }

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
            timeRangeSlicer = Duration.ofDays(1) // Intervalli di 1 giorni
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

    /*fun aggregateStepsInto(period: Timeframe): Flow<List<Pair<LocalDateTime, Long>>> = flow {
        val endOfTheDay =
            LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)

        val response =
            healthConnectClient.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        endOfTheDay.minusYears(6), endOfTheDay
                    ),
                    timeRangeSlicer = when (period) {
                        Timeframe.DAY -> Period.ofDays(1)
                        Timeframe.WEEK -> Period.ofWeeks(1)
                        Timeframe.MONTH -> Period.ofMonths(1)
                    }
                )
            )

        // Crea una lista mutabile per raccogliere i risultati
        val list = mutableListOf<Pair<LocalDateTime, Long>>()

        response.forEach { periodAggregate ->
            val steps = periodAggregate.result[StepsRecord.COUNT_TOTAL] ?: 0L
            val startDateTime = periodAggregate.startTime.atZone(ZoneId.systemDefault()).toLocalDateTime()
            list.add(Pair(startDateTime, steps))
        }

        emit(list)
    }*/

    /*  fun aggregateStepsInto(period: Timeframe): Flow<List<Pair<LocalDateTime, Long>>> = flow {
          val startTime = ZonedDateTime.now(ZoneId.systemDefault())
              .withHour(0)
              .withMinute(0)
              .withSecond(0)
              .minusYears(2)
              .toInstant()

          val endTime = ZonedDateTime.now(ZoneId.systemDefault())
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
          val list = mutableListOf<Pair<LocalDateTime, Long>>()

          response.forEach { periodAggregate ->
              val steps =
                  periodAggregate.result[StepsRecord.COUNT_TOTAL] ?: 0L

              val startDateTime = periodAggregate.startTime
                  .atZone(ZoneId.systemDefault())
                  .toLocalDateTime()
              list.add(Pair(startDateTime, steps))
          }
          val final = when (period) {
              Timeframe.DAY -> list // Mantieni la lista così com'è
              Timeframe.WEEK -> {
                  list.distinctBy { it.first }
                      .groupBy {
                          val weekFields = WeekFields.of(Locale.getDefault())
                          it.first.with(weekFields.firstDayOfWeek) // Inizio della settimana
                      }
                      .map { (weekStart, entries) ->
                          val total = entries.sumOf { it.second }
                          weekStart to total
                      }
              }

              Timeframe.MONTH -> {
                  list.distinctBy { it.first }
                      .groupBy { YearMonth.from(it.first) } // Raggruppa per mese
                      .map { (month, entries) ->
                          val total = entries.sumOf { it.second }
                          month.atDay(1).atStartOfDay() to total // Primo giorno del mese
                      }
              }
          }

          emit(final)
      }*/


    fun aggregateStepsInto(period: Timeframe): Flow<List<DayData>> = flow {
        val endOfRange = ZonedDateTime.now(ZoneId.systemDefault())
        val startOfRange = endOfRange.minusYears(1).withHour(0).withMinute(0).withSecond(0)

        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(
                    startOfRange.toInstant(),
                    endOfRange.toInstant()
                )
            )
        )

        val results = when (period) {
            Timeframe.DAY -> {
                response.records
                    .distinctBy { it.startTime }
                    .groupBy { record ->
                        record.startTime.atZone(record.startZoneOffset).toLocalDate()
                    }.map { (date, dailyRecords) ->
                        val totalSteps = dailyRecords.sumOf { it.count }
                        DayData(
                            date = date.atStartOfDay(ZoneId.systemDefault())
                                .toLocalDateTime(),
                            steps = totalSteps
                        )
                    }
            }

            Timeframe.WEEK -> {
                response.records
                    .distinctBy { it.startTime }
                    .groupBy { record ->
                        val weekFields = WeekFields.of(Locale.getDefault())
                        record.startTime.atZone(record.startZoneOffset)
                            .toLocalDate()
                            .with(weekFields.firstDayOfWeek)
                    }.map { (weekStart, weeklyRecords) ->
                        val totalSteps = weeklyRecords.sumOf { it.count }
                        DayData(
                            date = weekStart.atStartOfDay(ZoneId.systemDefault())
                                .toLocalDateTime(),
                            steps = totalSteps
                        )
                    }
            }

            Timeframe.MONTH -> {
                response.records
                    .distinctBy { it.startTime }
                    .groupBy { record ->
                        YearMonth.from(
                            record.startTime.atZone(record.startZoneOffset).toLocalDate()
                        )
                    }.map { (month, monthlyRecords) ->
                        val totalSteps = monthlyRecords.sumOf { it.count }
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