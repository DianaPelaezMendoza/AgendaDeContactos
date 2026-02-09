package com.example.principal.data.remote.datasource

import com.example.principal.data.remote.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(".")
    suspend fun getContacts(
        @Query("results") results: Int
    ): ApiResponse
}
