package com.example.principal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.principal.ui.detail.DetailScreen
import com.example.principal.ui.login.LoginScreen
import com.example.principal.ui.screens.APIScreen
import com.example.principal.ui.screens.AddEditContactScreen
import com.example.principal.ui.screens.CreatedScreen
import com.example.principal.ui.screens.FilterScreen
import com.example.principal.ui.screens.HomeScreen
import com.example.principal.viewmodel.HomeUiState
import com.example.principal.viewmodel.HomeViewModel

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "LoginScreen"
    ) {

        composable("LoginScreen") {
            LoginScreen(navController)
        }

        composable("HomeScreen") {
            HomeScreen(navController)
        }

        composable(
            route = "DetailScreen/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.IntType })
        ) { backStackEntry ->

            val contactId = backStackEntry.arguments?.getInt("contactId")
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            if (uiState is HomeUiState.Success && contactId != null) {
                val contact = (uiState as HomeUiState.Success)
                    .contacts
                    .find { it.id == contactId }

                contact?.let {
                    DetailScreen(navController, it)
                }
            }
        }

        composable("AddEditContact") {
            AddEditContactScreen(navController)
        }

        composable("FilterScreen") {
            FilterScreen(navController)
        }

        composable("APIScreen") {
            APIScreen(navController)
        }

        composable("CreatedScreen") {
            CreatedScreen(navController)
        }
    }
}
