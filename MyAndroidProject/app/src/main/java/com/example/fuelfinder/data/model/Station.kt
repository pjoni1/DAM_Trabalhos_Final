package com.example.fuelfinder.data.model

import com.google.gson.annotations.SerializedName

data class Station(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("gasolinePrice")
    val gasolinePrice: Double?, // Example: 1.659
    @SerializedName("dieselPrice")
    val dieselPrice: Double?,   // Example: 1.549
    @SerializedName("lastUpdated")
    val lastUpdated: String
)

data class StationsResponse(
    @SerializedName("stations")
    val stations: List<Station>
)
