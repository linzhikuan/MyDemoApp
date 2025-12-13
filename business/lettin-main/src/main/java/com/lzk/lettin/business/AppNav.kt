package com.lzk.lettin.business

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lzk.lettin.business.screens.loginScreen

object Routes {
    const val MAIN = "main"
    const val LOGIN = "user/long"
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MAIN) {
        composable(Routes.MAIN) {
            MainScreen(onLoginClick = {
                navController.navigate(Routes.LOGIN)
            }, onSettingClick = {
                navController.navigate(Routes.LOGIN)
            })
        }
        composable(Routes.LOGIN) {
            loginScreen()
        }
    }
}
