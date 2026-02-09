package com.example.principal.ui.screens

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.principal.data.local.dao.ContactSource
import com.example.principal.ui.detail.NetworkUtil
import com.example.principal.ui.screens.components.ContactItem
import com.example.principal.viewmodel.HomeUiState
import com.example.principal.viewmodel.HomeViewModel


/**
 * Pantalla que muestra los contactos importados desde la API.
 *
 * Verifica si hay conexión a Internet antes de mostrar los datos.
 * Permite importar un número limitado de contactos (máx. 30) desde la API.
 * Muestra un mensaje de error si no hay conexión o si ocurre un error al cargar.
 *
 * @param navController Controlador de navegación de la app.
 * @param viewModel ViewModel que maneja la lógica de negocio de los contactos.
 * @param context Contexto necesario para verificar la conexión a Internet.
 */



@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APIScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val uiState by viewModel.uiState.collectAsState()

    var mostrarDialogImport by remember { mutableStateOf(false) }
    var cantidadImportar by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val hasInternet = NetworkUtil(context)

    if (!hasInternet) {
        errorMessage = "No hay conexión a internet. Revisa tu conexión."
    }

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
                    if (hasInternet) {
                        IconButton(onClick = { mostrarDialogImport = true }) {
                            Icon(
                                Icons.Filled.CloudDownload,
                                contentDescription = "Importar desde API"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

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
            when (uiState) {

                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

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

                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
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

    // 🔹 DIALOGO PARA IMPORTAR CONTACTOS
    if (mostrarDialogImport) {
        AlertDialog(
            onDismissRequest = { mostrarDialogImport = false },
            title = { Text("Importar contactos") },
            text = {
                Column {
                    Text("Ingresa la cantidad de contactos (1 a 30)")
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cantidadImportar,
                        onValueChange = { cantidadImportar = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cantidad = cantidadImportar.toIntOrNull()
                        if (cantidad != null && cantidad in 1..30) {
                            viewModel.importContactsFromApi(cantidad)
                            mostrarDialogImport = false
                            cantidadImportar = ""
                        } else {
                            errorMessage = "Número inválido (1 a 30)"
                        }
                    }
                ) {
                    Text("Importar")
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


