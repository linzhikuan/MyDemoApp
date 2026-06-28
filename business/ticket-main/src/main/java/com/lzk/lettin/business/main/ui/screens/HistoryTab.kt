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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.model.parseNumbers
import com.lzk.lettin.business.main.ui.screens.components.NumberRow
import com.lzk.lettin.business.main.ui.vm.HistoryVM

@Composable
fun HistoryTab(type: LotteryType) {
    val vm: HistoryVM = hiltViewModel()
    val state by vm.state.collectAsState()

    LaunchedEffect(type) {
        vm.load(type, 50)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("最近 ${state.draws.size} 期开奖", style = MaterialTheme.typography.titleMedium)
            OutlinedButton(onClick = { vm.refresh(type) }) {
                Text("刷新")
            }
        }

        if (state.loading && state.draws.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.draws.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("暂无开奖数据")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.draws) { draw ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            ),
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    "第 ${draw.issueNo} 期",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Spacer(Modifier.padding(6.dp))
                                Text(
                                    draw.date,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            NumberRow(
                                frontNumbers = draw.frontNumbers.parseNumbers(),
                                backNumbers = draw.backNumbers.parseNumbers(),
                            )
                        }
                    }
                }
            }
        }
    }
}
