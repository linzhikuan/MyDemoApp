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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.lzk.lettin.business.main.domain.usecase.PredictedTicket
import com.lzk.lettin.business.main.ui.screens.components.NumberRow
import com.lzk.lettin.business.main.ui.vm.PredictVM

@Composable
fun PredictTab(type: LotteryType) {
    val vm: PredictVM = hiltViewModel()
    val state by vm.state.collectAsState()

    LaunchedEffect(type) {
        vm.load(type)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(
                    "预测仅为娱乐演示",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFBF360C),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "真实彩票号码为独立随机事件，任何算法都无法提升中奖概率。请理性对待，未成年人禁止购彩。",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("推荐号码", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(6.dp))
            OutlinedButton(onClick = { vm.load(type) }) {
                Text("换一批")
            }
        }

        when {
            state.loading -> {
                Box(Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.message != null -> Text(state.message!!)
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.list) { t ->
                        PredictedTicketCard(
                            ticket = t,
                            onSave = { vm.saveTicket(type, t) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PredictedTicketCard(
    ticket: PredictedTicket,
    onSave: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(ticket.note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            NumberRow(frontNumbers = ticket.front, backNumbers = ticket.back)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onSave) {
                    Text("保存到我的选号")
                }
            }
        }
    }
}
