package com.example.principal.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.principal.ui.home.components.ContactItem
import com.example.principal.util.NetworkUtils
import com.example.principal.viewmodel.HomeUiState
import com.example.principal.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Observa el estado de conectividad
    LaunchedEffect(Unit) { NetworkUtils.observeNetwork(context) }
    val isOnline by NetworkUtils.isOnline.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Botón de importar contactos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.importContacts() },
                enabled = isOnline
            ) {
                Text("Importar contactos")
            }
        }

        // Mensaje de conexión
        if (!isOnline) {
            Text(
                text = "Sin conexión",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido principal
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Success -> {
                val contacts = (uiState as HomeUiState.Success).contacts
                if (contacts.isEmpty()) {
                    Text("No hay contactos")
                } else {
                    LazyColumn {
                        items(contacts) { contact ->
                            ContactItem(contact = contact) {
                                navController.navigate("DetailScreen/${contact.id}")
                            }
                        }
                    }
                }
            }
            is HomeUiState.Error -> {
                Text(
                    text = (uiState as HomeUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

