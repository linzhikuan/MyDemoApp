package com.lzk.lettin.business.main.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 号码球 UI 组件。
 * - 双色球：前区红色、后区蓝色
 * - 大乐透：前区红色、后区蓝色
 */
@Composable
fun NumberBall(
    num: Int,
    isFront: Boolean = true,
    size: Dp = 32.dp,
    highlight: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val bgColor = when {
        isFront -> Color(0xFFE53935)
        else -> Color(0xFF1E88E5)
    }
    val baseBg = if (highlight) bgColor else Color(0xFFE0E0E0)
    val borderColor = if (highlight) Color(0xFFFFC107) else bgColor
    Box(
        modifier = modifier
            .size(size)
            .background(baseBg, CircleShape)
            .border(2.dp, borderColor, CircleShape)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = num.toString().padStart(2, '0'),
            color = if (highlight) Color.White else Color(0xFF212121),
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.45).sp,
        )
    }
}

@Composable
fun NumberRow(
    frontNumbers: List<Int>,
    backNumbers: List<Int>,
    size: Dp = 30.dp,
    modifier: Modifier = Modifier,
    gap: Dp = 6.dp,
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        frontNumbers.forEach {
            NumberBall(num = it, isFront = true, size = size)
        }
        if (backNumbers.isNotEmpty()) {
            Text("|", color = MaterialTheme.colorScheme.onSurfaceVariant)
            backNumbers.forEach {
                NumberBall(num = it, isFront = false, size = size)
            }
        }
    }
}
