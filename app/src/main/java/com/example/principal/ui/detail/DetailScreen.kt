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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.principal.viewmodel.DetailViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.principal.data.local.entity.ContactEntity

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel(),
    contactId: Int
) {
    val context = LocalContext.current
    val contactState by viewModel.contact.collectAsState()

    if (contactState == null) {
        // Pantalla de carga
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val contact = contactState!!

        ContactDetailLayout (
            contact = contact,
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate("AddEditContact/${contact.id}") },
            onCallClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                context.startActivity(intent)
            },
            onWhatsAppClick = {
                val url = "https://wa.me/${contact.phone}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
    }
}

