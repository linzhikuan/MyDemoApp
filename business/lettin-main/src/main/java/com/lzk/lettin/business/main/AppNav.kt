package com.lzk.lettin.business.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lzk.lettin.business.main.ui.MainScreen
import com.lzk.lettin.business.main.ui.screens.deviceControlScreen
import com.lzk.lettin.business.main.ui.screens.loginScreen

object Routes {
    const val MAIN = "main"
    const val LOGIN = "user/long"
    const val DEVICE_DETAIL = "device_detail"
    const val DEVICE_CONTROL = "device_control"
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
                navController.navigate(Routes.DEVICE_DETAIL)
            }, onDeviceControlClick = {
                navController.navigate(Routes.DEVICE_CONTROL)
            })
        }
        composable(Routes.LOGIN) {
            loginScreen()
        }
        composable(Routes.DEVICE_CONTROL) {
            deviceControlScreen()
        }
    }
}
