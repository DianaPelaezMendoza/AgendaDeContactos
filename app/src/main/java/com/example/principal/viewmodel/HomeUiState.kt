package com.example.principal.viewmodel

import com.example.principal.data.local.entity.ContactEntity

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val contacts: List<ContactEntity>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
