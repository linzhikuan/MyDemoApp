package com.lzk.lettin.business.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lzk.lettin.business.main.data.model.LotteryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLotteryClicked: (LotteryType) -> Unit,
    onMyTicketsClicked: () -> Unit,
    onSettingClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("彩票预测", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onMyTicketsClicked) {
                        Icon(Icons.Default.Star, contentDescription = "我的选号")
                    }
                    IconButton(onClick = onSettingClicked) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
        ) {
            Text(
                "选择要分析的彩种",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(LotteryType.values().size) { idx ->
                    val type = LotteryType.values()[idx]
                    LotteryTypeCard(
                        type = type,
                        onClick = { onLotteryClicked(type) },
                    )
                }
            }
        }
    }
}

@Composable
private fun LotteryTypeCard(
    type: LotteryType,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    when (type) {
                        LotteryType.SSQ -> Color(0xFFFFF3E0)
                        LotteryType.DLT -> Color(0xFFE3F2FD)
                    },
            ),
    ) {
        Box(Modifier.fillMaxSize().padding(16.dp)) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    type.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF3E2723),
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "前区 ${type.frontCount} 个 (${type.frontRange.first}-${type.frontRange.last})  " +
                        "后区 ${type.backCount} 个 (${type.backRange.first}-${type.backRange.last})",
                    fontSize = 13.sp,
                    color = Color(0xFF5D4037),
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "点击查看历史数据 / 统计 / 预测 / 选号",
                    fontSize = 12.sp,
                    color = Color(0xFF6D4C41),
                )
            }
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.align(Alignment.BottomEnd),
                tint = Color(0xFFEF6C00),
            )
        }
    }
}
