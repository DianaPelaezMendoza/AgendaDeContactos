package com.example.principal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.principal.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    // CREATE / UPDATE (si existe el mismo id)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    // UPDATE explícito
    @Update
    suspend fun updateContact(contact: ContactEntity)

    // READ
    @Query("SELECT * FROM contacts ORDER BY id DESC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE source = :source ORDER BY id DESC")
    fun getContactsBySource(source: ContactSource): Flow<List<ContactEntity>>

    // DELETE uno
    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    // DELETE todos
    @Query("DELETE FROM contacts")
    suspend fun deleteAll()

}
