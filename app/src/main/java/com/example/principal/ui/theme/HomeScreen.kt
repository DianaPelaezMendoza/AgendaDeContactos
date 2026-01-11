package com.example.principal.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.principal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val contacts = listOf(
        Contact(
            id = 1,
            name = "Juan Pérez",
            info = "Desarrollador Android",
            imageRes = R.drawable.avatar_gato,
            phone = "111-222-333",
            email = "juan@mail.com"
        ),
        Contact(
            id = 2,
            name = "María Gómez",
            info = "Diseñadora UX",
            imageRes = R.drawable.avatar_perro,
            phone = "444-555-666",
            email = "maria@mail.com"
        ),
        Contact(
            id = 3,
            name = "Carlos López",
            info = "Project Manager",
            imageRes = R.drawable.avatar_conejo,
            phone = "777-888-999",
            email = "carlos@mail.com"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contactos") }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(contacts) { contact ->
                ContactItem(
                    contact = contact,
                    onClick = {
                        navController.navigate(
                            "${NavigationRoutes.DetailScreen.name}/${contact.id}"
                        )
                    }
                )
            }
        }
    }
}


