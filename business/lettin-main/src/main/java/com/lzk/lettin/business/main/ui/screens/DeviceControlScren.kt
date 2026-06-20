package com.lzk.lettin.business.main.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lzk.lettin.business.main.vm.DeviceControlVM
import com.lzk.lettin.business.main.vm.effect.DeviceControlSideEffect
import com.lzk.lettin.business.main.vm.event.DeviceControlEvent
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun deviceControlScreen(vm: DeviceControlVM = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        vm.sideEffect.collect {
            when (it) {
                is DeviceControlSideEffect.ShowToast ->
                    Toast
                        .makeText(
                            context,
                            it.msg,
                            Toast.LENGTH_LONG,
                        ).show()
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            state.gatewayInfo?.let { info ->
                coroutineScope.launch {
                    vm.onEvent(
                        DeviceControlEvent.SyncGwTable(
                            gwId = info.mac,
                            ip = info.ip,
                        ),
                    )
                }
            }
        },
        state = pullToRefreshState,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    state.gatewayInfo?.let { info ->
                        vm.onEvent(
                            DeviceControlEvent.SyncGwTable(
                                gwId = info.mac,
                                ip = info.ip,
                            ),
                        )
                    }
                }) {
                    Text(text = "同步数据")
                }
            }
            HorizontalDivider(color = Color.Gray)

            // 添加足够的空间使内容可滚动，便于测试下拉刷新
            Spacer(modifier = Modifier.height(600.dp))
            Text(
                text = "下拉刷新测试区域",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
