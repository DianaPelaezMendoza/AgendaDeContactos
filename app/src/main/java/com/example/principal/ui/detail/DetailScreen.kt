package com.example.principal.ui.detail

import android.R.attr.value
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavController
import com.example.principal.data.local.entity.ContactEntity

import com.example.principal.viewmodel.DetailViewModel

/**
 * Pantalla de detalle de un contacto.
 *
 * Esta pantalla muestra toda la información de un contacto específico:
 * - Foto, nombre completo, ciudad y estado.
 * - Teléfono y correo electrónico.
 *
 * También proporciona acciones rápidas:
 * - Editar el contacto, navegando a [AddEditContactScreen].
 * - Llamar al contacto mediante la app de teléfono.
 * - Enviar mensaje de WhatsApp al contacto.
 *
 * @param navController Controlador de navegación para moverse entre pantallas.
 * @param viewModel ViewModel que proporciona el contacto a mostrar.
 */
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
            modifier = Modifier.fillMaxSize().padding(16.dp),
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
                Button(
                    onClick = { navController.navigate("AddEditContact/${c.id}") }
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Editar")
                }

                // Llamar
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${c.phone}")
                    context.startActivity(intent)
                }) { Text("Llamar") }

                // WhatsApp
                Button(onClick = {
                    val url = "https://wa.me/${c.phone}"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    context.startActivity(intent)
                }) { Text("WhatsApp") }
            }
        }
    }
}
