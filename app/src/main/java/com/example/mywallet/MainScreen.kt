package com.example.mywallet

import android.R.attr.contentDescription
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    TRANSACTION(
        route = "transaction",
        label = "収支",
        icon = Icons.Default.Payments,
        contentDescription = "収支入力"
    ),

    HISTORY(
        route = "history",
        label =  "履歴",
        icon = Icons.Default.AccessTime,
        contentDescription = "取引履歴"
    ),

    BALANCE(
        route = "balance",
        label = "残高",
        icon = Icons.Default.AccountBalance,
        contentDescription = "残高確認"
    ),

    SETTING(
        route =  "setting",
        label = "設定",
        icon = Icons.Default.Settings,
        contentDescription = "設定画面"
    )

}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.TRANSACTION
    var selectedDestination by remember { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFF171824),
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets,
                modifier = Modifier.height(90.dp),
                containerColor = Color(0xFF1D263A)
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        colors = NavigationBarItemColors(
                            selectedIconColor = Color(0xFFC0E2FF),
                            selectedTextColor = Color(0xFFC0E2FF),
                            selectedIndicatorColor = Color(0xFF3A5383),
                            unselectedIconColor = Color(0xFF7A7C88),
                            unselectedTextColor = Color(0xFF7A7C88),
                            disabledIconColor = Color.DarkGray,
                            disabledTextColor = Color.DarkGray
                        ),
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }

    ) { contentPadding ->
        AppNavHost(navController,startDestination,modifier = Modifier.padding(contentPadding))
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ){
        composable(Destination.TRANSACTION.route){
            Transaction()
        }

        composable(Destination.HISTORY.route) {
            History()
        }

        composable(Destination.BALANCE.route) {
            Balance()
        }

        composable(Destination.SETTING.route) {
            Setting()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun testScreen()
{
    MainScreen()
}