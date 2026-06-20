package com.lzk.lettin.business.main.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lzk.lettin.business.main.vm.DeviceControlVM
import com.lzk.lettin.business.main.vm.effect.DeviceControlSideEffect
import com.lzk.lettin.business.main.vm.event.DeviceControlEvent
import com.lzk.lettin.business.main.vm.state.AreaWithDevices
import com.lzk.lettin.business.main.vm.state.RoomWithAreas
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun deviceControlScreen(vm: DeviceControlVM = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()

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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
            item {
                Text(
                    text = "房间列表 (${state.roomWithAreas.size})",
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }

            items(
                items = state.roomWithAreas,
                key = { it.room.roomId },
            ) { roomWithAreas ->
                RoomCard(roomWithAreas = roomWithAreas)
            }

            if (state.roomWithAreas.isEmpty()) {
                item {
                    Text(
                        text = "暂无房间数据，下拉刷新获取",
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomCard(roomWithAreas: RoomWithAreas) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            Text(
                text = roomWithAreas.room.uname.ifEmpty { "房间 ${roomWithAreas.room.roomId}" },
            )

            Text(
                text = "区域: ${roomWithAreas.areas.size} | 设备: ${roomWithAreas.deviceCount}",
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )

            roomWithAreas.areas.forEachIndexed { index, areaWithDevices ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                AreaItem(areaWithDevices = areaWithDevices)
            }
        }
    }
}

@Composable
private fun AreaItem(areaWithDevices: AreaWithDevices) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = areaWithDevices.area.uname.ifEmpty { "区域 ${areaWithDevices.area.areaId}" })
        Text(text = "${areaWithDevices.devices.size}个设备")
    }
}
