package api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NebiusViewModel : ViewModel() {
    fun generateText(apiKey: String, prompt: String, onResponse: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val service = NebiusService(apiKey)
                val responseText = service.generateText(prompt)
                onResponse(responseText)
            } catch (e: Exception) {
                onResponse("Error: ${e.message}")
            }
        }
    }
}