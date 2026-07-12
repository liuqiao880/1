package com.taskflow.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.taskflow.app.domain.model.AccentColor
import com.taskflow.app.domain.model.ThemeType
import com.taskflow.app.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "taskflow_prefs")

class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
        val EXPANDED_PARENTS = stringPreferencesKey("expanded_parents")
        val AI_API_KEY = stringPreferencesKey("ai_api_key")
        val AI_BASE_URL = stringPreferencesKey("ai_base_url")
        val AI_MODEL = stringPreferencesKey("ai_model")
        val AI_PROVIDER = stringPreferencesKey("ai_provider")
        val AI_SYSTEM_PROMPT = stringPreferencesKey("ai_system_prompt")
        val DAILY_REMINDER_ENABLED = booleanPreferencesKey("daily_reminder_enabled")
        val DAILY_REMINDER_HOUR = intPreferencesKey("daily_reminder_hour")
        val DAILY_REMINDER_MINUTE = intPreferencesKey("daily_reminder_minute")
    }

    override val theme: Flow<ThemeType> = context.dataStore.data
        .map { prefs ->
            val value = prefs[Keys.THEME] ?: ThemeType.SYSTEM.name
            ThemeType.values().firstOrNull { it.name == value } ?: ThemeType.SYSTEM
        }

    override val accentColor: Flow<AccentColor> = context.dataStore.data
        .map { prefs ->
            val value = prefs[Keys.ACCENT_COLOR] ?: AccentColor.RED.name
            AccentColor.values().firstOrNull { it.name == value } ?: AccentColor.RED
        }

    override val expandedParents: Flow<Set<Int>> = context.dataStore.data
        .map { prefs ->
            val value = prefs[Keys.EXPANDED_PARENTS] ?: ""
            if (value.isEmpty()) emptySet()
            else value.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        }

    override val aiApiKey: Flow<String> = context.dataStore.data
        .map { it[Keys.AI_API_KEY] ?: "" }

    override val aiBaseUrl: Flow<String> = context.dataStore.data
        .map { it[Keys.AI_BASE_URL] ?: "https://api.openai.com/v1" }

    override val aiModel: Flow<String> = context.dataStore.data
        .map { it[Keys.AI_MODEL] ?: "gpt-4o-mini" }

    override val aiProvider: Flow<String> = context.dataStore.data
        .map { it[Keys.AI_PROVIDER] ?: "openai" }

    override val aiSystemPrompt: Flow<String> = context.dataStore.data
        .map { it[Keys.AI_SYSTEM_PROMPT] ?: "" }

    override val dailyReminderEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[Keys.DAILY_REMINDER_ENABLED] ?: true }

    override val dailyReminderTime: Flow<Pair<Int, Int>> = context.dataStore.data
        .map { prefs ->
            val hour = prefs[Keys.DAILY_REMINDER_HOUR] ?: 8
            val minute = prefs[Keys.DAILY_REMINDER_MINUTE] ?: 0
            hour to minute
        }

    override suspend fun setTheme(theme: ThemeType) {
        context.dataStore.edit { it[Keys.THEME] = theme.name }
    }

    override suspend fun setAccentColor(color: AccentColor) {
        context.dataStore.edit { it[Keys.ACCENT_COLOR] = color.name }
    }

    override suspend fun setExpandedParents(ids: Set<Int>) {
        context.dataStore.edit {
            it[Keys.EXPANDED_PARENTS] = ids.joinToString(",")
        }
    }

    override suspend fun toggleParentExpanded(id: Int) {
        val current = expandedParents.first()
        val updated = if (current.contains(id)) current - id else current + id
        context.dataStore.edit {
            it[Keys.EXPANDED_PARENTS] = updated.joinToString(",")
        }
    }

    override suspend fun setAiApiKey(key: String) {
        context.dataStore.edit { it[Keys.AI_API_KEY] = key }
    }

    override suspend fun setAiBaseUrl(url: String) {
        context.dataStore.edit { it[Keys.AI_BASE_URL] = url }
    }

    override suspend fun setAiModel(model: String) {
        context.dataStore.edit { it[Keys.AI_MODEL] = model }
    }

    override suspend fun setAiProvider(provider: String) {
        context.dataStore.edit { it[Keys.AI_PROVIDER] = provider }
    }

    override suspend fun setAiSystemPrompt(prompt: String) {
        context.dataStore.edit { it[Keys.AI_SYSTEM_PROMPT] = prompt }
    }

    override suspend fun setDailyReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DAILY_REMINDER_ENABLED] = enabled }
    }

    override suspend fun setDailyReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[Keys.DAILY_REMINDER_HOUR] = hour
            it[Keys.DAILY_REMINDER_MINUTE] = minute
        }
    }
}
