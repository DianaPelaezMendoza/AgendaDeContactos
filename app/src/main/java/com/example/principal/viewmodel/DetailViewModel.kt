package com.example.principal.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    fun deleteContact() {
        viewModelScope.launch {
            _contact.value?.let { repository.deleteContact(it) }
        }
    }
}
