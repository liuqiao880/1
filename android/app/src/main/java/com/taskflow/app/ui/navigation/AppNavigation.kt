package com.taskflow.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taskflow.app.ui.screen.chat.ChatDetailScreen
import com.taskflow.app.ui.screen.chat.ChatListScreen
import com.taskflow.app.ui.screen.home.HomeScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object ChatList : Screen("chat_list")
    data object ChatDetail : Screen("chat_detail/{chatId}") {
        fun createRoute(chatId: String) = "chat_detail/$chatId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToChatList = {
                    navController.navigate(Screen.ChatList.route)
                }
            )
        }

        composable(Screen.ChatList.route) {
            ChatListScreen(
                onBack = { navController.popBackStack() },
                onChatClick = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                },
                onSettingsClick = {}
            )
        }

        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatDetailScreen(
                chatId = chatId,
                onBack = { navController.popBackStack() },
                onNewChat = { newChatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(newChatId)) {
                        popUpTo(Screen.ChatList.route) { inclusive = false }
                    }
                }
            )
        }
    }
}
