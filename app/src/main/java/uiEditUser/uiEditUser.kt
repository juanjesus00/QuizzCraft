package uiEditUser

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import languagesBack.getStringByName
import routes.NavigationActions
import uiLogin.loginbacked
import uiPrincipal.poppinsFamily
import uiUserInfo.userInfoBack
import java.io.File


@Composable
fun EditUserScreen(navigationActions: NavigationActions, scrollState: ScrollState, viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel(), viewModelUser: userInfoBack = viewModel()) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    ) {
        EditUser(
            Modifier
                .align(Alignment.TopStart)
                .padding(vertical = 64.dp), navigationActions, viewModel, viewModelUser
        )
    }
}

@Composable
fun EditUser(
    modifier: Modifier,
    navigationActions: NavigationActions,
    viewModel: loginbacked,
    viewModelUser: userInfoBack
) {
    var userName by remember { mutableStateOf<String?>("")}
    var email by remember { mutableStateOf<String?>("")}
    var password by remember { mutableStateOf("")}
    var passwordChange by remember { mutableStateOf("")}
    var perfilImage by remember { mutableStateOf(mutableStateOf<Uri?>(null))}
    var showpassword by remember { mutableStateOf<String?>("") }
    var isopen by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
        viewModelUser.getInfoUser { user ->
            userName = user?.get("userName") as? String
            email = user?.get("email") as? String
            showpassword = user?.get("password") as? String
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            initialAlpha = 0f, // Comienza completamente invisible
            animationSpec = tween(
                durationMillis = 1000, // Duración de 3 segundos
                easing = LinearOutSlowInEasing // Efecto de animación suave
            )
        ) + slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight / 2 },
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        Box(
            modifier = modifier
        ) {
            Column(modifier = modifier) {
                Spacer(modifier = Modifier.padding(12.dp))
                UserField(userName) {userName = it}
                Spacer(modifier = Modifier.padding(12.dp))
                EmailField(email) {email = it}
                Spacer(modifier = Modifier.padding(12.dp))
                ShowPasswordField(showpassword)
                Spacer(modifier = Modifier.padding(12.dp))
                ChangePasswordField(password, {password = it}, passwordChange, {passwordChange = it})
                Spacer(modifier = Modifier.padding(12.dp))
                photoUploader(image = R.drawable.camara, image2 = R.drawable.galery_icon, size = 128, typeFile = "image/*", perfilImage)
                Spacer(modifier = Modifier.padding(6.dp))
                CancelAndAcceptButtons(navigationActions, viewModel, userName, email, password, perfilImage, showpassword, passwordChange)
            }
        }
    }
}


@Composable
fun CancelAndAcceptButtons(
    navigationActions: NavigationActions,
    viewModel: loginbacked,
    userName: String?,
    email: String?,
    password: String,
    perfilImage: MutableState<Uri?>,
    showpassword: String?,
    passwordChange: String
) {
    var context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            shape = RoundedCornerShape(20),
            onClick = { navigationActions.navigateToUserInfo() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
        ) {
            getStringByName(LocalContext.current, "cancel_button_text")?.let {
                Text(
                    it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 20.sp,
                    color = Color(0xFFB18F4F)
                )
            }
        }
        Button(
            shape = RoundedCornerShape(20),
            onClick = {
                if (passwordChange == ""){

                    viewModel.editUser(
                        userName = userName.toString(),
                        email = email.toString(),
                        password = password,
                        onSuccess = {navigationActions.navigateToHome()},
                        context = context,
                        selectImageUri = perfilImage.value,
                        showpassword = showpassword,
                        passwordchange = passwordChange,
                        navigationActions = navigationActions
                    )
                }else if (passwordChange != ""){
                    viewModel.ChangePassword(
                        changePassword = passwordChange,
                        context,
                        currentPassword = showpassword,
                        onSuccess = {navigationActions.navigateToHome()},
                        navigationActions = navigationActions
                    )
                }
                 },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
        ) {
            getStringByName(LocalContext.current, "accept_button_text")?.let {
                Text(
                    it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 20.sp,
                    color = Color(0xFFB18F4F)
                )
            }
        }
    }
}
@Composable
fun UserField(name: String?, function: (String) -> Unit) {
    TextField(
        value = name.toString(),
        onValueChange = function,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFFFF)),
        placeholder = {
            getStringByName(LocalContext.current, "username")?.let {
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
fun ChangePasswordField(password: String, function: (String) -> Unit, passwordchange: String, function2: (String) -> Unit) {
    var changePass by remember { mutableStateOf(false) }
    Button(
        modifier = Modifier,
        shape = RoundedCornerShape(20),
        onClick = {changePass = !changePass

                  },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325)),
        enabled = if(changePass) false else true
    ) {
        getStringByName(LocalContext.current, "change_pass")?.let {
            Text(
                it,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 20.sp,
                color = Color(0xFFB18F4F)
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    Box (modifier = Modifier){
        TextField(
            modifier = Modifier
                .alpha(if (changePass) 1f else 0f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth(),
            value = passwordchange,
            onValueChange = function2,
            placeholder = {
                getStringByName(LocalContext.current, "new_pass")?.let {
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
    Spacer(modifier = Modifier.height(10.dp))

    Box (modifier = Modifier.alpha(if(changePass) 0f else 1f)){
        TextField(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth(),
            enabled = if(changePass) false else true,
            value = password,
            onValueChange = function,
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
            maxLines = 1
        )
    }


}
@Composable
fun ShowPasswordField(password: String?) {
    var eyeState by remember { mutableStateOf(false) }
    Button(
        onClick = {
                eyeState = !eyeState
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEADEE6),
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            (if (eyeState)  password?:"no tienes contraseña" else getStringByName(
                LocalContext.current, "current_pass"))?.let { Text(text = it, fontSize = 10.sp,fontFamily = poppinsFamily, color = Color(0xFFC49450)) }
            Box {
                Image(

                    painter = painterResource(id = if (eyeState) R.drawable.eyeopen else R.drawable.eyeclose),
                    contentDescription = "Imagen de estado"
                )
            }
        }
    }
}

@Composable
fun EmailField(username: String?, function: (String) -> Unit) {
    TextField(
        value = username.toString(),
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
    )
}



@Composable
fun photoUploader(
    image: Int,
    image2: Int,
    size: Int,
    typeFile: String,
    perfilImage: MutableState<Uri?>
) {
    //var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    // Crear un archivo temporal para la foto de la cámara
    fun createImageFile(): Uri? {
        val tmpFile = File.createTempFile("camera_image", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", tmpFile)
    }

    // Lanzador para la cámara
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            perfilImage.value = imageUri.value
        }
    }

    // Lanzador para la galería
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        perfilImage.value = uri
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = {
                // Mostrar un diálogo o menú para elegir entre galería o cámara
                imageUri.value = createImageFile()
                imageUri.value?.let {
                    cameraLauncher.launch(it)
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(size.dp)
                .border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEADEE6),
            )
        ) {
            Box {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "imagen de agregación",
                    modifier = Modifier.alpha(if (perfilImage.value == null) 1f else 0f)
                )
                perfilImage.value?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Button(
            onClick = { galleryLauncher.launch(typeFile) }, // Seleccionar de la galería
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(size.dp)
                .border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEADEE6),
            )
        ) {
            Box {
                Image(
                    painter = painterResource(id = image2),
                    contentDescription = "imagen de agregación",
                    modifier = Modifier
                        .size(50.dp)
                        .alpha(if (perfilImage.value == null) 1f else 0f)
                )
                perfilImage.value?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(50.dp))
}