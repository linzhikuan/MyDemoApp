package com.lzk.lettin.business.main.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.lzk.common.bean.device.ConnectionState
import com.lzk.lettin.business.main.vm.DeviceControlVM
import com.lzk.lettin.business.main.vm.effect.DeviceControlSideEffect
import com.lzk.lettin.business.main.vm.event.DeviceControlEvent

@Suppress("ktlint:standard:function-naming")
@Composable
fun deviceControlScreen(vm: DeviceControlVM = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.sideEffect.collect {
            when (it) {
                is DeviceControlSideEffect.ShowToast ->
                    Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
            }
        }
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(1f), text = "name:${state.gatewayInfo?.name}")
            Button(onClick = {
                state.gatewayInfo?.let { info ->
                    vm.onEvent(
                        DeviceControlEvent.Connect(
                            ip = info.ip,
                            port = info.port,
                        ),
                    )
                }
            }) {
                Text(text = if (state.connectionState is ConnectionState.Connected) "已连接" else "连接")
            }

            Button(onClick = {
                state.gatewayInfo?.let { info ->
                    vm.onEvent(
                        DeviceControlEvent.Query(
                            gwId = info.mac,
                            ip = info.ip,
                        ),
                    )
                }
            }) {
                Text(text = "查询")
            }
        }
        HorizontalDivider(color = Color.Gray)
    }
}
