package com.example.principal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.principal.data.local.dao.ContactDao
import com.example.principal.data.local.entity.ContactEntity

@Database(
    entities = [ContactEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao
}
