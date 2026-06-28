package com.lzk.lettin.business.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.ui.screens.components.NumberBall
import com.lzk.lettin.business.main.ui.screens.components.NumberRow
import com.lzk.lettin.business.main.ui.vm.GeneratedTicket
import com.lzk.lettin.business.main.ui.vm.PickMode
import com.lzk.lettin.business.main.ui.vm.PickToolVM

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PickToolTab(type: LotteryType) {
    val vm: PickToolVM = hiltViewModel()
    val state by vm.state.collectAsState()

    LaunchedEffect(type) {
        vm.resetForType(type)
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(
                    "选号说明",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "双色球: ${type.frontRange.first}-${type.frontRange.last} 选 ${type.frontCount} + ${type.backRange.first}-${type.backRange.last} 选 ${type.backCount}。",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    "机选无任何科学性保证，仅为娱乐演示。请理性购彩，未成年人禁止购彩。",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        // 模式切换
        Text("模式", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            PickMode.values().forEach { m ->
                FilterChip(
                    selected = state.mode == m,
                    onClick = { vm.changeMode(m) },
                    label = { Text(m.label) },
                )
            }
        }

        // 号码选择区（非 RANDOM 模式下显示）
        if (state.mode != PickMode.RANDOM) {
            NumberPicker(
                title = "前区 (${state.selectedFront.size} 已选)",
                range = type.frontRange,
                selected = state.selectedFront,
                color = Color(0xFFE53935),
                onToggle = vm::toggleFront,
            )
            NumberPicker(
                title = "后区 (${state.selectedBack.size} 已选)",
                range = type.backRange,
                selected = state.selectedBack,
                color = Color(0xFF1E88E5),
                onToggle = vm::toggleBack,
            )
        }

        // 生成按钮
        Row {
            when (state.mode) {
                PickMode.RANDOM -> Button(onClick = { vm.generateRandom(5) }) { Text("随机生成 5 注") }
                PickMode.MANUAL -> Button(onClick = { vm.confirmManual() }) { Text("确认自选（前 ${type.frontCount} 后 ${type.backCount}）") }
                PickMode.DANTUO -> Button(onClick = { vm.generateDanTuo() }) { Text("以选中号码为胆码") }
                PickMode.COMPOUND -> Button(onClick = { vm.generateCompound(8) }) { Text("复式：随机生成组合") }
            }
            Spacer(Modifier.padding(6.dp))
            OutlinedButton(onClick = { vm.resetForType(type) }) { Text("清空") }
        }

        state.message?.let {
            Text(it, color = Color(0xFF2E7D32))
        }

        Spacer(Modifier.height(8.dp))
        Text(
            "生成的号码",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        val list = state.generated
        if (list == null) {
            Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                Text("点击上方按钮开始")
            }
        } else if (list.isEmpty()) {
            Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.height((120 + list.size * 80).dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(list) { t ->
                    GeneratedCard(ticket = t) { vm.saveTicket(t) }
                }
            }
        }
    }
}

@Composable
private fun GeneratedCard(
    ticket: GeneratedTicket,
    onSave: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(12.dp)) {
            NumberRow(frontNumbers = ticket.front, backNumbers = ticket.back)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onSave) { Text("保存到我的选号") }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NumberPicker(
    title: String,
    range: IntRange,
    selected: Set<Int>,
    color: Color,
    onToggle: (Int) -> Unit,
) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        for (n in range) {
            NumberBall(
                num = n,
                isFront = (color == Color(0xFFE53935)),
                highlight = n in selected,
                onClick = { onToggle(n) },
            )
        }
    }
}
