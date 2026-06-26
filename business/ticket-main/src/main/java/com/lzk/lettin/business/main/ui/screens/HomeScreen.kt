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
import com.lzk.lettin.business.main.vm.HomeVM
import com.lzk.lettin.business.main.vm.effect.HomeUiSideEffect
import com.lzk.lettin.business.main.vm.event.HomeUiEvent
import com.lzk.lettin.business.main.vm.state.HomeUiState

@Composable
fun HomeScreen() {
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
    UpdateHomeUi()
}

@Composable
private fun UpdateHomeUi() {
}
