package com.lzk.lettin.business.main.ui.screens.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(
    title: String,
    values: Map<Int, Int>,
    range: IntRange,
    color: Color = Color(0xFFE53935),
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        val maxVal = values.values.maxOrNull()?.coerceAtLeast(1) ?: 1
        Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
            val count = range.last - range.first + 1
            val barWidth = size.width / count * 0.75f
            val gap = size.width / count * 0.25f
            val maxHeightPx = size.height - 8f
            for (i in 0 until count) {
                val num = range.first + i
                val v = values[num] ?: 0
                val left = i * (barWidth + gap) + gap / 2
                val barHeight = (v.toFloat() / maxVal) * maxHeightPx
                drawRect(
                    color = color,
                    topLeft = Offset(left, size.height - barHeight),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                )
            }
        }
    }
}

@Composable
fun LineChart(
    title: String,
    values: List<Int>,
    color: Color = Color(0xFF1E88E5),
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        if (values.isEmpty()) {
            Text("（无数据）")
            return@Column
        }
        val minV = values.minOrNull() ?: 0
        val maxV = values.maxOrNull() ?: 0
        val diff = (maxV - minV).coerceAtLeast(1)
        Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
            val stepX = size.width / (values.size - 1).coerceAtLeast(1)
            val maxHeightPx = size.height - 8f
            var prev: Offset? = null
            for (i in values.indices) {
                val normalized = (values[i] - minV).toFloat() / diff
                val y = size.height - 4 - normalized * maxHeightPx
                val x = i * stepX + stepX / 2
                val curr = Offset(x, y)
                if (prev != null) {
                    drawLine(color = color, start = prev, end = curr, strokeWidth = 3f)
                }
                prev = curr
            }
        }
    }
}

@Composable
fun NumberListChip(label: String, nums: List<Int>) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(label, fontWeight = FontWeight.Medium, modifier = Modifier.width(60.dp))
        Text(if (nums.isEmpty()) "—" else nums.sorted().joinToString(", "))
    }
}
