package uiRegister

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import languagesBack.getStringByName
import routes.NavigationActions
import uiLogin.loginbacked
import uiPrincipal.poppinsFamily

@Composable
fun RegisterScreen(
    navigationActions: NavigationActions,
    viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {
        Register(Modifier.align(Alignment.Center), viewModel, navigationActions)
    }
}

@Composable
fun Register(modifier: Modifier, viewModel: loginbacked, navigationActions: NavigationActions) {
    Column(modifier = modifier) {
        ImageLogo(Modifier.align(Alignment.CenterHorizontally))
        BoxField(Modifier.align(Alignment.CenterHorizontally), viewModel, navigationActions)
    }
}

@Composable
fun BoxField(modifier: Modifier, viewModel: loginbacked, navigationActions: NavigationActions) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF212325))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        UserField(name) { name = it }
        Spacer(modifier = Modifier.padding(8.dp))
        PasswordField(password) { password = it }
        Spacer(modifier = Modifier.padding(8.dp))
        EmailField(username) { username = it }
        Spacer(modifier = Modifier.padding(2.dp))
        LoginSection(navigationActions)
        Spacer(modifier = Modifier.padding(2.dp))
        RegisterButton(viewModel, username, password, name, navigationActions)
        Spacer(modifier = Modifier.padding(8.dp))
        GoogleIcon()

    }
}

@Composable
fun GoogleIcon() {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_googleicon),
        contentDescription = "Google Icon",
        modifier = Modifier.height(32.dp),
        tint = Color.Unspecified
    )
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
fun EmailField(username: String, function: (String) -> Unit) {
    TextField(
        value = username,
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
fun LoginSection(navigationActions: NavigationActions) {
    Button(
        onClick = { navigationActions.navigateToLogin() },
        modifier = Modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)

    ) {
        getStringByName(LocalContext.current, "login")?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily
            )
        }
    }
}

@Composable
fun RegisterButton(
    viewModel: loginbacked,
    username: String,
    password: String,
    name: String,
    navigationActions: NavigationActions
) {
    var isLoading by remember { mutableStateOf(false) }
    var context = LocalContext.current
    Button(
        onClick = {
            isLoading = !isLoading
            viewModel.register(email = username, password = password, context = context, name) {
                navigationActions.navigateToHome()
            }
        },
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB18F4F))

    ) {
        (if (isLoading) getStringByName(LocalContext.current, "loading") else getStringByName(
            LocalContext.current, "register"))?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 16.sp
            )
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