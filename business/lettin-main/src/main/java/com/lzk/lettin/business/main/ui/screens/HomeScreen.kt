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
import com.lzk.lettin.business.main.component.RefreshSample1
import com.lzk.lettin.business.main.vm.HomeVM
import com.lzk.lettin.business.main.vm.effect.HomeUiSideEffect
import com.lzk.lettin.business.main.vm.event.HomeUiEvent
import com.lzk.lettin.business.main.vm.state.HomeUiState

@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onSettingClick: (LettinGatewayInfo) -> Unit,
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
    UpdateHomeUi(onLoginClick, onSettingClick, state, vm::onEvent)
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun UpdateHomeUi(
    onLoginClick: () -> Unit,
    onSettingClick: (LettinGatewayInfo) -> Unit,
    state: HomeUiState,
    event: (HomeUiEvent) -> Unit,
) {
    val onRefresh = {
        event(HomeUiEvent.FindHq)
    }
    RefreshSample1(state.isFindingHq, onRefresh) {
        ContentView(onLoginClick, onSettingClick, state.gatewayList ?: listOf(), event)
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentView(
    onLoginClick: () -> Unit,
    onSettingClick: (LettinGatewayInfo) -> Unit,
    hqDataList: List<LettinGatewayInfo>,
    event: (HomeUiEvent) -> Unit,
) {
    val size = hqDataList.size
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(hqDataList.size) {
                val hqData = hqDataList[it]
                ListItem({ HqItem(hqData, onSettingClick) })
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

@Suppress("ktlint:standard:function-naming")
@Composable
private fun HqItem(
    hqData: LettinGatewayInfo,
    onSettingClick: (LettinGatewayInfo) -> Unit,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(1f), text = "name:${hqData.name}")
            Button(onClick = {
                onSettingClick(hqData)
            }) {
                Text(text = "设置")
            }
        }
        HorizontalDivider(color = Color.Gray)
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}
