@file:OptIn(ExperimentalFoundationApi::class)

package com.example.pedometer.feature.analytics.ui


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.example.pedometer.R
import com.example.pedometer.feature.analytics.ui.components.KmBarChart
import com.example.pedometer.feature.analytics.ui.components.KmCalendarChart
import com.example.pedometer.feature.analytics.ui.components.MonthSelector
import com.example.pedometer.feature.analytics.ui.components.NumericValue
import com.example.pedometer.feature.analytics.ui.components.RoundLabeledProgress
import com.example.pedometer.util.roundTo
import kotlinx.coroutines.launch

@Composable
fun AnalyticsScreen(
    state: AnalyticsUiState,
    onEvent: (AnalyticsUiIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        MonthSelector(
            currentDate = state.currentDate,
            onEvent = onEvent
        )

        LazyColumn {
            item {
                KmCalendarChart(
                    items = state.calendarDays,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                KmBarChart(
                    data = state.calendarDays.mapNotNull { if (it.isInCurrentMonth) it.km else null },
                    labels = state.calendarDays.mapNotNull { if (it.isInCurrentMonth) it.date.dayOfMonth.toString() else null },
                    modifier = Modifier
                        .animatePlacement()
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(
                                topStart = 24.dp,
                                topEnd = 24.dp,
                                bottomEnd = 4.dp,
                                bottomStart = 4.dp,
                            ),
                        )
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp)

                        .fillMaxWidth()
                        .animatePlacement()
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .padding(16.dp),

                    ) {
                    RoundLabeledProgress(
                        progress = state.stats.activeDaysPercentage,
                        label = stringResource(R.string.active_days)
                    )
                    RoundLabeledProgress(
                        progress = state.stats.weekendPercentage,
                        label = stringResource(R.string.km_during_weekend)
                    )
                    RoundLabeledProgress(
                        progress = state.stats.weekdayPercentage,
                        label = stringResource(R.string.km_during_weekday)
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .animatePlacement()
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .padding(24.dp),

                    ) {
                    NumericValue(
                        value = state.stats.totalKilometers.roundTo(1),
                        label = stringResource(R.string.total)
                    )
                    NumericValue(
                        value = state.stats.maxDay.kilometers.roundTo(1),
                        label = stringResource(R.string.best_day)
                    )
                    NumericValue(
                        value = state.stats.minDay.kilometers.roundTo(1),
                        label = stringResource(R.string.worst_day)
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .animatePlacement()
                ) {
                    NumericValue(
                        value = state.stats.activeDays,
                        label = stringResource(R.string.active_days),
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .weight(0.5f)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = RoundedCornerShape(4.dp),
                            )
                            .padding(24.dp)

                    )
                    NumericValue(
                        value = state.stats.restDays,
                        label = stringResource(R.string.rest_days),
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .weight(0.5f)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = RoundedCornerShape(4.dp),
                            )
                            .padding(24.dp)
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .animatePlacement()
                ) {
                    NumericValue(
                        value = state.stats.longestActiveStreak,
                        label = stringResource(R.string.active_days_streak),
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .weight(0.5f)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = RoundedCornerShape(4.dp),
                            )
                            .padding(24.dp)

                    )
                    NumericValue(
                        value = state.stats.longestRestStreak,
                        label = stringResource(R.string.rest_days_streak),
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .weight(0.5f)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = RoundedCornerShape(4.dp),
                            )
                            .padding(24.dp)
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 24.dp)
                        .fillMaxWidth()
                        .animatePlacement()
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 4.dp,
                                bottomEnd = 24.dp,
                                bottomStart = 24.dp
                            ),
                        )
                        .padding(24.dp),

                    ) {
                    NumericValue(
                        value = state.stats.averageDailyKilometers.roundTo(1),
                        label = stringResource(R.string.average_daily_kilometers)
                    )
                    NumericValue(
                        value = state.stats.averageWeekendDistance.roundTo(1),
                        label = stringResource(R.string.average_weekend_kilometers)
                    )
                    NumericValue(
                        value = state.stats.averageWeekdayDistance.roundTo(1),
                        label = stringResource(R.string.average_weekday_kilometers)
                    )
                }
            }
        }
    }
}

fun Modifier.animatePlacement(): Modifier = composed {
    val scope = rememberCoroutineScope()
    var targetOffset by remember { mutableStateOf(IntOffset.Zero) }
    var animatable by remember {
        mutableStateOf<Animatable<IntOffset, AnimationVector2D>?>(null)
    }
    this
        .onPlaced {
            targetOffset = it
                .positionInParent()
                .round()
        }
        .offset {
            val anim = animatable ?: Animatable(targetOffset, IntOffset.VectorConverter)
                .also {
                    animatable = it
                }
            if (anim.targetValue != targetOffset) {
                scope.launch {
                    anim.animateTo(targetOffset, spring(stiffness = Spring.StiffnessHigh))
                }
            }
            animatable?.let { it.value - targetOffset } ?: IntOffset.Zero
        }
}
