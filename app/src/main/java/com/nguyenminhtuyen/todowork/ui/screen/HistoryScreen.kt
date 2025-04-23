package com.nguyenminhtuyen.todowork.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhtuyen.todowork.viewmodel.TodoViewModel

@Composable
fun HistoryScreen(viewModel: TodoViewModel, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Lịch sử hoàn thành", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        for (todo in viewModel.history) {
            Text("• " + todo.title)
        }
    }
}