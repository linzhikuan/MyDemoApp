package com.lzk.lettin.business.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.ui.screens.HomeScreen
import com.lzk.lettin.business.main.ui.screens.LotteryDetailScreen
import com.lzk.lettin.business.main.ui.screens.MyTicketsScreen
import com.lzk.lettin.business.main.ui.screens.settingScreen

object Routes {
    const val HOME = "home"
    const val LOTTERY = "lottery/{type}"
    const val MY_TICKETS = "my_tickets"
    const val SETTING = "setting"

    fun lottery(type: LotteryType) = "lottery/${type.name}"
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onLotteryClicked = { type -> navController.navigate(Routes.lottery(type)) },
                onMyTicketsClicked = { navController.navigate(Routes.MY_TICKETS) },
                onSettingClicked = { navController.navigate(Routes.SETTING) },
            )
        }
        composable(
            route = Routes.LOTTERY,
            arguments = listOf(navArgument("type") { type = NavType.StringType }),
        ) { entry ->
            val raw = entry.arguments?.getString("type")
            val type = LotteryType.from(raw)
            LotteryDetailScreen(type = type, onBack = { navController.popBackStack() })
        }
        composable(Routes.MY_TICKETS) {
            MyTicketsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.SETTING) {
            settingScreen()
        }
    }
}
