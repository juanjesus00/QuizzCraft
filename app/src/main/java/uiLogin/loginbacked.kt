package uiLogin

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch


class loginbacked: ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private var _loading = MutableLiveData(false)
    fun signIn(email: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Loginbackend", "Inicio de sesión exitoso")
                        onSuccess()
                    } else {
                        Log.d("Loginbackend", "Error al iniciar sesion: ${task.result.toString()}")
                    }
                }
        } catch (e: Exception) {
            Log.d("Loginbackend", "Error de inicio: ${e.message}")
        }
    }

    fun register(email: String, password: String, context: Context, onSuccess: () -> Unit) = viewModelScope.launch{
        if (_loading.value == false){ //no se esta creando usuarios actualmente
            if(password.length < 6){
                Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            }else{
                _loading.value = true

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){
                            onSuccess()
                        }else{
                            Log.d("loginbackend", "La creacion de usuarios falló: ${task.result.toString()}")
                        }
                        _loading.value = false
                    }
            }

        }
    }
}


