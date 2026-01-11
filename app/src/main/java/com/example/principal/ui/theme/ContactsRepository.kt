package com.example.principal.ui.theme

import com.example.principal.R

object ContactsRepository {

    val contacts = listOf(
        Contact(
            id = 1,
            name = "Juan Pérez",
            info = "Desarrollador Android",
            phone = "111-222-333",
            email = "juan@mail.com",
            imageRes = R.drawable.avatar_gato
        ),
        Contact(
            id = 2,
            name = "María Gómez",
            info = "Diseñadora UX",
            phone = "444-555-666",
            email = "maria@mail.com",
            imageRes = R.drawable.avatar_perro
        ),
        Contact(
            id = 3,
            name = "Carlos López",
            info = "Project Manager",
            phone = "777-888-999",
            email = "carlos@mail.com",
            imageRes = R.drawable.avatar_conejo
        )
    )

    fun getContactById(id: Int): Contact? {
        return contacts.find { it.id == id }
    }
}
