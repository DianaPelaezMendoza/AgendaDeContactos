package com.example.principal.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.principal.viewmodel.DetailViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel
) {
    val context = LocalContext.current
    val contact by viewModel.contact.collectAsState()

    if (contact == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val c = contact!!
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle de contacto") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(c.image),
                        contentDescription = "Foto contacto",
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("${c.firstName} ${c.lastName}", style = MaterialTheme.typography.headlineMedium)
                    Text("${c.city}, ${c.state}")
                    Text(c.phone)
                    Text(c.email)

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Editar
                        Button(onClick = {
                            // Pasar contacto completo a AddEditContactScreen
                            navController.currentBackStackEntry?.savedStateHandle?.set("contact", c)
                            navController.navigate("AddEditContact")
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Editar")
                        }

                        // Llamar
                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${c.phone}"))
                            context.startActivity(intent)
                        }) { Text("Llamar") }

                        // WhatsApp
                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${c.phone}"))
                            context.startActivity(intent)
                        }) { Text("WhatsApp") }
                    }
                }
            }
        )
    }
}
