package com.example.fuelfinder.data.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockFuelApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val localidade = request.url.queryParameter("localidade") ?: "Lisboa"

        val jsonResponse = when(localidade.lowercase()) {
            "lisboa" -> """
                {
                  "resultado": [
                    { "Id": "1", "Nome": "Galp", "Marca": "Galp", "Localidade": "Lisboa", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.729" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.639" } ] },
                    { "Id": "2", "Nome": "Repsol", "Marca": "Repsol", "Localidade": "Lisboa", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.689" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.589" } ] },
                    { "Id": "3", "Nome": "BP", "Marca": "BP", "Localidade": "Lisboa", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.739" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.669" } ] },
                    { "Id": "4", "Nome": "Prio", "Marca": "Prio", "Localidade": "Lisboa", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.659" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.549" } ] }
                  ]
                }
            """.trimIndent()
            "porto" -> """
                {
                  "resultado": [
                    { "Id": "5", "Nome": "Cepsa", "Marca": "Cepsa", "Localidade": "Porto", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.699" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.559" } ] },
                    { "Id": "6", "Nome": "Auchan", "Marca": "Auchan", "Localidade": "Porto", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.619" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.499" } ] },
                    { "Id": "7", "Nome": "Galp", "Marca": "Galp", "Localidade": "Porto", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.719" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.629" } ] }
                  ]
                }
            """.trimIndent()
            else -> """
                {
                  "resultado": [
                    { "Id": "8", "Nome": "Intermarché", "Marca": "Intermarché", "Localidade": "$localidade", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.609" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.489" } ] },
                    { "Id": "9", "Nome": "BP", "Marca": "BP", "Localidade": "$localidade", "Combustiveis": [ { "TipoCombustivel": "Gasolina 95", "Preço": "1.759" }, { "TipoCombustivel": "Gasóleo Simples", "Preço": "1.679" } ] }
                  ]
                }
            """.trimIndent()
        }

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
