package api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class OpenAIViewModel : ViewModel() {
    fun generateText(apiKey: String, prompt: String, onResponse: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val service = OpenAIService(apiKey)
                val responseText = service.generateText(prompt)
                onResponse(responseText)
            } catch (e: Exception) {
                if (e.message?.contains("429") == true) {
                    onResponse("Error: Too many requests. Please wait and try again.")
                    delay(60000) // Espera 60 segundos antes de permitir nuevos intentos.
                } else {
                    onResponse("Error: ${e.message}")
                }
            }
        }
    }
}
