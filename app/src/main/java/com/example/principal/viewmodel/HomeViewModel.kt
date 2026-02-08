package com.example.principal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val contactsFlow: Flow<List<ContactEntity>> = repository.getContacts()

    init {
        viewModelScope.launch {
            contactsFlow.collect { list ->
                _uiState.value = HomeUiState.Success(list)
            }
        }
    }

    fun importContactsFromApi(limit: Int) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                repository.importContacts(limit)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error al importar contactos")
            }
        }
    }

    fun addOrUpdateContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.insertOrUpdate(contact)
        }
    }

    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch { repository.deleteContact(contact) }
    }

    suspend fun getContactByIdSync(id: Int): ContactEntity? {
        return repository.getContactById(id)
    }

    fun getImportedContacts(): Flow<List<ContactEntity>> = repository.getImportedContacts()
    fun getCreatedContacts(): Flow<List<ContactEntity>> = repository.getCreatedContacts()
}

