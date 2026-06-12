package com.lzk.lettin.business.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.lettin.business.main.ui.MainScreen
import com.lzk.lettin.business.main.ui.screens.deviceControlScreen
import com.lzk.lettin.business.main.ui.screens.loginScreen
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val MAIN = "main"
    const val LOGIN = "user/long"
    const val DEVICE_DETAIL = "device_detail"
    const val DEVICE_CONTROL = "device_control/{data}"
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
            }, onDeviceControlClick = { hqData ->
                val json = Gson().toJson(hqData)
                val encoded = URLEncoder.encode(json, "UTF-8")
                navController.navigate("device_control/$encoded")
            })
        }
        composable(Routes.LOGIN) {
            loginScreen()
        }
        composable(
            route = Routes.DEVICE_CONTROL,
            arguments = listOf(
                navArgument("data") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("data") ?: ""
            val json = URLDecoder.decode(encoded, "UTF-8")
            val hqData = Gson().fromJson(json, LettinGatewayInfo::class.java)
            deviceControlScreen(hqData)
        }
    }
}
