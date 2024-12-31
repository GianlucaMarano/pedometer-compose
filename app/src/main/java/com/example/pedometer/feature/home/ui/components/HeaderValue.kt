package com.example.pedometer.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.pedometer.data.Timeframe
import com.example.pedometer.R
import com.example.pedometer.ui.theme.Typography
import com.example.pedometer.ui.theme.White

@Composable
fun HeaderValue(
    value: Int,
    period: Timeframe,
    unit: String = "",
    style: TextStyle = Typography.displayLarge,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
        modifier = modifier
    ) {
        Row (verticalAlignment = Alignment.Bottom){
            Text(
                text = "$value",
                style = style,
                color = White,
                modifier = Modifier.alignBy(FirstBaseline),
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelLarge,
                color = White,
                modifier = Modifier.alignBy(FirstBaseline)
            )
        }

        Text(
            text = when (period) {
                Timeframe.DAY -> stringResource(R.string.today)
                Timeframe.WEEK -> stringResource(R.string.this_week)
                Timeframe.MONTH -> stringResource(R.string.this_month)
            },
            style =  MaterialTheme.typography.bodyMedium,
            color = White,

        )


    }
}

@Preview
@Composable
private fun HeaderValuePreview() {
    HeaderValue(value = 100, period = Timeframe.MONTH, unit = stringResource(R.string.km))
}