package com.lzk.lettin.business.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lzk.common.bean.device.LettinGatewayInfo

@Suppress("ktlint:standard:function-naming")
@Composable
fun deviceControlScreen(hqData: LettinGatewayInfo) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(modifier = Modifier.weight(1f), text = "name:${hqData.name}")
            Button(onClick = {
            }) {
                Text(text = "连接")
            }
        }
        HorizontalDivider(color = Color.Gray)
    }
}
