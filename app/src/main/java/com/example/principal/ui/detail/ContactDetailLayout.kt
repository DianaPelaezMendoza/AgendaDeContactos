package com.example.principal.ui.detail

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import coil.compose.rememberAsyncImagePainter
import com.example.principal.data.local.entity.ContactEntity
import kotlin.text.ifEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailLayout(
        contact: ContactEntity,
        onBackClick: () -> Unit,
        onEditClick: () -> Unit,
        onCallClick: () -> Unit,
        onWhatsAppClick: () -> Unit
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "${contact.firstName} ${contact.lastName}") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar")
                    }

                    Button(
                        onClick = onCallClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Llamar")
                    }

                    Button(
                        onClick = onWhatsAppClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("WhatsApp")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
