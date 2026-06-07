package com.lzk.lettin.business.main.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshSample(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val state = rememberPullToRefreshState()
    val infiniteTransition = rememberInfiniteTransition(label = "refresh")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "rotation",
    )

    val pullDistance = state.distanceFraction * 200
    val isPulling = state.distanceFraction > 0f

    Box(
        modifier =
            modifier.pullToRefresh(
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, pullDistance.roundToInt()) },
        ) {
            content()
        }
        if (isPulling || isRefreshing) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .offset { IntOffset(0, pullDistance.roundToInt()) },
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .size(36.dp)
                            .rotate(
                                if (state.distanceFraction > 0) {
                                    state.distanceFraction * 360
                                } else {
                                    rotation
                                },
                            ),
                )
            }
        }
    }
}
