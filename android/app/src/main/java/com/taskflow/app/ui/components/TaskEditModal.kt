package com.taskflow.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.TaskPriority

@Composable
fun TaskEditModal(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    var title by remember(task) { mutableStateOf(task.title) }
    var description by remember(task) { mutableStateOf(task.description ?: "") }
    var priority by remember(task) { mutableStateOf(task.priority) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("编辑任务") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("任务标题") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("描述") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Priority selector
                Text(
                    text = "优先级",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                PrioritySelector(
                    selected = priority,
                    onSelected = { priority = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        task.copy(
                            title = title,
                            description = description.ifBlank { null },
                            priority = priority
                        )
                    )
                },
                enabled = title.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun PrioritySelector(
    selected: TaskPriority,
    onSelected: (TaskPriority) -> Unit
) {
    // 简化的优先级选择器
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TaskPriority.values().forEach { priority ->
            val isSelected = selected == priority
            TextButton(
                onClick = { onSelected(priority) },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = when (priority) {
                        TaskPriority.HIGH -> "紧急"
                        TaskPriority.MEDIUM -> "普通"
                        TaskPriority.LOW -> "低优"
                    }
                )
            }
        }
    }
}
