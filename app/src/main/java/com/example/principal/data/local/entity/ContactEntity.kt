package com.example.principal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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

    val thumbnail: String,
    val image: String
)
