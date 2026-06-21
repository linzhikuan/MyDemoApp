package com.lzk.lettin.business.main.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.lettin.business.main.component.RefreshSample
import com.lzk.lettin.business.main.vm.HomeVM
import com.lzk.lettin.business.main.vm.effect.HomeUiSideEffect
import com.lzk.lettin.business.main.vm.event.HomeUiEvent
import com.lzk.lettin.business.main.vm.state.HomeUiState

@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onSettingClick: (LettinGatewayInfo) -> Unit,
    onDeviceControlClick: (LettinGatewayInfo) -> Unit,
) {
    val vm: HomeVM = hiltViewModel()
    val state by vm.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.sideEffect.collect {
            when (it) {
                is HomeUiSideEffect.ShowToast ->
                    Toast
                        .makeText(context, it.msg, Toast.LENGTH_LONG)
                        .show()
            }
        }
    }
    UpdateHomeUi(onLoginClick, onSettingClick, state, vm::onEvent, onDeviceControlClick)
}

@Composable
private fun UpdateHomeUi(
    onLoginClick: () -> Unit,
    onSettingClick: (LettinGatewayInfo) -> Unit,
    state: HomeUiState,
    event: (HomeUiEvent) -> Unit,
    onDeviceControlClick: (LettinGatewayInfo) -> Unit,
) {
    val onRefresh = {
        event(HomeUiEvent.FindHq)
    }
    RefreshSample(state.isFindingHq, onRefresh) {
        ContentView(
            onLoginClick,
            onSettingClick,
            state.gatewayList ?: listOf(),
            event,
            onDeviceControlClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentView(
    onLoginClick: () -> Unit,
    onSettingClick: (LettinGatewayInfo) -> Unit,
    hqDataList: List<LettinGatewayInfo>,
    event: (HomeUiEvent) -> Unit,
    onDeviceControlClick: (LettinGatewayInfo) -> Unit,
) {
    val size = hqDataList.size
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(hqDataList.size) {
                val hqData = hqDataList[it]
                ListItem({ HqItem(hqData, onSettingClick, onDeviceControlClick) })
            }
        }
        if (size == 0) {
            Text(
                text = "没有设备",
                modifier =
                    Modifier.clickable {
                        onLoginClick.invoke()
                    },
            )
        }
    }
}

@Composable
private fun HqItem(
    hqData: LettinGatewayInfo,
    onSettingClick: (LettinGatewayInfo) -> Unit,
    onDeviceControlClick: (LettinGatewayInfo) -> Unit,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(1f), text = "name:${hqData.name}")
            Button(onClick = {
                onDeviceControlClick(hqData)
            }) {
                Text(text = "控制")
            }
            Button(onClick = {
                onSettingClick(hqData)
            }) {
                Text(text = "连接")
            }
        }
        HorizontalDivider(color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun HqItemPreview() {
    HqItem(
        LettinGatewayInfo(
            name = "haha",
            mac = "haha",
            ip = "",
            port = 0,
        ),
        onSettingClick = TODO(),
        onDeviceControlClick = TODO(),
    )
}
