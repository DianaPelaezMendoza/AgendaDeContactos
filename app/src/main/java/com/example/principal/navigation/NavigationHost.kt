package com.example.principal.navigation


import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember
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

    NavHost(navController, startDestination = "LoginScreen") {
        composable("LoginScreen") { LoginScreen(navController) }
        composable("HomeScreen") { HomeScreen(navController) }

        composable(
            "DetailScreen/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.IntType })
        ) { backStackEntry ->
            DetailScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(
            "AddEditContact/{contactId}",
            arguments = listOf(navArgument("contactId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId")

            // Use HomeViewModel to fetch the contact
            val homeViewModel: HomeViewModel = hiltViewModel()
            val contacto = remember(contactId) {
                contactId?.let { id ->
                    homeViewModel.uiState.value.let { state ->
                        if (state is HomeUiState.Success) state.contacts.find { it.id == id } else null
                    }
                }
            }

            AddEditContactScreen(
                navController = navController,
                contact = contacto // still ContactEntity? like before
            )
        }




        composable("AddEditContact") { AddEditContactScreen(navController) }
        composable("FilterScreen") { FilterScreen(navController) }
        composable("APIScreen") { APIScreen(navController) }
        composable("CreatedScreen") { CreatedScreen(navController) }
    }
}




