package com.example.principal.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.principal.ui.screens.components.ContactItem
import com.example.principal.viewmodel.HomeUiState
import com.example.principal.viewmodel.HomeViewModel
import androidx.compose.runtime.collectAsState




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Observa el estado de la UI desde el ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        // ----------------------------------
        // Barra superior
        // ----------------------------------
        topBar = {
            TopAppBar(
                title = { Text("Contactos") },
                actions = {
                    // Botón para ir a la pantalla de filtros
                    IconButton(
                        onClick = { navController.navigate("FilterScreen") }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Filtrar contactos"
                        )
                    }
                }
            )
        },

        // ----------------------------------
        // Botón flotante (+)
        // ----------------------------------
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("AddEditContact") }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Añadir contacto"
                )
            }
        },

        // ----------------------------------
        // Barra inferior (importar API)
        // ----------------------------------
        bottomBar = {
            Button(
                onClick = { viewModel.importContacts() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Importar contactos desde API")
            }
        }

    ) { padding ->

        // ----------------------------------
        // Contenido principal
        // ----------------------------------
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Success -> {
                val contacts = (uiState as HomeUiState.Success).contacts

                if (contacts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay contactos")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(contacts) { contact ->
                            ContactItem(contact = contact) {
                                navController.navigate("DetailScreen/${contact.id}")
                            }
                        }
                    }
                }
            }

            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as HomeUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
