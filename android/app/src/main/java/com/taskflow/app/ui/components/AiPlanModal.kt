package com.taskflow.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.service.AiService

@Composable
fun AiPlanModal(
    onDismiss: () -> Unit,
    onConfirm: (List<Task>) -> Unit
) {
    val aiService = remember { AiService() }
    var step by remember { mutableStateOf(AiStep.FORM) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var generatedTasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    val selectedTasks = remember { mutableStateListOf<Task>() }

    when (step) {
        AiStep.FORM -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("AI 智能规划") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("任务目标") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("补充说明（选填）") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { step = AiStep.LOADING },
                        enabled = title.isNotBlank()
                    ) {
                        Text("开始规划")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                }
            )
        }

        AiStep.LOADING -> {
            LaunchedEffect(Unit) {
                val messages = listOf("user" to (title + if (description.isNotBlank()) "：$description" else ""))
                val response = aiService.chat(messages, com.taskflow.app.domain.service.AiConfigData())
                generatedTasks = aiService.parseTasksFromResponse(response)
                selectedTasks.clear()
                selectedTasks.addAll(generatedTasks)
                step = AiStep.PREVIEW
            }
            AlertDialog(
                onDismissRequest = {},
                title = { Text("AI 正在规划...") },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("正在生成任务清单...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }

        AiStep.PREVIEW -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("预览 (${generatedTasks.size} 项)") },
                text = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(generatedTasks) { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (selectedTasks.contains(task)) {
                                            selectedTasks.remove(task)
                                        } else {
                                            selectedTasks.add(task)
                                        }
                                    }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = if (selectedTasks.contains(task)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    task.description?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (task.children.isNotEmpty()) {
                                        task.children.forEach { child ->
                                            Text(
                                                text = "  • ${child.title}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { onConfirm(selectedTasks.toList()) },
                        enabled = selectedTasks.isNotEmpty()
                    ) {
                        Text("确认保存")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { step = AiStep.FORM }) {
                        Text("返回修改")
                    }
                }
            )
        }
    }
}

enum class AiStep {
    FORM,
    LOADING,
    PREVIEW
}
