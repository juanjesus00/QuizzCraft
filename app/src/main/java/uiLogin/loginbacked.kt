package uiLogin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.InputType
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.itextpdf.text.xml.xmp.DublinCoreProperties.setSubject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

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

    fun editUser(userName: String, email: String, password: String, context: Context, selectImageUri: Uri?, onSuccess: () -> Unit, showpassword: String?, passwordchange: String) = viewModelScope.launch{
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (_loading.value == false){ //no se esta creando usuarios actualmente
            if(password.length < 6 || password != showpassword && passwordchange == ""){
                if(password.length < 6 && passwordchange == ""){
                    Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                }else if(password != showpassword && passwordchange == ""){
                    Toast.makeText(context, "la contraseña es incorrecta", Toast.LENGTH_SHORT).show()
                }
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
                    }else if(email != "" || userName != ""){
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
                    }else{
                        Toast.makeText(context, "Almenos Rellena email y nombre de usuario", Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }
    }

    /*fun ChangePassword(changePassword: String, context: Context, currentPassword:String?, onSuccess: () -> Unit)= viewModelScope.launch {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (_loading.value == false) { // No se está procesando otro cambio
            if (changePassword.length < 6) {
                Toast.makeText(
                    context,
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                _loading.value = true
                currentUser?.let { user ->
                    // Reautenticación usando la contraseña actual
                    val credential =
                        EmailAuthProvider.getCredential(user.email!!, currentPassword!!)
                    user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            // Cambiar contraseña en Firebase Authentication
                            user.updatePassword(changePassword)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        // Actualizar Firestore para mantener sincronización
                                        val db = FirebaseFirestore.getInstance()
                                        val userUpdates = mapOf(
                                            "password" to changePassword
                                        )
                                        db.collection("Usuarios").document(user.uid)
                                            .update(userUpdates)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "Firestore",
                                                    "Usuario actualizado exitosamente en Firestore"
                                                )
                                                Toast.makeText(
                                                    context,
                                                    "Contraseña cambiada exitosamente",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                onSuccess()
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w(
                                                    "Firestore",
                                                    "Error al actualizar Firestore",
                                                    e
                                                )
                                                Toast.makeText(
                                                    context,
                                                    "Error al actualizar la base de datos",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    } else {
                                        Log.w(
                                            "FirebaseAuth",
                                            "Error al cambiar contraseña en Authentication",
                                            updateTask.exception
                                        )
                                        Toast.makeText(
                                            context,
                                            "Error al cambiar la contraseña",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    _loading.value = false
                                }
                        } else {
                            Log.w("FirebaseAuth", "Reautenticación fallida", reauthTask.exception)
                            Toast.makeText(
                                context,
                                "Reautenticación fallida. Verifique la contraseña actual.",
                                Toast.LENGTH_SHORT
                            ).show()
                            _loading.value = false
                        }
                    }
                } ?: run {
                    Log.w("FirebaseAuth", "No hay usuario autenticado")
                    Toast.makeText(
                        context,
                        "No se encontró un usuario autenticado",
                        Toast.LENGTH_SHORT
                    ).show()
                    _loading.value = false
                }
            }
        }
    }*/

    /*private var verificationCode: String? = null

    fun sendVerificationEmail(
        userEmail: String,
        context: Context
    ) {
        verificationCode = (100000..999999).random().toString() // Genera un código aleatorio de 6 dígitos

        val subject = "Confirmación de cambio de contraseña"
        val message = "Hola, usa este código para confirmar el cambio de contraseña: $verificationCode"

        // Enviar correo automáticamente utilizando Firebase o algún servicio SMTP
        try {
            // Aquí podrías usar un SDK de terceros o API propia para el envío de correo
            Log.d("Email", "Correo enviado a $userEmail con el código $verificationCode")
            Toast.makeText(context, "Correo de verificación enviado.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("EmailError", "No se pudo enviar el correo: ${e.localizedMessage}")
            Toast.makeText(context, "Error al enviar el correo.", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun promptUserForCode(context: Context): String? {
        // Aquí puedes implementar un diálogo para que el usuario ingrese el código recibido por correo
        // Esta es una simulación, en una app real usarías un método como "suspendCoroutine" o un listener
        return withContext(Dispatchers.Main) {
            var userInputCode: String? = null
            val dialog = AlertDialog.Builder(context).apply {
                setTitle("Verificación de Código")
                setMessage("Introduce el código que recibiste por correo electrónico:")
                val input = EditText(context)
                input.inputType = InputType.TYPE_CLASS_NUMBER
                setView(input)
                setPositiveButton("Confirmar") { _, _ ->
                    userInputCode = input.text.toString()
                }
                setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
            }.create()
            dialog.show()
            dialog.setOnDismissListener {
                dialog.dismiss()
            }
            userInputCode
        }
    }

    fun ChangePassword(
        changePassword: String,
        context: Context,
        currentPassword: String?,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (_loading.value == false) { // No se está procesando otro cambio
            if (changePassword.length < 6) {
                Toast.makeText(
                    context,
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                _loading.value = true
                currentUser?.let { user ->
                    // Enviar correo de verificación
                    sendVerificationEmail(user.email!!, context)

                    // Solicitar código al usuario
                    val inputCode = promptUserForCode(context)

                    if (inputCode != null && inputCode == verificationCode) {
                        // Reautenticación usando la contraseña actual
                        val credential =
                            EmailAuthProvider.getCredential(user.email!!, currentPassword!!)
                        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                // Cambiar contraseña en Firebase Authentication
                                user.updatePassword(changePassword)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            // Actualizar Firestore para mantener sincronización
                                            val db = FirebaseFirestore.getInstance()
                                            val userUpdates = mapOf(
                                                "password" to changePassword
                                            )
                                            db.collection("Usuarios").document(user.uid)
                                                .update(userUpdates)
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        "Firestore",
                                                        "Usuario actualizado exitosamente en Firestore"
                                                    )
                                                    Toast.makeText(
                                                        context,
                                                        "Contraseña cambiada exitosamente",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onSuccess()
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w(
                                                        "Firestore",
                                                        "Error al actualizar Firestore",
                                                        e
                                                    )
                                                    Toast.makeText(
                                                        context,
                                                        "Error al actualizar la base de datos",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        } else {
                                            Log.w(
                                                "FirebaseAuth",
                                                "Error al cambiar contraseña en Authentication",
                                                updateTask.exception
                                            )
                                            Toast.makeText(
                                                context,
                                                "Error al cambiar la contraseña",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        _loading.value = false
                                    }
                            } else {
                                Log.w("FirebaseAuth", "Reautenticación fallida", reauthTask.exception)
                                Toast.makeText(
                                    context,
                                    "Reautenticación fallida. Verifique la contraseña actual.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                _loading.value = false
                            }
                        }
                    } else {
                        Toast.makeText(context, "Código de verificación incorrecto.", Toast.LENGTH_SHORT).show()
                        _loading.value = false
                    }
                } ?: run {
                    Log.w("FirebaseAuth", "No hay usuario autenticado")
                    Toast.makeText(
                        context,
                        "No se encontró un usuario autenticado",
                        Toast.LENGTH_SHORT
                    ).show()
                    _loading.value = false
                }
            }
        }
    }*/
    private var verificationCode: String? = null

    fun sendVerificationEmail(
        userEmail: String,
        context: Context
    ) {
        verificationCode = (100000..999999).random().toString() // Genera un código aleatorio de 6 dígitos

        val subject = "Confirmación de cambio de contraseña"
        val message = "Hola, usa este código para confirmar el cambio de contraseña: $verificationCode"

        // Configura las propiedades para el servidor SMTP
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com") // Host de Gmail
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "587")
            put("mail.smtp.starttls.enable", "true")
        }

        // Crea una sesión de correo
        val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                return javax.mail.PasswordAuthentication("jjsmx24@gmail.com", "qple ydtz ozms chzf") // Usa tu correo y contraseña de aplicación
            }
        })

        // Crea el mensaje de correo
        try {
            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress("jjsmx24@gmail.com"))
                addRecipient(Message.RecipientType.TO, InternetAddress(userEmail))
                setSubject(subject)
                setText(message)
            }

            // Envía el correo en un hilo independiente
            Thread {
                try {
                    Transport.send(mimeMessage)
                    Log.d("Email", "Correo enviado a $userEmail con el código $verificationCode")
                    // Asegúrate de ejecutar el Toast en el hilo principal
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Correo de verificación enviado.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("EmailError", "No se pudo enviar el correo: ${e.localizedMessage}")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Error al enviar el correo.", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        } catch (e: Exception) {
            Log.e("EmailError", "Error al configurar el correo: ${e.localizedMessage}")
            Toast.makeText(context, "Error al configurar el correo.", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun promptUserForCode(context: Context): String? {
        val deferred = CompletableDeferred<String?>()

        val dialog = AlertDialog.Builder(context).apply {
            setTitle("Verificación de Código")
            setMessage("Introduce el código que recibiste por correo electrónico:")
            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            setView(input)
            setPositiveButton("Confirmar") { _, _ ->
                deferred.complete(input.text.toString())  // Completa el valor cuando el usuario confirma
            }
            setNegativeButton("Cancelar") { dialog, _ ->
                deferred.complete(null)  // Completa como null si el usuario cancela
                dialog.dismiss()
            }
        }.create()

        dialog.show()

        // Espera hasta que el valor esté disponible
        return deferred.await()
    }


    fun ChangePassword(
        changePassword: String,
        context: Context,
        currentPassword: String?,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (_loading.value == false) { // No se está procesando otro cambio
            if (changePassword.length < 6) {
                Toast.makeText(
                    context,
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                _loading.value = true
                currentUser?.let { user ->
                    // Enviar correo de verificación
                    sendVerificationEmail(user.email!!, context)

                    // Solicitar código al usuario
                    val inputCode = promptUserForCode(context)
                    Log.d("Correo", "verificado")
                    if (inputCode != null && inputCode == verificationCode) {
                        // Reautenticación usando la contraseña actual
                        Log.d("Correo", "verificado")
                        val credential =
                            EmailAuthProvider.getCredential(user.email!!, currentPassword!!)
                        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                // Cambiar contraseña en Firebase Authentication
                                user.updatePassword(changePassword)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            // Actualizar Firestore para mantener sincronización
                                            val db = FirebaseFirestore.getInstance()
                                            val userUpdates = mapOf(
                                                "password" to changePassword
                                            )
                                            db.collection("Usuarios").document(user.uid)
                                                .update(userUpdates)
                                                .addOnSuccessListener {
                                                    Log.d(
                                                        "Firestore",
                                                        "Usuario actualizado exitosamente en Firestore"
                                                    )
                                                    Toast.makeText(
                                                        context,
                                                        "Contraseña cambiada exitosamente",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onSuccess()
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w(
                                                        "Firestore",
                                                        "Error al actualizar Firestore",
                                                        e
                                                    )
                                                    Toast.makeText(
                                                        context,
                                                        "Error al actualizar la base de datos",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        } else {
                                            Log.w(
                                                "FirebaseAuth",
                                                "Error al cambiar contraseña en Authentication",
                                                updateTask.exception
                                            )
                                            Toast.makeText(
                                                context,
                                                "Error al cambiar la contraseña",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        _loading.value = false
                                    }
                            } else {
                                Log.w("FirebaseAuth", "Reautenticación fallida", reauthTask.exception)
                                Toast.makeText(
                                    context,
                                    "Reautenticación fallida. Verifique la contraseña actual.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                _loading.value = false
                            }
                        }
                    } else {
                        Toast.makeText(context, "Código de verificación incorrecto.", Toast.LENGTH_SHORT).show()
                        _loading.value = false
                    }
                } ?: run {
                    Log.w("FirebaseAuth", "No hay usuario autenticado")
                    Toast.makeText(
                        context,
                        "No se encontró un usuario autenticado",
                        Toast.LENGTH_SHORT
                    ).show()
                    _loading.value = false
                }
            }
        }
    }

}






