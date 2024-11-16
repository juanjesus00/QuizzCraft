package uiEditUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import quizcraft.FileUploader
import routes.NavigationActions
import uiLogin.loginbacked
import uiPrincipal.poppinsFamily

@Composable
fun EditUserScreen(navigationActions: NavigationActions, viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {
        EditUser(
            Modifier
                .align(Alignment.TopStart)
                .padding(vertical = 64.dp), navigationActions, viewModel
        )
    }
}

@Composable
fun EditUser(modifier: Modifier, navigationActions: NavigationActions, viewModel: loginbacked) {
    var userName by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var perfilImage by remember { mutableStateOf("")}

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
            FileUploader(image = R.drawable.camara, size = 128, typeFile = "image/*")
            Spacer(modifier = Modifier.padding(6.dp))
            CancelAndAcceptButtons(navigationActions, viewModel, userName, email, password)
        }
    }
}

@Composable
fun CancelAndAcceptButtons(
    navigationActions: NavigationActions,
    viewModel: loginbacked,
    userName: String,
    email: String,
    password: String
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
                    userName = userName,
                    email = email,
                    password = password,
                    onSuccess = {navigationActions.navigateToHome()},
                    context = context
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
fun UserField(name: String, function: (String) -> Unit) {
    TextField(
        value = name,
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
fun EmailField(username: String, function: (String) -> Unit) {
    TextField(
        value = username,
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
