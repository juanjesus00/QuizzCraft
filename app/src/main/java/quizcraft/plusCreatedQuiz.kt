package quizcraft

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

fun plusCreatedQuizUser()  {
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.let { user ->
        val uid = user.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("Usuarios").document(uid)
            .update("createdQuiz", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("Firestore", "Campo 'createdQuiz' incrementado exitosamente")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error al incrementar 'createdQuiz'", e)
            }
    }
}