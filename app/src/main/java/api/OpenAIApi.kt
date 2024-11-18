package api

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun generateText(@Body request: OpenAIRequest): OpenAIResponse
}