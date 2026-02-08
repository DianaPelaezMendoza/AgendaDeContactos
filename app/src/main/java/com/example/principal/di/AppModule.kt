package com.example.principal.di

import android.app.Application
import androidx.room.Room
import com.example.principal.data.local.dao.ContactDao
import com.example.principal.data.local.database.AppDatabase
import com.example.principal.data.remote.datasource.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // URL base
    @Provides
    @Singleton
    fun provideBaseUrl(): String = "https://randomuser.me/api/"

    // Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ApiService
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // Room Database
    @Suppress("DEPRECATION")
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
         return Room.databaseBuilder(app, AppDatabase::class.java, "contacts_db")
             .fallbackToDestructiveMigration()
             .build()
    }
    // DAO
    @Provides
    @Singleton
    fun provideContactDao(db: AppDatabase): ContactDao {
        return db.contactDao()
    }
}
