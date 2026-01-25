package com.example.principal.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.ui.detail.DetailScreen
import com.example.principal.ui.home.HomeScreen
import com.example.principal.ui.login.LoginScreen
import com.example.principal.viewmodel.HomeViewModel

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "LoginScreen"
    ) {

        // ----------------------------------
        // Login Screen
        // ----------------------------------
        composable("LoginScreen") {
            LoginScreen(navController)
        }

        // ----------------------------------
        // Home Screen
        // ----------------------------------
        composable("HomeScreen") {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController, viewModel)
        }

        // ----------------------------------
        // Detail Screen
        // ----------------------------------
        composable(
            route = "DetailScreen/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.IntType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId")
            val viewModel: HomeViewModel = hiltViewModel()

            val contact: ContactEntity? = viewModel.contactsFlow.replayCache.firstOrNull()
                ?.find { it.id == contactId }

            contact?.let {
                DetailScreen(navController, it)
            }
        }
    }
}
