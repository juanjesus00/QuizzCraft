package uiEditUser

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import routes.NavigationActions
import uiLogin.loginbacked
import uiPrincipal.poppinsFamily
import uiUserInfo.userInfoBack
import java.io.File

@Composable
fun EditUserScreen(navigationActions: NavigationActions, viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel(), viewModelUser: userInfoBack = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

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
    var perfilImage by remember { mutableStateOf(mutableStateOf<Uri?>(null))}

    LaunchedEffect(Unit) {
        viewModelUser.getInfoUser { user ->
            userName = user?.get("userName") as? String
            email = user?.get("email") as? String
        }
    }

    Box(
        modifier = modifier
    ) {
        Column(modifier = modifier) {
            Spacer(modifier = Modifier.padding(12.dp))
            UserField(userName) {userName = it}
            Spacer(modifier = Modifier.padding(12.dp))
            EmailField(email) {email = it}
            Spacer(modifier = Modifier.padding(12.dp))
            PasswordField(password) {password = it}
            Spacer(modifier = Modifier.padding(12.dp))
            photoUploader(image = R.drawable.camara, image2 = R.drawable.galery_icon, size = 128, typeFile = "image/*", perfilImage)
            Spacer(modifier = Modifier.padding(6.dp))
            CancelAndAcceptButtons(navigationActions, viewModel, userName, email, password, perfilImage)
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
    perfilImage: MutableState<Uri?>
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
            Text(
                "Cancelar",
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 20.sp,
                color = Color(0xFFB18F4F)
            )
        }
        Button(
            shape = RoundedCornerShape(20),
            onClick = {
                viewModel.editUser(
                    userName = userName.toString(),
                    email = email.toString(),
                    password = password,
                    onSuccess = {navigationActions.navigateToHome()},
                    context = context,
                    selectImageUri = perfilImage.value
                ) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
        ) {
            Text(
                "Aceptar",
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 20.sp,
                color = Color(0xFFB18F4F)
            )
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
            Text(
                text = "Nombre de usuario",
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                color = Color(0xFFC49450)
            )
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
            Text(
                text = "Contraseña",
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                color = Color(0xFFC49450)
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
    )
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
            Text(
                text = "Email",
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                color = Color(0xFFC49450)
            )
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