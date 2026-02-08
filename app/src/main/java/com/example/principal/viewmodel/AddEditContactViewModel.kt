package com.example.principal.viewmodel

import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.repository.ContactRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _contact = MutableStateFlow<ContactEntity?>(null)
    val contact: StateFlow<ContactEntity?> = _contact

    // Cargar un contacto existente (para editar)
    fun loadContact(contacto: ContactEntity?) {
        _contact.value = contacto
    }

    // Guardar un contacto nuevo o actualizar uno existente
    fun saveContact(contacto: ContactEntity) {
        viewModelScope.launch {
            repository.insertOrUpdate(contacto)
        }
    }

}
