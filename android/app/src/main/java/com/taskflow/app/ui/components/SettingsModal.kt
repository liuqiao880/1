package com.taskflow.app.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskflow.app.domain.model.ThemeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsModal(
    theme: ThemeType,
    onThemeChange: (ThemeType) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var darkMode by remember { mutableStateOf(theme == ThemeType.DARK) }

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

            // 外观
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

            Spacer(modifier = Modifier.height(24.dp))

            // AI 设置
            SettingsSectionTitle("AI 智能规划")
            SettingsItem(
                icon = Icons.Default.Key,
                title = "API 配置",
                subtitle = "使用你自己的 AI API",
                onClick = { /* 打开 API 配置页 */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 通知
            SettingsSectionTitle("通知")
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "每日提醒",
                subtitle = "早晨 8:00 推送今日待办",
                trailing = {
                    Switch(checked = true, onCheckedChange = {})
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 关于
            SettingsSectionTitle("关于")
            SettingsItem(
                icon = Icons.Default.Info,
                title = "版本",
                subtitle = "TaskFlow v1.0.0"
            )

            Spacer(modifier = Modifier.height(32.dp))
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
