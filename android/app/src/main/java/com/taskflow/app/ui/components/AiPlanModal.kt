package com.taskflow.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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

@Composable
fun AiPlanModal(
    onDismiss: () -> Unit,
    onConfirm: (List<Task>) -> Unit
) {
    var step by remember { mutableStateOf(AiStep.FORM) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var generatedTasks by remember { mutableStateOf<List<Task>>(emptyList()) }

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
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("补充说明（选填）") },
                            minLines = 2
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            step = AiStep.LOADING
                            // 模拟 AI 生成
                            // 实际项目中调用 API
                        },
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
                    // 任务列表预览，可勾选
                },
                confirmButton = {
                    TextButton(onClick = { onConfirm(generatedTasks) }) {
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
