package uiLogin

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import carga.LoadingScreen
import com.example.myapplication.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import languagesBack.getStringByName
import routes.NavigationActions
import uiPrincipal.poppinsFamily



@Composable
fun LoginScreen(navigationActions: NavigationActions, viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {
        Login(Modifier.align(Alignment.Center), navigationActions, viewModel)
    }
}

@Composable
fun Login(modifier: Modifier, navigationActions: NavigationActions, viewModel: loginbacked) {
    Column(modifier = modifier) {
        ImageLogo(Modifier.align(Alignment.CenterHorizontally))
        BoxField(Modifier.align(Alignment.CenterHorizontally), navigationActions, viewModel)
    }
}

@Composable
fun BoxField(modifier: Modifier, navigationActions: NavigationActions, viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF212325))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        UserField(email) {email = it}
        Spacer(modifier = Modifier.padding(8.dp))
        PasswordField(password) {password = it}
        Spacer(modifier = Modifier.padding(2.dp))
        RegisterSection(navigationActions)
        Spacer(modifier = Modifier.padding(2.dp))
        LoginButton(email, password, navigationActions, viewModel)
        Spacer(modifier = Modifier.padding(8.dp))
        GoogleIcon(viewModel, navigationActions)

    }
}

@Composable
fun GoogleIcon(viewModel: loginbacked, navigationActions: NavigationActions) {
    val tokenGoogle = "1074117218604-it9mlufe66sldtihomg5s4vae1jum1o5.apps.googleusercontent.com"
    val context = LocalContext.current

    val oneTapClient = remember { Identity.getSignInClient(context) }
    val signInRequest = remember {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(tokenGoogle)
                    .setFilterByAuthorizedAccounts(false) // Permite elegir una cuenta nueva
                    .build()
            )
            .setAutoSelectEnabled(false) // Muestra el selector de cuentas
            .build()
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .StartIntentSenderForResult()
    ) {result ->
        try{
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            val email = credential.id
            val password = (100000..999999).random().toString()//credential.password.toString()
            if (idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                viewModel.SingInWithGoogleCredential(firebaseCredential, credential, password) {

                    viewModel.linkEmailAndPassword(email, password,
                        onSuccess = {
                            Log.d("FirebaseAuth", "Usuario vinculado correctamente")
                            navigationActions.navigateToHome()
                        },
                        onFailure = { error ->
                            Log.e("FirebaseAuth", "Error al vincular usuario: $error")
                        }
                    )
                    navigationActions.navigateToHome()
                }

            } else {
                Log.e("GoogleSignIn", "No se obtuvo un token válido")
            }
        }catch (ex:ApiException){
            Log.d("loginGoogle", "El login de google falló" + "${ex.localizedMessage}")
        }
    }
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_googleicon),
        contentDescription = "Google Icon",
        modifier = Modifier
            .height(32.dp)
            .clickable {
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener { result ->
                        try{
                            //val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                            launcher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                        }catch (e: Exception){
                            Log.d("GoogleSignIn", "Error al construir IntentSender: ${e.localizedMessage}")
                        }
                    }
                    .addOnFailureListener{ e ->
                        Log.d("login google", "Error al iniciar el flujo de inicio de sesión: ${e.localizedMessage}\"")
                    }
            },
        tint = Color.Unspecified
    )
}

@Composable
fun UserField(email: String, function: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = function,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFFFF)),
        placeholder = {
            getStringByName(LocalContext.current, "email")?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    color = Color(0xFFC49450)
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun PasswordField(password: String, function: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = function,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFFFF)),
        placeholder = {
            getStringByName(LocalContext.current, "password")?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    color = Color(0xFFC49450)
                )
            }
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun RegisterSection(navigationActions: NavigationActions) {
    Button(
        onClick = {navigationActions.navigateToRegister()},
        modifier = Modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)

    ) {
        getStringByName(LocalContext.current, "register")?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily
            )
        }
    }
}

@Composable
fun LoginButton(
    username: String,
    password: String,
    navigationActions: NavigationActions,
    viewModel: loginbacked
) {
    var isLoading by remember { mutableStateOf(false) }
    var context = LocalContext.current
    Button(
        onClick = {
            isLoading = !isLoading
            viewModel.signIn(
                email = username,
                password = password,
                context,
                onSuccess = {
                    navigationActions.navigateToHome()
                }
            ) {
                navigationActions.navigateToLogin()
            }

        },
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB18F4F))

    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            getStringByName(LocalContext.current, "login")?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Composable
fun ImageLogo(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(256.dp)
    )
}
