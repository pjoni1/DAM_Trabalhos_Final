package com.mip2.imageviewer.repository

import com.mip2.imageviewer.api.ApiService
import com.mip2.imageviewer.model.ImageItem

/**
 * Ponto de contacto que expõe informações de Modelos baseadas na fonte de dados providenciada pela Web API.
 */
class ImageRepository(private val apiService: ApiService) {
    
    // Método que despachará a execução para as corrotinas
    suspend fun getImages(apiKey: String): List<ImageItem> {
        return apiService.getPhotos(apiKey = apiKey, page = 1, perPage = 30)
    }
}
