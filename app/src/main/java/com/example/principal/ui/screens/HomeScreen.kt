package com.example.principal.ui.screens

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.principal.ui.detail.NetworkUtil

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    context: Context = LocalContext.current  // Get the context
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarDialogImport by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // comprobacion de internet
    val hasInternet = NetworkUtil(context)
    if (!hasInternet) {
        errorMessage = "No hay conexión a internet. Por favor, revisa tu conexión."
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contactos") },
                actions = {
                    IconButton(onClick = { navController.navigate("FilterScreen") }) {
                        Icon(Icons.Filled.List, contentDescription = "Filtrar contactos")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("AddEditContact") }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir contacto")
            }
        },

        bottomBar = {
            Button(
                onClick = {
                    if (hasInternet) {
                        mostrarDialogImport = true
                    } else {
                        errorMessage = "No se puede importar: sin conexión a internet."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Importar contactos desde API")
            }
        }
    ) { padding ->

        // --- mostar mensaje si no hay internet
        if (errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        } else {
            // --- UI STATE HANDLING ---
            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is HomeUiState.Success -> {
                    val contacts = (uiState as HomeUiState.Success).contacts
                    if (contacts.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(padding),
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
                        modifier = Modifier.fillMaxSize().padding(padding),
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

        // --- DIALOG TO IMPORT CONTACTS ---
        if (mostrarDialogImport) {
            var numContactos by remember { mutableStateOf("") }
            var error by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { mostrarDialogImport = false },
                title = { Text("Importar desde API") },
                text = {
                    Column {
                        Text("Introduce cuántos contactos quieres importar. Máximo 30 por vez:")
                        OutlinedTextField(
                            value = numContactos,
                            onValueChange = { numContactos = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (error.isNotEmpty()) {
                            Text(error, color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val limite = numContactos.toIntOrNull()
                        if (limite == null || limite < 1 || limite > 30) {
                            error = "Por favor ingresa un número entre 1 y 30"
                        } else {
                            viewModel.importContactsFromApi(limite)
                            mostrarDialogImport = false
                        }
                    }) { Text("Aceptar") }
                },
                dismissButton = {
                    Button(onClick = { mostrarDialogImport = false }) { Text("Cancelar") }
                }
            )
        }
    }
}
