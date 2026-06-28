package com.lzk.lettin.business.main.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lzk.lettin.business.main.data.model.LotteryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotteryDetailScreen(
    type: LotteryType,
    onBack: () -> Unit,
) {
    val tabs = remember { listOf("历史开奖", "数据统计", "预测推荐", "选号工具") }
    var selected by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${type.displayName} · 分析") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(padding),
        ) {
            TabRow(selectedTabIndex = selected) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selected == index,
                        onClick = { selected = index },
                        text = { Text(title, style = MaterialTheme.typography.bodyMedium) },
                    )
                }
            }
            when (selected) {
                0 -> HistoryTab(type = type)
                1 -> StatsTab(type = type)
                2 -> PredictTab(type = type)
                3 -> PickToolTab(type = type)
            }
        }
    }
}
