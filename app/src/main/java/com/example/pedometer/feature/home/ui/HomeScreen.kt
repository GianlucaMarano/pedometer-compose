package com.example.pedometer.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pedometer.R
import com.example.pedometer.data.Timeframe
import com.example.pedometer.model.DayData
import com.example.pedometer.feature.home.ui.components.HeaderValue
import com.example.pedometer.feature.home.ui.components.PeriodSelector
import com.example.pedometer.feature.home.ui.components.TopListItemUI
import java.time.LocalDateTime
import kotlin.math.roundToInt


@Composable
fun StepsScreen(
    uiState: StepsUiState,
    onIntent: (StepsUiIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.errorMessage != null -> Text("Error: ${uiState.errorMessage}")
            else -> {
                HomeHeader(
                    kmToday = uiState.kmToday,
                    kmThisMonth = uiState.kmThisMonth,
                    stepsToday = uiState.stepsToday
                )

                PeriodSelector(
                    options = Timeframe.entries,
                    selectedOption = uiState.timeSelected,
                    onOptionSelected = onIntent,
                    modifier = Modifier.padding(24.dp)
                )

                TopPeriodList(
                    list = uiState.top,
                    timeSelected = uiState.timeSelected
                )
            }
        }
    }
}

@Composable
fun TopPeriodList(
    list: List<DayData>,
    timeSelected: Timeframe,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(
            items = list,
            key = { _, item -> item.uid }
        ) { idx, item ->
            val isFirst = idx == 0
            val isLast = idx == list.size - 1

            val shape = when {
                isFirst -> RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 4.dp
                )

                isLast -> RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 4.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )

                else -> RoundedCornerShape(4.dp)
            }
            TopListItemUI(
                item = item,
                shouldShowTrophy = isFirst,
                period = timeSelected,
                modifier = Modifier
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 2.dp,
                        bottom = if (isLast) 24.dp else 2.dp
                    )
                    .shadow(2.dp, shape = shape)
                    .fillMaxWidth()
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(16.dp)
                    .animateItem()
            )

        }
    }
}

@Composable
fun HomeHeader(
    kmToday: Float,
    kmThisMonth: Float,
    stepsToday: Int,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .padding(top = 60.dp, bottom = 24.dp)
            .fillMaxWidth()
    ) {
        HeaderValue(
            value = kmThisMonth.roundToInt(),
            period = Timeframe.MONTH,
            unit = stringResource(R.string.km),
            style = MaterialTheme.typography.displaySmall
        )
        HeaderValue(
            value = kmToday.roundToInt(),
            period = Timeframe.DAY,
            unit = stringResource(R.string.km)
        )
        HeaderValue(
            value = stepsToday,
            period = Timeframe.DAY,
            style = MaterialTheme.typography.displaySmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    StepsScreen(
        uiState = StepsUiState(
            stepsToday = 10000,
            kmToday = 10f,
            kmThisMonth = 100f,
            steps = emptyList(),
            top = listOf(
                DayData(
                    date = LocalDateTime.now(),
                    km = 10f,
                    steps = 10000
                ),
                DayData(
                    date = LocalDateTime.now(),
                    km = 7f,
                    steps = 8000
                ),
            ),
            isLoading = false,
            errorMessage = null,
            timeSelected = Timeframe.DAY
        ),
        onIntent = {}
    )

}