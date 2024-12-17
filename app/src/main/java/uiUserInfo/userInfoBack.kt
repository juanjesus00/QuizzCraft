package uiUserInfo

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class userInfoBack : ViewModel(){
    fun getInfoUser(onResult: (Map<String, Any>?) -> Unit){
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val uid = user.uid
            val db = FirebaseFirestore.getInstance()

            db.collection("Usuarios").document(uid).get()
                .addOnSuccessListener { document ->
                    if(document != null && document.exists()){
                        //val user = arrayOf(document.getString("PerfilImage"), document.getString("userName"), document.getString("email"))
                        val user = model.User(
                            userId = document.getString("user_id").toString(),
                            email = document.getString("email").toString(),
                            userName = document.getString("userName").toString(),
                            profileImageUrl = document.getString("PerfilImage").toString(),
                            createdQuiz = document.getLong("createdQuiz")?.toInt() ?: 0,
                            passQuiz = document.getLong("passQuiz")?.toInt() ?: 0,
                            lastQuizzes = document.get("lastQuizzes") as? MutableList<String> ?: mutableListOf(),
                            password = document.getString("password").toString()

                        ).toMap()
                        onResult(user)
                    }else{
                        onResult(null)
                    }
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    onResult(null)
                }
        } ?: run{
            onResult(null)
        }
    }

    fun deleteUser(
        context: Context
    ):Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            return try {
                // Eliminar los datos asociados al usuario de Firestore
                val db = FirebaseFirestore.getInstance()
                val userId = currentUser.uid

                // Eliminar datos de Firestore
                db.collection("Usuarios").document(userId).delete()
                    .addOnSuccessListener {
                        // Luego de eliminar los datos, eliminamos el usuario
                        currentUser.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Usuario eliminado exitosamente
                                    Toast.makeText(
                                        context,
                                        "El usuario se ha eliminado con éxito",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    // Manejar error al eliminar el usuario
                                    task.exception?.let {
                                        Toast.makeText(
                                            context,
                                            "El usuario no se ha podido eliminar",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                    }
                    .addOnFailureListener { e ->
                        // Error al eliminar los datos de Firestore
                        Toast.makeText(
                            context,
                            "Error al eliminar los datos del usuario: ${e.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                        false
                    }
                true // Si todo salió bien
            } catch (e: Exception) {
                // Manejo de excepciones
                Toast.makeText(
                    context,
                    "Ocurrió un error: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        } else {
            // Si no hay un usuario autenticado
            Toast.makeText(
                context,
                "No hay un usuario autenticado",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

    }

}