package com.example.principal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.principal.data.local.dao.ContactSource

/**
 * Entidad que representa un contacto en la base de datos.
 *
 * @property id Identificador único del contacto.
 * @property firstName Nombre del contacto.
 * @property lastName Apellido del contacto.
 * @property city Ciudad del contacto.
 * @property state Estado o región del contacto.
 * @property phone Teléfono del contacto.
 * @property email Correo electrónico del contacto.
 * @property thumbnail URL o path de la miniatura.
 * @property image URL o path de la imagen completa.
 * @property source Fuente del contacto (importado o creado manualmente).
 */


@Entity(tableName = "contacts")
data class ContactEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firstName: String,
    val lastName: String,

    val city: String,
    val state: String,

    val phone: String,
    val email: String,

    val thumbnail: String = "",
    val image: String = "",

    val source: ContactSource
)
