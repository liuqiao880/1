package com.taskflow.app.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.TaskPriority
import com.taskflow.app.domain.model.TaskStatus
import com.taskflow.app.ui.theme.BluePriority
import com.taskflow.app.ui.theme.RedPriority
import com.taskflow.app.ui.theme.YellowPriority
import com.taskflow.app.util.DateUtils

@Composable
fun TaskList(
    groupedTasks: List<Pair<String, List<Task>>>,
    expandedParents: Set<Int>,
    multiSelectMode: Boolean,
    selectedTasks: Set<Int>,
    onTaskClick: (Task) -> Unit,
    onTaskChecked: (Int) -> Unit,
    onTaskLongPress: (Task) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onStartPomodoro: (Task) -> Unit,
    onChildClick: (Task) -> Unit,
    onChildChecked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (groupedTasks.isEmpty()) {
        EmptyState()
        return
    }

    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        groupedTasks.forEach { (groupName, tasks) ->
            item(key = "header_$groupName") {
                TaskGroupHeader(title = groupName, count = tasks.size)
            }
            item(key = "spacer_$groupName") {
                Spacer(modifier = Modifier.height(8.dp))
            }
            itemsIndexed(tasks, key = { _, task -> task.id }) { index, task ->
                TaskItem(
                    task = task,
                    isExpanded = expandedParents.contains(task.id),
                    isSelected = selectedTasks.contains(task.id),
                    multiSelectMode = multiSelectMode,
                    selectedTasks = selectedTasks,
                    onClick = { onTaskClick(task) },
                    onCheckedChange = { onTaskChecked(task.id) },
                    onLongPress = { onTaskLongPress(task) },
                    onDelete = { onDeleteTask(task.id) },
                    onStartPomodoro = { onStartPomodoro(task) },
                    onChildClick = onChildClick,
                    onChildChecked = onChildChecked
                )
            }
            item(key = "bottom_spacer_$groupName") {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TaskGroupHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$count 项",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    isExpanded: Boolean,
    isSelected: Boolean,
    multiSelectMode: Boolean,
    selectedTasks: Set<Int>,
    onClick: () -> Unit,
    onCheckedChange: () -> Unit,
    onLongPress: () -> Unit,
    onDelete: () -> Unit,
    onStartPomodoro: () -> Unit,
    onChildClick: (Task) -> Unit,
    onChildChecked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val priorityColor = when (task.priority) {
        TaskPriority.HIGH -> RedPriority
        TaskPriority.MEDIUM -> YellowPriority
        TaskPriority.LOW -> BluePriority
    }

    val isCompleted = task.status == TaskStatus.COMPLETED
    val rotation by animateFloatAsState(if (isExpanded) 180f else 0f, label = "expand")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority indicator
            Box(
                modifier = Modifier
                    .size(4.dp, 48.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(priorityColor)
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Checkbox
            CheckBox(
                checked = if (multiSelectMode) isSelected else isCompleted,
                onCheckedChange = { onCheckedChange() }
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (isCompleted && !multiSelectMode) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isCompleted && !multiSelectMode)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    task.dueDate?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = DateUtils.formatDate(it),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    PriorityBadge(priority = task.priority)

                    if (task.aiGenerated) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "AI",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (!multiSelectMode) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (task.pomodoroCount > 0)
                                        RedPriority.copy(alpha = 0.12f)
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                )
                                .clickable { onStartPomodoro() }
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = null,
                                    modifier = Modifier.size(11.dp),
                                    tint = if (task.pomodoroCount > 0) RedPriority
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (task.pomodoroCount > 0) "${task.pomodoroCount}" else "开始",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = if (task.pomodoroCount > 0) RedPriority
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (task.children.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LinearProgressIndicator(
                                progress = { task.progress },
                                modifier = Modifier.width(60.dp),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${(task.progress * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Expand arrow
            if (task.children.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Children
        if (task.children.isNotEmpty() && isExpanded) {
            Column(
                modifier = Modifier
                    .padding(start = 48.dp, end = 16.dp, bottom = 12.dp)
            ) {
                task.children.forEach { child ->
                    SubTaskItem(
                        task = child,
                        isSelected = selectedTasks.contains(child.id),
                        multiSelectMode = multiSelectMode,
                        onClick = { onChildClick(child) },
                        onCheckedChange = { onChildChecked(child.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SubTaskItem(
    task: Task,
    isSelected: Boolean,
    multiSelectMode: Boolean,
    onClick: () -> Unit,
    onCheckedChange: () -> Unit
) {
    val isCompleted = task.status == TaskStatus.COMPLETED

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckBox(
            checked = if (multiSelectMode) isSelected else isCompleted,
            onCheckedChange = { onCheckedChange() },
            size = 18.dp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = if (isCompleted && !multiSelectMode) TextDecoration.LineThrough else TextDecoration.None,
                color = if (isCompleted && !multiSelectMode)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            task.dueDate?.let {
                Text(
                    text = DateUtils.formatDate(it),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CheckBox(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                if (checked) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            .clickable { onCheckedChange() },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

@Composable
fun PriorityBadge(priority: TaskPriority) {
    val color = when (priority) {
        TaskPriority.HIGH -> RedPriority
        TaskPriority.MEDIUM -> YellowPriority
        TaskPriority.LOW -> BluePriority
    }
    val label = when (priority) {
        TaskPriority.HIGH -> "紧急"
        TaskPriority.MEDIUM -> "普通"
        TaskPriority.LOW -> "低优"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Icon(
            Icons.Default.Flag,
            contentDescription = null,
            modifier = Modifier.size(10.dp),
            tint = color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
