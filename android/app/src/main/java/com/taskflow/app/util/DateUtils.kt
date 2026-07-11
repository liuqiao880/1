package com.taskflow.app.util

import java.util.Calendar
import java.util.concurrent.TimeUnit

object DateUtils {

    fun startOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun endOfDay(timestamp: Long): Long {
        return startOfDay(timestamp) + TimeUnit.DAYS.toMillis(1) - 1
    }

    fun isToday(timestamp: Long): Boolean {
        return startOfDay(timestamp) == startOfDay(System.currentTimeMillis())
    }

    fun isTomorrow(timestamp: Long): Boolean {
        val tomorrow = startOfDay(System.currentTimeMillis()) + TimeUnit.DAYS.toMillis(1)
        return startOfDay(timestamp) == tomorrow
    }

    fun isThisWeek(timestamp: Long): Boolean {
        val now = Calendar.getInstance()
        val dayOfWeek = now.get(Calendar.DAY_OF_WEEK)
        val diffToMonday = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - 2
        val weekStart = startOfDay(now.timeInMillis) - diffToMonday * TimeUnit.DAYS.toMillis(1)
        val weekEnd = weekStart + 7 * TimeUnit.DAYS.toMillis(1)
        return timestamp in weekStart until weekEnd
    }

    fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return ""
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val today = startOfDay(System.currentTimeMillis())
        val target = startOfDay(timestamp)
        val diff = (target - today) / TimeUnit.DAYS.toMillis(1)

        return when {
            diff == 0L -> "今天"
            diff == 1L -> "明天"
            diff == -1L -> "昨天"
            diff in 2..6 -> {
                val weekdays = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
                weekdays[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            }
            else -> "${calendar.get(Calendar.MONTH) + 1}月${calendar.get(Calendar.DAY_OF_MONTH)}日"
        }
    }

    fun formatRelativeTime(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            minutes < 1 -> "刚刚"
            minutes < 60 -> "${minutes} 分钟前"
            hours < 24 -> "${hours} 小时前"
            days < 7 -> "${days} 天前"
            else -> formatDate(timestamp)
        }
    }

    fun formatChatTime(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val now = Calendar.getInstance()
        val sameDay = isToday(timestamp)
        return if (sameDay) {
            String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        } else {
            "${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.DAY_OF_MONTH)}"
        }
    }

    fun groupTasksByDate(tasks: List<com.taskflow.app.domain.model.Task>): List<Pair<String, List<com.taskflow.app.domain.model.Task>>> {
        val today = mutableListOf<com.taskflow.app.domain.model.Task>()
        val tomorrow = mutableListOf<com.taskflow.app.domain.model.Task>()
        val thisWeek = mutableListOf<com.taskflow.app.domain.model.Task>()
        val later = mutableListOf<com.taskflow.app.domain.model.Task>()

        for (task in tasks) {
            val dueDate = task.dueDate
            if (dueDate == null) {
                later.add(task)
                continue
            }
            when {
                isToday(dueDate) -> today.add(task)
                isTomorrow(dueDate) -> tomorrow.add(task)
                isThisWeek(dueDate) -> thisWeek.add(task)
                else -> later.add(task)
            }
        }

        val result = mutableListOf<Pair<String, List<com.taskflow.app.domain.model.Task>>>()
        if (today.isNotEmpty()) result.add("今天" to today.sortedBy { it.order })
        if (tomorrow.isNotEmpty()) result.add("明天" to tomorrow.sortedBy { it.order })
        if (thisWeek.isNotEmpty()) result.add("本周" to thisWeek.sortedBy { it.dueDate ?: Long.MAX_VALUE })
        if (later.isNotEmpty()) result.add("更晚" to later.sortedBy { it.dueDate ?: Long.MAX_VALUE })
        return result
    }
}
