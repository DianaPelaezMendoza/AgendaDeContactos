package com.example.principal.ui.detail

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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavController
import com.example.principal.data.local.entity.ContactEntity

@Composable
fun DetailScreen(
    navController: NavController,
    contact: ContactEntity
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberAsyncImagePainter(contact.image),
            contentDescription = "Foto contacto",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("${contact.firstName} ${contact.lastName}", style = MaterialTheme.typography.headlineMedium)
        Text(contact.city + ", " + contact.state)
        Text(contact.phone)
        Text(contact.email)

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    navController.navigate("AddEditContact/${contact.id}")
                }
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar")
            }

            Button(onClick = {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${contact.phone}")
                context.startActivity(intent)
            }) {
                Text("Llamar")
            }

            Button(onClick = {
                val url = "https://wa.me/${contact.phone}"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }) {
                Text("WhatsApp")
            }
        }
    }
}

