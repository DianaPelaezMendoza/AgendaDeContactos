package com.example.principal.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavigationHostController() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.LoginScreen.name
    ) {

        composable(NavigationRoutes.LoginScreen.name) {
            LoginScreen(navController)
        }

        composable(NavigationRoutes.HomeScreen.name) {
            HomeScreen(navController)
        }

        composable(
            route = "${NavigationRoutes.DetailScreen.name}/{contactId}",
            arguments = listOf(
                navArgument("contactId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId")
            DetailScreen(navController, contactId)
        }
    }
}


