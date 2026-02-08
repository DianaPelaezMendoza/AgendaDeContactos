package com.example.principal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


/**
 * ViewModel para la pantalla de creación o edición de contactos.
 *
 * Mantiene el estado del contacto que se está editando o creando.
 * Permite:
 *  - Cargar un contacto existente (`loadContact`)
 *  - Guardar un contacto (`saveContact`)
 *  - Eliminar un contacto (`deleteContact`)
 *
 * @param repository Repositorio que maneja la persistencia de los contactos.
 */
@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _contact = MutableStateFlow<ContactEntity?>(null)
    val contact: StateFlow<ContactEntity?> = _contact

    // Load contact by ID and collect the Flow to update UI
    fun loadContact(contactId: Int) {
        viewModelScope.launch {
            // Collect the flow to get the contact
            repository.getContactById(contactId).collect { contact ->
                _contact.value = contact
            }
        }
    }

    // Save or update the contact to the repository
    fun saveContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.insertOrUpdate(contact)
        }
    }

    // Delete the contact from the repository
    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }
}
