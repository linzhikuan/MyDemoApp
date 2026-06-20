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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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
                    Toast
                        .makeText(
                            context,
                            it.msg,
                            Toast.LENGTH_LONG,
                        ).show()
            }
        }
    }

    Column {
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
    }
}
