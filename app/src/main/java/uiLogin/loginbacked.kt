package uiLogin

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class loginbacked: ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private var _loading = MutableLiveData(false)
    fun signIn(
        email: String,
        password: String,
        context: Context,
        onSuccess: () -> Unit,
        onErrorAction: () -> Unit
    ) = viewModelScope.launch {
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
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    onErrorAction()
                    Toast.makeText(context, "Inicio de se sesio erroneo la contraseña o el email son incorrectos", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Log.d("Loginbackend", "Error de inicio: ${e.message}")
        }
    }
    /*fun signInWithGoogleCredential(credential: AuthCredential, home:() -> Unit)=viewModelScope.launch{
        try {
            auth.signInWithCredential(credential)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        Log.d("GoogleLogin", "Login exitoso")
                        home()
                    }
                }
        }

    }*/
    fun register(
        email: String,
        password: String,
        context: Context,
        name: String,
        onSuccess: () -> Unit
    ) = viewModelScope.launch{
        if (_loading.value == false){ //no se esta creando usuarios actualmente
            if(password.length < 6){
                Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            }else{
                _loading.value = true

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){
                            crateUser(name, email)
                            onSuccess()
                        }else{
                            Log.d("loginbackend", "La creacion de usuarios falló: ${task.result.toString()}")
                        }
                        _loading.value = false
                    }
            }

        }
    }


    private fun crateUser(displayName: String, email: String) {
        val userId = auth.currentUser?.uid
        val user = model.User(
            userId = userId.toString(),
            userName = displayName,
            profileImageUrl = "",
            email = email,
            createdQuiz = "0",
            passQuiz = "0",
            lastQuizzes = mutableListOf()
        ).toMap()

        FirebaseFirestore.getInstance().collection("Usuarios")
            .document(userId.toString())
            .set(user)
            .addOnSuccessListener {
                Log.d("loginbackend", "Creado ${it}")
            }.addOnFailureListener {
                Log.d("loginbackend", "Error ${it}")
            }
    }

    fun editUser(userName: String, email: String, password: String, context: Context, selectImageUri: Uri?, onSuccess: () -> Unit) = viewModelScope.launch{
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (_loading.value == false){ //no se esta creando usuarios actualmente
            if(password.length < 6){
                Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            }else{
                _loading.value = true
                currentUser?.let{ user ->
                    val uid = user.uid
                    val db = FirebaseFirestore.getInstance()
                    val storageRef = FirebaseStorage.getInstance().reference.child("userImage/$uid.jpg")
                    if(selectImageUri != null && email != "" && userName != ""){
                        storageRef.putFile(selectImageUri)
                            .addOnSuccessListener { taskSnapshot ->
                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val imageUri = uri.toString()

                                    val userUpdates = mapOf(
                                        "email" to email,
                                        "userName" to userName,
                                        "PerfilImage" to imageUri
                                    )
                                    db.collection("Usuarios").document(uid).update(userUpdates)
                                        .addOnSuccessListener {
                                            Log.d("Firestore", "Usuario actualizado exitosamente")
                                            onSuccess()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w("Firestore", "Error al actualizar el usuario", e)
                                        }
                                }.addOnFailureListener { e ->
                                    Log.w("Storage", "Error al obtener la URL de descarga", e)
                                }

                            }.addOnFailureListener { e ->
                                Log.w("Storage", "Error al subir la imagen", e)
                            }
                    }else if(email != "" && userName != ""){
                        val userUpdates = mapOf(
                            "email" to email,
                            "userName" to userName
                        )
                        db.collection("Usuarios").document(uid).update(userUpdates)
                            .addOnSuccessListener { document ->
                                Log.d("Firestore", "Usuario actualizado exitosamente")
                                onSuccess()
                            }
                            .addOnFailureListener{ e ->
                                Log.w("Firestore", "Error al actualizar el usuario", e)
                            }
                    } else {
                        Toast.makeText(context, "Almenos Rellena email y nombre de usuario", Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }
    }
}







