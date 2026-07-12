package com.taskflow.app.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskflow.app.domain.model.AccentColor
import com.taskflow.app.domain.model.ThemeType
import com.taskflow.app.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.ViewModelScoped
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsModal(
    theme: ThemeType,
    onThemeChange: (ThemeType) -> Unit,
    dailyReminderEnabled: Boolean = true,
    onDailyReminderChange: (Boolean) -> Unit = {},
    preferencesRepository: PreferencesRepository,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var darkMode by remember(theme) { mutableStateOf(theme == ThemeType.DARK) }
    val accentColor by preferencesRepository.accentColor.collectAsState(initial = AccentColor.RED)
    val aiApiKey by preferencesRepository.aiApiKey.collectAsState(initial = "")
    val aiBaseUrl by preferencesRepository.aiBaseUrl.collectAsState(initial = "")
    val aiModel by preferencesRepository.aiModel.collectAsState(initial = "")
    val aiProvider by preferencesRepository.aiProvider.collectAsState(initial = "")

    var showApiConfig by remember { mutableStateOf(false) }
    var localApiKey by remember { mutableStateOf(aiApiKey) }
    var localBaseUrl by remember { mutableStateOf(aiBaseUrl) }
    var localModel by remember { mutableStateOf(aiModel) }
    var localProvider by remember { mutableStateOf(aiProvider) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "设置",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SettingsSectionTitle("外观")
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "深色模式",
                subtitle = if (darkMode) "已开启" else "已关闭",
                trailing = {
                    Switch(
                        checked = darkMode,
                        onCheckedChange = {
                            darkMode = it
                            onThemeChange(if (it) ThemeType.DARK else ThemeType.LIGHT)
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "主题色",
                subtitle = getAccentColorLabel(accentColor),
                onClick = {
                    showColorPicker(accentColor) { newColor ->
                        preferencesRepository.setAccentColor(newColor)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionTitle("AI 智能规划")
            if (!showApiConfig) {
                SettingsItem(
                    icon = Icons.Default.Key,
                    title = "API 配置",
                    subtitle = "使用你自己的 AI API",
                    onClick = { showApiConfig = true }
                )
            } else {
                ApiConfigForm(
                    provider = localProvider,
                    baseUrl = localBaseUrl,
                    apiKey = localApiKey,
                    model = localModel,
                    onProviderChange = { localProvider = it },
                    onBaseUrlChange = { localBaseUrl = it },
                    onApiKeyChange = { localApiKey = it },
                    onModelChange = { localModel = it },
                    onSave = {
                        preferencesRepository.setAiProvider(localProvider)
                        preferencesRepository.setAiBaseUrl(localBaseUrl)
                        preferencesRepository.setAiApiKey(localApiKey)
                        preferencesRepository.setAiModel(localModel)
                        showApiConfig = false
                    },
                    onCancel = { showApiConfig = false }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionTitle("通知")
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "每日提醒",
                subtitle = if (dailyReminderEnabled) "早晨 8:00 推送今日待办" else "已关闭",
                trailing = {
                    Switch(checked = dailyReminderEnabled, onCheckedChange = onDailyReminderChange)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionTitle("关于")
            SettingsItem(
                icon = Icons.Default.Info,
                title = "版本",
                subtitle = "TaskFlow v0.5.0"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

fun getAccentColorLabel(color: AccentColor): String {
    return when (color) {
        AccentColor.RED -> "报纸红"
        AccentColor.INK -> "墨黑"
        AccentColor.GOLD -> "暗金"
        AccentColor.BLUE -> "钢蓝"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showColorPicker(
    currentColor: AccentColor,
    onSelect: (AccentColor) -> Unit
) {
    val colors = AccentColor.values()
    val colorValues = mapOf(
        AccentColor.RED to Color(0xFFC41E3A),
        AccentColor.INK to Color(0xFF1A1A1A),
        AccentColor.GOLD to Color(0xFFB8860B),
        AccentColor.BLUE to Color(0xFF4682B4),
    )

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "选择主题色",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                colors.forEach { color ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(colorValues[color]!!)
                                .clickable { onSelect(color) }
                                .border(
                                    width = if (color == currentColor) 3.dp else 0.dp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = getAccentColorLabel(color),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (color == currentColor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApiConfigForm(
    provider: String,
    baseUrl: String,
    apiKey: String,
    model: String,
    onProviderChange: (String) -> Unit,
    onBaseUrlChange: (String) -> Unit,
    onApiKeyChange: (String) -> Unit,
    onModelChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column {
        val providers = listOf("openai", "gemini", "anthropic", "ollama")
        Text(
            text = "Provider",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        androidx.compose.material3.OutlinedTextField(
            value = provider,
            onValueChange = onProviderChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Base URL",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        androidx.compose.material3.OutlinedTextField(
            value = baseUrl,
            onValueChange = onBaseUrlChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "API Key",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        androidx.compose.material3.OutlinedTextField(
            value = apiKey,
            onValueChange = onApiKeyChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Model",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        androidx.compose.material3.OutlinedTextField(
            value = model,
            onValueChange = onModelChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            gap = 8.dp
        ) {
            TextButton(onClick = onCancel) {
                Text("取消")
            }
            TextButton(onClick = onSave) {
                Text("保存")
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String = "",
    trailing: @Composable () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        trailing()
    }
}
