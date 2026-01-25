package com.example.principal.data.repository

import com.example.principal.data.local.dao.ContactDao
import com.example.principal.data.local.entity.ContactEntity
import com.example.principal.data.remote.datasource.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(

    private val dao: ContactDao,
    private val apiService: ApiService

) {

    /**
     * Descarga contactos desde la API y los guarda en Room.
     */
    suspend fun importContacts() {

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
                image = user.picture.large
            )

            dao.insertContact(entity)
        }
    }

    /**
     * Devuelve todos los contactos guardados.
     */
    fun getContacts(): Flow<List<ContactEntity>> {
        return dao.getAllContacts()
    }

    /**
     * Borra un contacto.
     */
    suspend fun deleteContact(contact: ContactEntity) {
        dao.deleteContact(contact)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
