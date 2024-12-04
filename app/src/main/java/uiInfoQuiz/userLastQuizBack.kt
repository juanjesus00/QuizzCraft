package uiInfoQuiz

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun userAddLastQuiz(quizId: String) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.let { user ->
        val uid = user.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("Usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                var lastQuizzes =
                    document.get("lastQuizzes") as? MutableList<String> ?: mutableListOf()
                if (!(lastQuizzes.contains(quizId))) {
                    if (lastQuizzes.size == 5) {
                        lastQuizzes.removeAt(0)
                    }
                    lastQuizzes.add(quizId)

                    val updateLastQuizzes = mapOf("lastQuizzes" to lastQuizzes)
                    db.collection("Usuarios").document(uid).update(updateLastQuizzes)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Usuario actualizado exitosamente")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error al actualizar el usuario", e)
                        }
                }

            }
    }
}

fun getLastQuizByUserId(onResult: (MutableList<String>) -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.let { user ->
        val uid = user.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("Usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                var lastQuizzes =
                    document.get("lastQuizzes") as? MutableList<String> ?: mutableListOf()
                onResult(lastQuizzes)
            }
    }
}
