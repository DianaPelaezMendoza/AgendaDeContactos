package com.example.principal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.principal.data.local.dao.ContactSource
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.viewmodel.AddEditContactViewModel
import com.example.principal.viewmodel.HomeViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(
    navController: NavHostController,
    contact: ContactEntity? = null // null si es creación
) {
    val viewModel: AddEditContactViewModel = hiltViewModel()

    // Cargar contacto si existe
    LaunchedEffect(contact) {
        viewModel.loadContact(contact)
    }

    val existingContact by viewModel.contact.collectAsState()

    // Estados de los campos del formulario
    var firstName by remember { mutableStateOf(existingContact?.firstName ?: "") }
    var lastName by remember { mutableStateOf(existingContact?.lastName ?: "") }
    var city by remember { mutableStateOf(existingContact?.city ?: "") }
    var state by remember { mutableStateOf(existingContact?.state ?: "") }
    var phone by remember { mutableStateOf(existingContact?.phone ?: "") }
    var email by remember { mutableStateOf(existingContact?.email ?: "") }

    // Actualizar estados cuando el contacto cargue
    LaunchedEffect(existingContact) {
        existingContact?.let {
            firstName = it.firstName
            lastName = it.lastName
            city = it.city
            state = it.state
            phone = it.phone
            email = it.email
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingContact == null) "Crear contacto" else "Editar contacto") },
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campos del formulario
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Ciudad") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("Estado") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botones Guardar y Cancelar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            val newContact = ContactEntity(
                                id = existingContact?.id ?: 0,
                                firstName = firstName,
                                lastName = lastName,
                                city = city,
                                state = state,
                                phone = phone,
                                email = email,
                                thumbnail = existingContact?.thumbnail ?: "",
                                image = existingContact?.image ?: "",
                                source = existingContact?.source ?: ContactSource.CREATED
                            )
                            viewModel.saveContact(newContact)
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    )
}
