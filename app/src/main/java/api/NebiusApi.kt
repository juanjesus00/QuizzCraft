package api

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NebiusApi {
    @POST("/v1/chat/completions") // Cambiar al endpoint correcto de Nebius
    @Headers("Content-Type: application/json")
    suspend fun generateText(@Body request: NebiusRequest): NebiusResponse
}