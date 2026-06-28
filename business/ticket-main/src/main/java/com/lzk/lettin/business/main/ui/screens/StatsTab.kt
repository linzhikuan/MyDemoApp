package com.lzk.lettin.business.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.ui.screens.components.BarChart
import com.lzk.lettin.business.main.ui.screens.components.LineChart
import com.lzk.lettin.business.main.ui.screens.components.NumberListChip
import com.lzk.lettin.business.main.ui.vm.StatsVM

@Composable
fun StatsTab(type: LotteryType) {
    val vm: StatsVM = hiltViewModel()
    val state by vm.state.collectAsState()

    LaunchedEffect(type) {
        vm.load(type, 30)
    }

    val result = state.result
    if (state.loading || result == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            "基于最近 ${result.drawCount} 期开奖",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        BarChart(
            title = "前区号码频率",
            values = result.frontFrequency,
            range = type.frontRange,
            color = Color(0xFFE53935),
        )
        BarChart(
            title = "后区号码频率",
            values = result.backFrequency,
            range = type.backRange,
            color = Color(0xFF1E88E5),
        )
        LineChart(
            title = "和值趋势（由旧到新）",
            values = result.sumTrend,
            color = Color(0xFF43A047),
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Column(Modifier.padding(12.dp)) {
                Text("冷 / 热号", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                NumberListChip("前区热号", result.hotFront)
                NumberListChip("前区冷号", result.coldFront)
                NumberListChip("后区热号", result.hotBack)
                NumberListChip("后区冷号", result.coldBack)
                Spacer(Modifier.height(8.dp))
                Row {
                    Text(
                        "奇数占比: ${"%.1f".format(result.oddRatio * 100)}%",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Column(Modifier.padding(12.dp)) {
                Text("遗漏值（越大越久未出）", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                val frontTopMiss = result.frontMiss.entries.sortedByDescending { it.value }.take(10)
                Text("前区 Top-10 遗漏: " + frontTopMiss.joinToString(" , ") { "${it.key}(${it.value})" })
                Spacer(Modifier.height(4.dp))
                val backTopMiss = result.backMiss.entries.sortedByDescending { it.value }.take(6)
                Text("后区 Top 遗漏: " + backTopMiss.joinToString(" , ") { "${it.key}(${it.value})" })
            }
        }
    }
}
