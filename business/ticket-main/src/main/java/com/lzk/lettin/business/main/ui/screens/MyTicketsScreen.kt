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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lzk.lettin.business.main.data.model.SavedTicket
import com.lzk.lettin.business.main.data.model.parseNumbers
import com.lzk.lettin.business.main.data.repository.LotteryRepository
import com.lzk.lettin.business.main.ui.screens.components.NumberRow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyTicketsVM @Inject constructor(
    private val repository: LotteryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(emptyList<SavedTicket>())
    val state: StateFlow<List<SavedTicket>> = _state.asStateFlow()

    fun load() {
        viewModelScope.launch {
            repository.observeSavedTickets().collect { list ->
                _state.value = list
            }
        }
    }

    fun delete(ticket: SavedTicket) {
        viewModelScope.launch {
            repository.deleteTicket(ticket)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketsScreen(onBack: () -> Unit) {
    val vm: MyTicketsVM = hiltViewModel()
    val list by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.load() }

    val dateFormat = SimpleDateFormat("MM-dd HH:mm")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的选号") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(12.dp).fillMaxSize(),
        ) {
            if (list.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("还没有保存的号码")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(items = list) { saved ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(saved.type, style = MaterialTheme.typography.titleSmall)
                                    Spacer(Modifier.padding(6.dp))
                                    Text(saved.source, style = MaterialTheme.typography.bodySmall)
                                    Spacer(Modifier.padding(6.dp))
                                    Text(
                                        dateFormat.format(Date(saved.createdAt)),
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                NumberRow(
                                    frontNumbers = saved.frontNumbers.parseNumbers(),
                                    backNumbers = saved.backNumbers.parseNumbers(),
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    IconButton(onClick = { vm.delete(saved) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "删除")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
