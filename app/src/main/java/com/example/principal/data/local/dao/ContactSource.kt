package com.example.principal.data.local.dao

/**
 * Fuente de un contacto.
 *
 * IMPORTED → Contacto traído desde la API.
 * CREATED → Contacto creado manualmente en la app.
 */
enum class ContactSource {
    IMPORTED,
    CREATED
}
