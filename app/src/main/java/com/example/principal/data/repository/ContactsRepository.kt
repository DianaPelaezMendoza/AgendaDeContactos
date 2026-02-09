package com.example.principal.data.repository

import com.example.principal.data.local.dao.ContactDao
import com.example.principal.data.local.dao.ContactSource
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.remote.datasource.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val dao: ContactDao,
    private val apiService: ApiService
) {

    suspend fun importContacts(limit: Int) {
        val response = apiService.getContacts()
        response.results.forEach { user ->
            val entity = ContactEntity(
                firstName = user.name.first,
                lastName = user.name.last,
                city = user.location.city,
                state = user.location.state,
                phone = user.phone,
                email = user.email,
                thumbnail = user.picture.thumbnail,
                image = user.picture.large,
                source = ContactSource.IMPORTED
            )
            dao.insertContact(entity)
        }
    }

    fun getContacts(): Flow<List<ContactEntity>> = dao.getAllContacts()

    suspend fun deleteContact(contact: ContactEntity) = dao.deleteContact(contact)

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun insertOrUpdate(contact: ContactEntity) = dao.insertContact(contact)

    fun getImportedContacts(): Flow<List<ContactEntity>> =
        dao.getContactsBySource(ContactSource.IMPORTED)

    fun getCreatedContacts(): Flow<List<ContactEntity>> =
        dao.getContactsBySource(ContactSource.CREATED)

    suspend fun getContactById(id: Int): ContactEntity? {
        return dao.getContactById(id)  // Call the suspend function from DAO directly
    }
}


