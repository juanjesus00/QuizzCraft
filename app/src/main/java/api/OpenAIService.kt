package api

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenAIService(private val apiKey: String) {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenAIApi::class.java)

    suspend fun generateText(prompt: String): String {
        val request = OpenAIRequest(
            messages = listOf(
                Message(role = "user", content = prompt)
            )
        )

        val response = api.generateText(request)
        return response.choices.firstOrNull()?.message?.content ?: "No response"
    }
}