package com.nguyenminhtuyen.todowork.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhtuyen.todowork.viewmodel.TodoViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Add


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(viewModel: TodoViewModel, modifier: Modifier = Modifier) {
    var showAddSheet by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Danh sách công việc", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                items(viewModel.todos) { todo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.toggleDone(todo.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (todo.isDone) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = todo.isDone,
                                onCheckedChange = { viewModel.toggleDone(todo.id) }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = todo.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (todo.isDone) MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.4f
                                )
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Nút thêm ở góc dưới
        FloatingActionButton(
            onClick = { showAddSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }

        // Bottom sheet để thêm todo
        if (showAddSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddSheet = false }
            ) {
                AddTodoScreen(
                    viewModel = viewModel,
                    onDone = { showAddSheet = false }
                )
            }
        }
    }
}