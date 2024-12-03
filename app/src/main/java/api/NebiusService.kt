package api

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NebiusService(private val apiKey: String) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(500, TimeUnit.SECONDS)
        .readTimeout(500, TimeUnit.SECONDS)
        .writeTimeout(500, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
            chain.proceed(request)
        }
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            val responseBody = response.body?.string() ?: ""
            println("Request: ${request.method} ${request.url}")
            println("Headers: ${request.headers}")
            println("Body: ${request.body}")
            println("Response: ${response.code} $responseBody")
            // Extraer el contenido usando Gson
            try {
                val gson = Gson()
                val responseObject = gson.fromJson(responseBody, NebiusResponse::class.java)
                // Obtener solo el 'content' del primer mensaje
                val generatedContent = responseObject.choices.firstOrNull()?.message?.content
                if (!generatedContent.isNullOrEmpty()) {
                    println("Generated Content: $generatedContent")
                }
            } catch (e: Exception) {
                println("Error al parsear la respuesta: ${e.message}")
            }

            // Crear una nueva respuesta con el cuerpo original
            response.newBuilder().body(responseBody.toResponseBody(response.body?.contentType())).build()

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

            // Obtener el 'content' del primer elemento en 'choices'
            response.choices.firstOrNull()?.message?.content ?: "No response"

        } catch (e: retrofit2.HttpException) {
            "HTTP Error: ${e.code()} - ${e.response()?.errorBody()?.string()}"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}