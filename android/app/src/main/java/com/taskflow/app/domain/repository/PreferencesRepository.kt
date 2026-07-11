package com.taskflow.app.domain.repository

import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.ThemeType
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val theme: Flow<ThemeType>
    val expandedParents: Flow<Set<Int>>
    val aiApiKey: Flow<String>
    val aiBaseUrl: Flow<String>
    val aiModel: Flow<String>
    val dailyReminderEnabled: Flow<Boolean>
    val dailyReminderTime: Flow<Pair<Int, Int>>

    suspend fun setTheme(theme: ThemeType)
    suspend fun setExpandedParents(ids: Set<Int>)
    suspend fun toggleParentExpanded(id: Int)
    suspend fun setAiApiKey(key: String)
    suspend fun setAiBaseUrl(url: String)
    suspend fun setAiModel(model: String)
    suspend fun setDailyReminderEnabled(enabled: Boolean)
    suspend fun setDailyReminderTime(hour: Int, minute: Int)
}
