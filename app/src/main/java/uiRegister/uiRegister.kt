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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import uiPrincipal.poppinsFamily

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {
        Register(Modifier.align(Alignment.Center))
    }
}

@Composable
fun Register(modifier: Modifier) {
    Column(modifier = modifier) {
        ImageLogo(Modifier.align(Alignment.CenterHorizontally))
        BoxField(Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun BoxField(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF212325))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        UserField()
        Spacer(modifier = Modifier.padding(8.dp))
        PasswordField()
        Spacer(modifier = Modifier.padding(8.dp))
        EmailField()
        Spacer(modifier = Modifier.padding(2.dp))
        LoginSection()
        Spacer(modifier = Modifier.padding(2.dp))
        RegisterButton()
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
fun UserField() {
    TextField(
        value = "",
        onValueChange = {},
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
fun PasswordField() {
    TextField(
        value = "",
        onValueChange = {},
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun EmailField() {
    TextField(
        value = "",
        onValueChange = {},
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
fun LoginSection() {
    Button(
        onClick = {},
        modifier = Modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)

    ) {
        Text(text = "Iniciar Sesion", fontWeight = FontWeight.Bold, fontFamily = poppinsFamily)
    }
}

@Composable
fun RegisterButton() {
    Button(
        onClick = {},
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB18F4F))

    ) {
        Text(
            text = "Registrarse",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            fontSize = 16.sp
        )
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

