package uiLogin

import android.os.Bundle
import com.example.myapplication.MainActivity
import com.google.firebase.auth.FirebaseAuth


class loginbacked(mainActivity: MainActivity) {
    private lateinit var auth: FirebaseAuth
    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Llama a la función de éxito
                    onSuccess()
                } else {
                    // Llama a la función de error con un mensaje
                    onFailure(task.exception?.message ?: "Error de autenticación")
                }
            }
    }
}


