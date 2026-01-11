package com.example.principal.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    contactId: Int?
) {

    val contact = contactId?.let {
        ContactsRepository.getContactById(it)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de contacto") },
                navigationIcon = {
                    Text(
                        text = "←",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                }
            )
        }
    ) { paddingValues ->

        if (contact == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Contacto no encontrado")
            }
        }
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Imagen del contacto
                if (contact.imageRes != 0) {
                    Image(
                        painter = painterResource(id = contact.imageRes),
                        contentDescription = "Imagen de contacto",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Imagen no disponible")
                }

                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(text = contact.info)
                Text(text = contact.phone)
                Text(text = contact.email)
            }
        }
    }
}
