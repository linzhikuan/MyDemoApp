package com.lzk.lettin.business

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.lettin.business.screens.HomeScreen
import com.lzk.lettin.business.screens.settingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNav()
        }
    }
}

@Composable
fun MainScreen(
    onLoginClick: () -> Unit,
    onSettingClick: (LettinGatewayInfo) -> Unit,
) {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
        NavigationHost(navController, Modifier.padding(innerPadding), onLoginClick, onSettingClick)
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items =
        listOf(
            Screen.Home,
            Screen.Setting,
        )
    // 获取当前路由
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(screen.icon),
                        contentDescription = null,
                    )
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
            )
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onSettingClick: (LettinGatewayInfo) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) { HomeScreen(onLoginClick, onSettingClick) }
        composable(Screen.Setting.route) {
            settingScreen()
        }
    }
}

sealed class Screen(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Home : Screen("home", "首页", R.drawable.ic_menu_edit)

    object Setting : Screen("settings", "设置", R.drawable.ic_menu_search)
}
