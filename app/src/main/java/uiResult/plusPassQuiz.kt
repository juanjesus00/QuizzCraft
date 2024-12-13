package uiResult

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

fun plusPassQuizUser() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.let { user ->
        val uid = user.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("Usuarios").document(uid)
            .update("passQuiz", FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("Firestore", "Campo 'passQuiz' incrementado exitosamente")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error al incrementar 'createdQuiz'", e)
            }
    }
}