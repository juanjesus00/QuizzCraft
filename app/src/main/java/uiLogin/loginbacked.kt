package uiLogin

import android.os.Bundle
import com.example.myapplication.MainActivity
import com.google.firebase.auth.FirebaseAuth
import android.util.Log


class loginbacked(mainActivity: MainActivity) {
    private lateinit var auth: FirebaseAuth
    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Loginbackend", "Inicio de sesión exitoso")
                        onSuccess()
                    } else {
                        Log.d("Loginbackend", "Error al iniciar sesion: ")
                        onFailure(task.exception?.message ?: "Error desconocido")
                    }
                }
        } catch (e: Exception) {
            Log.d("Loginbackend", "Se ha inicado sesion: ")
            onFailure("Error de autenticación: ${e.message}")
            Log.e("loginError", "Error en el inicio de sesion")
        }
    }
}


