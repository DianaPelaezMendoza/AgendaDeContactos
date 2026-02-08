package com.example.principal.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.principal.data.local.dao.ContactSource
import com.example.principal.ui.screens.components.ContactItem
import com.example.principal.viewmodel.HomeUiState
import com.example.principal.viewmodel.HomeViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APIScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarDialogImport by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contactos desde API") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarDialogImport = true }) {
                        Icon(
                            Icons.Filled.CloudDownload,
                            contentDescription = "Importar desde API"
                        )
                    }
                }
            )
        }
    ) { padding ->

        when (uiState) {
            is HomeUiState.Success -> {
                val contacts = (uiState as HomeUiState.Success).contacts
                    .filter { it.source == ContactSource.IMPORTED }

                if (contacts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay contactos importados")
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

            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error al cargar contactos")
                }
            }
        }

        // -------- Dialogo importar X contactos --------
        if (mostrarDialogImport) {
            var numContactos by remember { mutableStateOf("") }
            var error by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { mostrarDialogImport = false },
                title = { Text("Importar desde API") },
                text = {
                    Column {
                        Text(
                            "Introduce cuántos contactos deseas importar. " +
                                    "Puedes importar hasta 30 contactos por vez."
                        )
                        OutlinedTextField(
                            value = numContactos,
                            onValueChange = { numContactos = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
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
                        if (limite == null || limite !in 1..30) {
                            error = "Introduce un número entre 1 y 30"
                        } else {
                            viewModel.importContactsFromApi(limite)
                            mostrarDialogImport = false
                        }
                    }) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    Button(onClick = { mostrarDialogImport = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
