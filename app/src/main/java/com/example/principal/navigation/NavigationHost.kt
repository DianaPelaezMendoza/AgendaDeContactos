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
import com.example.principal.ui.screens.APIScreen
import com.example.principal.ui.screens.AddEditContactScreen
import com.example.principal.ui.screens.CreatedScreen
import com.example.principal.ui.screens.FilterScreen
import com.example.principal.ui.screens.HomeScreen
import com.example.principal.viewmodel.AddEditContactViewModel
import com.example.principal.viewmodel.DetailViewModel

/**
 * Host de navegación de la aplicación.
 *
 * Este Composable configura el NavController y define todas las rutas
 * de la aplicación mediante [NavHost] y [composable].
 *
 * Rutas definidas:
 * - "LoginScreen": Pantalla de inicio de sesión.
 * - "HomeScreen": Pantalla principal con lista de contactos.
 * - "DetailScreen/{contactId}": Detalle de un contacto específico.
 * - "AddEditContact/{contactId}": Editar un contacto existente.
 * - "AddEditContact": Crear un nuevo contacto.
 * - "FilterScreen": Filtrado de contactos.
 * - "APIScreen": Visualización de contactos importados desde API.
 * - "CreatedScreen": Visualización de contactos creados manualmente.
 *
 * El NavController se utiliza para navegar entre estas pantallas.
 */

@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LoginScreen") {
        // LOGIN SCREEN
        composable("LoginScreen") {
            LoginScreen(navController)
        }

        // HOME SCREEN
        composable("HomeScreen") {
            HomeScreen(navController)
        }

        // DETAIL SCREEN
        composable(
            "DetailScreen/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.IntType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId") ?: 0
            val viewModel: DetailViewModel = hiltViewModel()

            // Pass the contactId to the DetailScreen composable
            DetailScreen(
                navController = navController,
                viewModel = viewModel,
                contactId = contactId
            )
        }

        // ADD / EDIT CONTACT SCREEN with contactId
        composable(
            "AddEditContact/{contactId}",
            arguments = listOf(navArgument("contactId") {
                type = NavType.IntType; defaultValue = -1
            })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId") ?: -1
            val viewModel: AddEditContactViewModel = hiltViewModel()

            // If contactId is -1, it's a new contact, otherwise it's an existing one
            AddEditContactScreen(
                navController = navController,
                contactId = if (contactId != -1) contactId else null,
                viewModel = viewModel
            )
        }

        // ADD NEW CONTACT (No contactId needed here)
        composable("AddEditContact") {
            val viewModel: AddEditContactViewModel = hiltViewModel()
            AddEditContactScreen(
                navController = navController,
                contactId = null,
                viewModel = viewModel
            )
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