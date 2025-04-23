package com.nguyenminhtuyen.todowork.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhtuyen.todowork.viewmodel.TodoViewModel

@Composable
fun AddTodoScreen(viewModel: TodoViewModel, onDone: () -> Unit) {
    var title by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Thêm công việc", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Nội dung") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.addTodo(title)
                title = ""
                onDone()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Thêm")
        }
    }
}
