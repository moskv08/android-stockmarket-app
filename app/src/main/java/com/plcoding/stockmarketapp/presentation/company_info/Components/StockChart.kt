package com.plcoding.stockmarketapp.presentation.company_info.Components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.plcoding.stockmarketapp.domain.model.IntradayInfo
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    stockInfos: List<IntradayInfo> = emptyList(),
    modifier: Modifier = Modifier,
    graphColor: Color = Color.DarkGray
) {
    val spacing = 100f
    val padding = 5

    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }

    val upperValue = remember(stockInfos) {
        (stockInfos.maxOfOrNull { it.close }?.plus(1))?.roundToInt()?: 0
    }

    val lowerValue = remember(stockInfos) {
        (stockInfos).minOfOrNull { it.close }?.toInt()?: 0
    }

    // Legacy Canvas: Draw text values
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx()}
        }
    }
    Canvas(modifier = modifier){

        // Define X-Axis
        val spacePerHour = (size.width - spacing) / stockInfos.size
        (0 until stockInfos.size - 1 step 2).forEach { index ->
            val info = stockInfos[index]
            val hour = info.date.hour

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + index * spacePerHour,
                    size.height - padding,
                    textPaint
                )
            }
        }

        // Define Y-Axis
        val priceStep = (upperValue - lowerValue) / 5f
        val staticValue = 30f

        (0..5).forEach { index ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + priceStep * index).toString(),
                    staticValue,
                    size.height - spacing - index * size.height / 5f,
                    textPaint
                )
            }
        }

        // Draw Chart
        var lastX = 0f
        val strokePath = Path().apply {
            val height = size.height
            for(index in stockInfos.indices) {
                val info = stockInfos[index]
                val nextInfo = stockInfos.getOrNull(index + 1) ?: stockInfos.last()

                // Calculate percentage values (0..1)
                val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + index * spacePerHour
                val y1 = height - spacing -(leftRatio * height).toFloat()
                val x2 = spacing + (index + 1) * spacePerHour
                val y2 = height - spacing - (leftRatio * height).toFloat()

                if(index == 0){
                    moveTo(x1, y1)
                }

                // Draw smooth lines between two values
                lastX = (x1 + x2) / 2f
                quadraticBezierTo(
                    x1, y1, lastX, (y1 + y2) / 2f
                )
            }
        }

        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )

        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}