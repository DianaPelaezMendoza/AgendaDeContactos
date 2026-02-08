package com.example.principal.ui.detail

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import coil.compose.rememberAsyncImagePainter
import com.example.principal.data.local.entity.ContactEntity

/**
 * Diseño para mostrar los detalles de un contacto de manera consistente.
 *
 * Este Composable muestra la imagen del contacto, su nombre completo, ciudad, estado, número de teléfono
 * y correo electrónico. Asegura que los detalles del contacto se muestren de manera clara y visualmente atractiva.
 *
 * @param contact El [ContactEntity] que se va a mostrar, que contiene la información del contacto.
 * @param modifier Un [Modifier] para personalizar la apariencia o el comportamiento del diseño (opcional).
 */

@Composable
fun ContactDetailLayout(
    contact: ContactEntity,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp) // espacio entre elementos
    ) {
        // Foto del contacto
        Image(
            painter = rememberAsyncImagePainter(contact.image.ifEmpty { contact.thumbnail }),
            contentDescription = "Foto del contacto",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )

        // Nombre completo
        Text(
            text = "${contact.firstName} ${contact.lastName}",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        // Ciudad y estado
        Text(
            text = "${contact.city}, ${contact.state}",
            style = MaterialTheme.typography.bodyMedium
        )

        // Teléfono
        Text(
            text = "Teléfono: ${contact.phone}",
            style = MaterialTheme.typography.bodyMedium
        )

        // Email
        Text(
            text = "Email: ${contact.email}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
