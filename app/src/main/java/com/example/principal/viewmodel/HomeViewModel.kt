package com.example.principal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject



// ----------------------------------
// Estado de UI para HomeScreen
// ----------------------------------
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val contacts: List<ContactEntity>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ContactRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Flujo de contactos desde Room
    val contactsFlow: Flow<List<ContactEntity>> = repository.getContacts()

    init {
        // Observamos Room y actualizamos la UI automáticamente
        viewModelScope.launch {
            contactsFlow.collect { list ->
                _uiState.value = HomeUiState.Success(list)
            }
        }
    }



    // ----------------------------------
    // IMPORTAR CONTACTOS DESDE API CON LÍMITE, 30 lim
    // ----------------------------------
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

    // ----------------------------------
    // CREAR O EDITAR CONTACTO (Room decide)
    // ----------------------------------
    fun addOrUpdateContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.insertOrUpdate(contact)
        }
    }

    // ----------------------------------
    // ELIMINAR CONTACTO
    // ----------------------------------
    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }
}
