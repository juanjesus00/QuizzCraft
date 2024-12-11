package uiLogin

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import uiUserInfo.userInfoBack
import java.util.Properties

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
                            crateUser(name, email, "", password)
                            onSuccess()
                        }else{
                            Log.d("loginbackend", "La creacion de usuarios falló: ${task.result.toString()}")
                        }
                        _loading.value = false
                    }
            }

        }
    }
    fun SingInWithGoogleCredential(
        credentialToken: AuthCredential,
        credential: SignInCredential,
        password: String,
        home: () -> Unit
    )=viewModelScope.launch{
        try{
            auth.signInWithCredential(credentialToken)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Log.d("loginGoogle", "login con google exitoso")
                        println("contraseña: "+ "${credential.password}")
                        val db = FirebaseFirestore.getInstance()
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        val userRef = db.collection("Usuarios").document(uid!!)
                        userRef.get().addOnSuccessListener { document ->
                            if(document.exists()){
                                Log.d("loginGoogle", "El usuario ya existe")
                            }else{
                                crateUser(
                                    displayName = credential.givenName?:"unknown",
                                    email = credential.id,
                                    profileImage = credential.profilePictureUri.toString(),
                                    password = password
                                )
                            }
                        }.addOnFailureListener { e ->
                            Log.e("FirebaseAuth", "Error al crear usuario: ${e.localizedMessage}")
                        }
                        home()

                    }

                }
                .addOnFailureListener {
                    Log.d("loginGoogle", "login con google Error")
                }
        }catch (ex:Exception){
            Log.d("loginGoogle", "Excepcion de login con Google: " + "${ex.localizedMessage}")
        }
    }
    fun linkEmailAndPassword(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val credential = EmailAuthProvider.getCredential(email, password)

            user.linkWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FirebaseAuth", "Método de correo y contraseña vinculado exitosamente")
                        onSuccess()
                    } else {
                        Log.e("FirebaseAuth", "Error al vincular credenciales: ${task.exception?.localizedMessage}")
                        onFailure(task.exception?.localizedMessage ?: "Error desconocido")
                    }
                }
        } else {
            onFailure("No hay un usuario autenticado")
        }
    }


    private fun crateUser(
        displayName: String,
        email: String,
        profileImage: String,
        password: String
    ) {
        val userId = auth.currentUser?.uid
        val user = model.User(
            userId = userId.toString(),
            userName = displayName,
            profileImageUrl = profileImage,
            email = email,
            createdQuiz = "0",
            passQuiz = "0",
            lastQuizzes = mutableListOf(),
            password = password
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

    fun editUser(userName: String, email: String, password: String, context: Context, selectImageUri: Uri?, onSuccess: () -> Unit, showpassword: String?) = viewModelScope.launch{
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
                    if(selectImageUri != null && email != "" && userName != "" && password != showpassword){
                        storageRef.putFile(selectImageUri)
                            .addOnSuccessListener { taskSnapshot ->
                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val imageUri = uri.toString()

                                    val userUpdates = mapOf(
                                        "email" to email,
                                        "userName" to userName,
                                        "PerfilImage" to imageUri,
                                        "password" to password
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






