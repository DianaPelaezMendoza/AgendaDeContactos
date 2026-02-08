package com.example.principal.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.principal.data.local.entity.ContactEntity
@Composable
fun ContactItem(
    contact: ContactEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ----------------------------------
            // Imagen o icono por defecto
            // ----------------------------------
            if (contact.thumbnail.isNotEmpty()) {
                // Contacto de la API → tiene foto
                Image(
                    painter = rememberAsyncImagePainter(contact.thumbnail),
                    contentDescription = "Foto de ${contact.firstName}",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Contacto creado a mano → icono genérico
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Contacto sin foto",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // ----------------------------------
            // Información del contacto
            // ----------------------------------
            Column {
                Text(
                    text = "${contact.firstName} ${contact.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${contact.city}, ${contact.state}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
