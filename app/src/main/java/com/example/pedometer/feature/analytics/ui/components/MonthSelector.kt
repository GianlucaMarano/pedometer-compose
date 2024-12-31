package com.example.pedometer.feature.analytics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pedometer.feature.analytics.ui.AnalyticsUiIntent
import com.example.pedometer.feature.analytics.ui.animatePlacement
import com.example.pedometer.util.topMonthFormat
import java.time.LocalDateTime

@Composable
fun MonthSelector(
    currentDate: LocalDateTime,
    onEvent: (AnalyticsUiIntent) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .animatePlacement()
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { onEvent(AnalyticsUiIntent.PreviousMonth) },
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                )
            }
        )
        Text(
            text = currentDate.topMonthFormat(),
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(
            onClick = { onEvent(AnalyticsUiIntent.NextMonth) },
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                )
            }
        )
    }
}