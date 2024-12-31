package com.example.pedometer.feature.easteregg.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.stringResource
import com.example.pedometer.R

@Composable
fun HeartCanvas(
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {
    val pathData = stringResource(id = R.string.heart_path)
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height


        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val heartSize = minOf(canvasWidth, canvasHeight) * scale


        drawSvg(
            centerX = centerX,
            centerY = centerY,
            size = heartSize,
            color = Color.Red,
            svgPathData = pathData
        )
    }
}

fun DrawScope.drawSvg(
    centerX: Float,
    centerY: Float,
    size: Float,
    svgPathData: String,
    color: Color
) {
    val svgPath = PathParser().parsePathString(svgPathData).toPath()
    val pathBounds = svgPath.getBounds()
    val scale = size / pathBounds.width

    withTransform({
        scale(scale, scale)
        translate(
            left = centerX - pathBounds.width / 2,
            top = centerY - pathBounds.height / 2
        )
    }) {
        drawPath(path = svgPath, color = color)
    }
}