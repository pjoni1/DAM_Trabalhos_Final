package com.mip2.imageviewer.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton responsável por centralizar a comunicação à infraestrutura e configuração do cliente Retrofit.
 */
object ApiClient {
    private const val BASE_URL = "https://api.unsplash.com/"

    // lazy instantiation -> só inicia e aloca memória na primeira invocação do retrofitService
    val retrofitService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Gson tratará do parse automático entre JSON Node Tree e Object de ImageItem
            .addConverterFactory(GsonConverterFactory.create()) 
            .build()
            .create(ApiService::class.java)
    }
}
