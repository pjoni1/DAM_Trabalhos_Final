package com.example.fuelfinder.data.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockFuelApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val city = request.url.queryParameter("city") ?: "Lisbon"

        val jsonResponse = """
            {
              "stations": [
                {
                  "id": "1",
                  "name": "Galp",
                  "address": "Av. da Liberdade 100",
                  "city": "$city",
                  "latitude": 38.7202,
                  "longitude": -9.1456,
                  "gasolinePrice": 1.729,
                  "dieselPrice": 1.639,
                  "lastUpdated": "2023-10-27T10:00:00Z"
                },
                {
                  "id": "2",
                  "name": "Repsol",
                  "address": "Rua Augusta 200",
                  "city": "$city",
                  "latitude": 38.7121,
                  "longitude": -9.1370,
                  "gasolinePrice": 1.689,
                  "dieselPrice": 1.589,
                  "lastUpdated": "2023-10-27T09:30:00Z"
                },
                {
                  "id": "3",
                  "name": "BP",
                  "address": "Marquês de Pombal",
                  "city": "$city",
                  "latitude": 38.7253,
                  "longitude": -9.1500,
                  "gasolinePrice": 1.739,
                  "dieselPrice": 1.669,
                  "lastUpdated": "2023-10-27T08:15:00Z"
                },
                {
                  "id": "4",
                  "name": "Prio",
                  "address": "Avenida Almirante Reis 50",
                  "city": "$city",
                  "latitude": 38.7258,
                  "longitude": -9.1345,
                  "gasolinePrice": 1.659,
                  "dieselPrice": 1.549,
                  "lastUpdated": "2023-10-27T11:45:00Z"
                }
              ]
            }
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(jsonResponse.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}
