package com.example.principal.viewmodel

import androidx.compose.remote.creation.first
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    // Fetch the contact if we're editing it
    fun loadContact(contactId: Int) {
        viewModelScope.launch {
            // Now, directly assign the contact to _contact, and the UI will collect the data
            _contact.value = repository.getContactById(contactId)
        }
    }

    fun saveContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.insertOrUpdate(contact)
        }
    }

    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }
}
