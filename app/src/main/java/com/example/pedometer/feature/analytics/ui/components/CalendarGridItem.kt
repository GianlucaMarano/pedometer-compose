package com.example.pedometer.feature.analytics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pedometer.feature.analytics.model.CalendarDay
import com.example.pedometer.util.toCompactKm
import com.example.pedometer.util.toCompactString
import java.time.LocalDateTime

@Composable
fun CalendarGridItem(
    item: CalendarDay, modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .size(60.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                color = when {
                    item.isToday && item.isInCurrentMonth -> MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    item.isInCurrentMonth -> MaterialTheme.colorScheme.surfaceContainerHigh

                    else -> MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f)
                }
            )
            .padding(4.dp)

    ) {
        val textColor = when {
            item.isToday && item.isInCurrentMonth -> MaterialTheme.colorScheme.onPrimary
            item.isInCurrentMonth -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        }

        Text(
            text = "${item.date.dayOfMonth}",
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        if (item.isInCurrentMonth && !item.noData) {
            Text(
                text = item.km.toCompactKm(),
                style = MaterialTheme.typography.labelSmall,
                color = textColor
            )
            Text(
                text = item.steps.toCompactString(),
                style = MaterialTheme.typography.labelSmall,
                color = if (item.isToday) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        } else {
            Text(
                text = "-",
                style = MaterialTheme.typography.labelSmall,
                color = textColor
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun CalendarGridItemPreview() {
    CalendarGridItem(
        item = CalendarDay(
            date = LocalDateTime.now(),
            dayOfWeek = LocalDateTime.now().dayOfWeek,
            isToday = true,
            noData = false,
            steps = 10000,
            km = 10f,
            isInCurrentMonth = true
        ),
        modifier = Modifier
            .size(60.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(4.dp)
    )
}