package com.mip2.imageviewer.api

import com.mip2.imageviewer.model.ImageItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    /**
     * Endpoint padrão para chamar fotografias aleatórias / recentes na API.
     * Utiliza funções "suspend" suportadas intrinsecamente pelo cliente do Retrofit para Kotlin Coroutines.
     */
    @GET("photos")
    suspend fun getPhotos(
        @Query("client_id") apiKey: String, // Authentication key enviada em Query
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): List<ImageItem>
}
