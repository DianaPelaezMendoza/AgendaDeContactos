package com.example.principal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.repository.ContactRepository
import com.example.principal.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado de UI para HomeScreen
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val contacts: List<ContactEntity>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val contactsFlow: Flow<List<ContactEntity>> = repository.getContacts()

    init {
        // Inicialmente cargamos contactos desde DB
        viewModelScope.launch {
            contactsFlow.collect { list ->
                _uiState.value = HomeUiState.Success(list)
            }
        }
    }

    /**
     * Importa contactos desde la API y los guarda en la DB
     */
    fun importContacts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                repository.importContacts()
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error al importar contactos")
            }
        }
    }

    /**
     * Elimina un contacto de la DB
     */
    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }
}
