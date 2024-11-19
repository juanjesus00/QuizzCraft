package api

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NebiusService(private val apiKey: String) {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
            chain.proceed(request)
        }
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            println("Request: ${request.method} ${request.url}")
            println("Headers: ${request.headers}")
            println("Body: ${request.body}")
            println("Response: ${response.code} ${response.body?.string()}")
            response
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.studio.nebius.ai/v1/") // Base URL de la API de Nebius
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(NebiusApi::class.java)

    suspend fun generateText(prompt: String): String {
        val request = NebiusRequest(
            messages = listOf(
                Message(role = "user", content = prompt) // Rol del mensaje y contenido.
            )
        )

        return try {
            val response = api.generateText(request)
            response.results.firstOrNull()?.generated_text ?: "No response"
        } catch (e: retrofit2.HttpException) {
            "HTTP Error: ${e.code()} - ${e.response()?.errorBody()?.string()}"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}