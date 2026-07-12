package com.taskflow.app.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.taskflow.app.ui.theme.NewspaperRed
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
                Spacer(modifier = Modifier.height(4.dp))
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
                    onChildChecked = onChildChecked,
                    showTopDivider = index > 0
                )
            }
            item(key = "bottom_spacer_$groupName") {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TaskGroupHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$count",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
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
    showTopDivider: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isCompleted = task.status == TaskStatus.COMPLETED
    val rotation by animateFloatAsState(if (isExpanded) 180f else 0f, label = "expand")

    Column(modifier = modifier.fillMaxWidth().animateContentSize()) {
        if (showTopDivider) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckBox(
                checked = if (multiSelectMode) isSelected else isCompleted,
                onCheckedChange = { onCheckedChange() }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (isCompleted && !multiSelectMode) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isCompleted && !multiSelectMode)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    task.dueDate?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
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
                                tint = NewspaperRed
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "AI",
                                style = MaterialTheme.typography.labelSmall,
                                color = NewspaperRed,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (!multiSelectMode && task.pomodoroCount > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = NewspaperRed
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${task.pomodoroCount}",
                                style = MaterialTheme.typography.labelSmall,
                                color = NewspaperRed,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

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

        if (task.children.isNotEmpty() && isExpanded) {
            Column(
                modifier = Modifier
                    .padding(start = 40.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                )
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
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckBox(
            checked = if (multiSelectMode) isSelected else isCompleted,
            onCheckedChange = { onCheckedChange() },
            size = 20.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
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
                else Color.Transparent
            )
            .border(
                width = 2.dp,
                color = if (checked) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                shape = CircleShape
            )
            .clickable { onCheckedChange() },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * 0.55f)
            )
        }
    }
}

@Composable
fun PriorityBadge(priority: TaskPriority) {
    val color = when (priority) {
        TaskPriority.HIGH -> NewspaperRed
        TaskPriority.MEDIUM -> Color(0xFFB8860B)
        TaskPriority.LOW -> Color(0xFF4682B4)
    }
    val label = when (priority) {
        TaskPriority.HIGH -> "紧急"
        TaskPriority.MEDIUM -> "普通"
        TaskPriority.LOW -> "低优"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
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
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}
