package com.example.fuelfinder.data.model

import com.google.gson.annotations.SerializedName

data class FuelStation(
    @SerializedName("Id")
    val id: String,
    @SerializedName("Nome")
    val nome: String,
    @SerializedName("Marca")
    val marca: String,
    @SerializedName("Localidade")
    val localidade: String,
    @SerializedName("Combustiveis")
    val combustiveis: List<FuelPrice>
)

data class FuelPrice(
    @SerializedName("TipoCombustivel")
    val tipoCombustivel: String,
    @SerializedName("Preço")
    val preco: String
)

data class DgegResponse(
    @SerializedName("resultado") // Wrapper assumed based on typical DGEG JSON
    val resultado: List<FuelStation>
)
