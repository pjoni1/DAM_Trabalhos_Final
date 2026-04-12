package com.mip2.imageviewer.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de dados encapsulado a mapear a API (como o Unsplash).
 * Cumpre a estrutura exigida em (04_data_model.md): id, url, title.
 */
data class ImageItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("description")
    val title: String?,

    // A API embutirá a resolução dentro do sub-objeto URLs.
    @SerializedName("urls")
    private val urls: ImageUrls
) {
    // Propriedade Helper calculada para providenciar o `url` limpo exigido na doc.
    val url: String
        get() = urls.regular
}

data class ImageUrls(
    @SerializedName("regular")
    val regular: String
)
