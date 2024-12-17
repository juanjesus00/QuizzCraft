package SplashVideo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _isLoadingComplete = MutableStateFlow(false)
    val isLoadingComplete: StateFlow<Boolean> = _isLoadingComplete

    init {
        preloadData()
    }

    private fun preloadData() {
        viewModelScope.launch {
            // Simula la precarga (por ejemplo, obtener datos de red o caché local)
            delay(5000) // Simula una carga que tarda 3 segundos
            _isLoadingComplete.value = true
        }
    }
}