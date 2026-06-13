package com.lzk.lettin.business.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.lettin.business.main.vm.DeviceControlVM
import com.lzk.lettin.business.main.vm.event.DeviceControlEvent

@Suppress("ktlint:standard:function-naming")
@Composable
fun deviceControlScreen(
    hqData: LettinGatewayInfo,
    vm: DeviceControlVM =
        viewModel(
            key = hqData.mac,
        ) {
            DeviceControlVM(hqData)
        },
) {
    val state by vm.state.collectAsState()

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(1f), text = "name:${state.gatewayInfo.name}")
            Button(onClick = {
                vm.onEvent(
                    DeviceControlEvent.Connect(
                        ip = state.gatewayInfo.ip,
                        port = state.gatewayInfo.port,
                    ),
                )
            }) {
                Text(text = if (state.isConnecting) "连接中..." else "连接")
            }
        }
        HorizontalDivider(color = Color.Gray)
    }
}
