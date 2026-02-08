package com.example.principal.viewmodel

import androidx.lifecycle.SavedStateHandle
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
 * ViewModel que maneja los detalles de un contacto específico.
 *
 * Carga el contacto al inicializarse según el `contactId` recibido.
 * Permite eliminar el contacto cargado.
 *
 * @param repository Repositorio para acceder a los datos de los contactos.
 * @param savedStateHandle Maneja argumentos pasados a la pantalla.
 */

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ContactRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contactId: Int = checkNotNull(savedStateHandle["contactId"])
    private val _contact = MutableStateFlow<ContactEntity?>(null)
    val contact: StateFlow<ContactEntity?> = _contact

    init {
        viewModelScope.launch {
            _contact.value = repository.getContactById(contactId)
        }
    }

    fun deleteContact(c: ContactEntity) {
        viewModelScope.launch {
            _contact.value?.let { repository.deleteContact(it) }
        }
    }
}


