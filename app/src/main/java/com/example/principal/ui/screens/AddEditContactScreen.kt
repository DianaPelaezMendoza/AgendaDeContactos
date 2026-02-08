package com.example.principal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.principal.data.local.dao.ContactSource
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.viewmodel.AddEditContactViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(
    navController: NavHostController,
    contactId: Int? = null,
    viewModel: AddEditContactViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    // Cargar contacto si es edición
    LaunchedEffect(contactId) {
        contactId?.let { viewModel.loadContact(it) }
    }

    val existingContact by viewModel.contact.collectAsState()

    var firstName by remember { mutableStateOf(existingContact?.firstName ?: "") }
    var lastName by remember { mutableStateOf(existingContact?.lastName ?: "") }
    var city by remember { mutableStateOf(existingContact?.city ?: "") }
    var state by remember { mutableStateOf(existingContact?.state ?: "") }
    var phone by remember { mutableStateOf(existingContact?.phone ?: "") }
    var email by remember { mutableStateOf(existingContact?.email ?: "") }

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

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingContact == null) "Crear contacto" else "Editar contacto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campos del formulario
                OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("Estado") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = {
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
                    }, modifier = Modifier.weight(1f)) { Text("Guardar") }

                    Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray), modifier = Modifier.weight(1f)) { Text("Cancelar") }
                }

                // Botón eliminar siempre visible
                Button(onClick = {
                    existingContact?.let {
                        scope.launch {
                            viewModel.deleteContact(it)
                            snackbarHostState.showSnackbar("Contacto eliminado")
                            navController.popBackStack()
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red), modifier = Modifier.fillMaxWidth()) {
                    Text("Eliminar", color = Color.White)
                }
            }
        }
    )
}
