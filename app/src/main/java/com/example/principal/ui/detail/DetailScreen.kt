package com.example.principal.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


import com.example.principal.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val contact by viewModel.contact.collectAsState()

    if (contact == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val c = contact!!

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Aquí usamos nuestro layout bonito
            ContactDetailLayout(contact = c)

            // Botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Editar
                Button(
                    onClick = { navController.navigate("AddEditContact/${c.id}") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar")
                }

                // Eliminar
                Button(
                    onClick = { viewModel.deleteContact(c); navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Llamar
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${c.phone}"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Llamar") }

                // WhatsApp
                Button(
                    onClick = {
                        val url = "https://wa.me/${c.phone}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("WhatsApp") }
            }
        }
    }
}
