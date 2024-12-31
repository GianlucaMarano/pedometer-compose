package com.example.pedometer.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pedometer.R
import com.example.pedometer.data.Timeframe
import com.example.pedometer.model.DayData
import com.example.pedometer.util.toCompactKm
import com.example.pedometer.util.topDayFormat
import com.example.pedometer.util.topMonthFormat
import com.example.pedometer.util.topWeekFormat
import java.time.LocalDateTime

@Composable
fun TopListItemUI(
    item: DayData,
    shouldShowTrophy: Boolean = false,
    period: Timeframe = Timeframe.DAY,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = item.km.toCompactKm(),
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = stringResource(R.string.steps_number, item.steps),
                style = MaterialTheme.typography.bodySmall,
            )
        }

        if (shouldShowTrophy) {
            Icon(
                Icons.Rounded.EmojiEvents,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Trophy",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = when (period) {
                Timeframe.DAY -> item.date.topDayFormat()
                Timeframe.WEEK -> {
                    stringResource(
                        id = R.string.from_week_to_week,
                        item.date.topWeekFormat(),
                        item.date.plusDays(6).topWeekFormat()
                    )
                }
                Timeframe.MONTH -> item.date.topMonthFormat()
            },
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UITopListItemPreview() {
    TopListItemUI(
        shouldShowTrophy = true,
        period = Timeframe.WEEK,
        item = DayData(
            date = LocalDateTime.now(),
            km = 10f,
            steps = 10000
        )
    )
}