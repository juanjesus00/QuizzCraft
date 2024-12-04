package uiUserInfo

import android.util.Log
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
                            createdQuiz = document.getString("createdQuiz").toString(),
                            passQuiz = document.getString("passQuiz").toString(),
                            lastQuizzes = document.get("lastQuizzes") as? MutableList<String> ?: mutableListOf()
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
}