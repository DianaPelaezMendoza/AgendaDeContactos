package com.example.principal.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.principal.ui.detail.DetailScreen
import com.example.principal.ui.login.LoginScreen
import com.example.principal.ui.screens.AddEditContactScreen
import com.example.principal.ui.screens.HomeScreen
import com.example.principal.viewmodel.AddEditContactViewModel
import com.example.principal.viewmodel.DetailViewModel

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LoginScreen") {

        // LOGIN
        composable("LoginScreen") {
            LoginScreen(navController)
        }

        // HOME
        composable("HomeScreen") {
            HomeScreen(navController)
        }

        // DETAIL SCREEN
        composable(
            "DetailScreen/{contactId}",
            arguments = listOf(navArgument("contactId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId") ?: 0
            val viewModel: DetailViewModel = hiltViewModel()

            DetailScreen(
                navController = navController,
                viewModel = viewModel,
                contactId = contactId
            )
        }

        // ADD / EDIT SCREEN
        composable(
            "AddEditContact/{contactId}",
            arguments = listOf(navArgument("contactId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId") ?: -1
            val viewModel: AddEditContactViewModel = hiltViewModel()

            AddEditContactScreen(
                navController = navController,
                contactId = if (contactId != -1) contactId else null,
                viewModel = viewModel
            )
        }

        // ADD NEW CONTACT
        composable("AddEditContact") {
            val viewModel: AddEditContactViewModel = hiltViewModel()
            AddEditContactScreen(
                navController = navController,
                contactId = null,
                viewModel = viewModel
            )
        }
    }
}
