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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import quizcraft.FileUploader
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun EditUserScreen(navigationActions: NavigationActions) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {
        EditUser(
            Modifier
                .align(Alignment.TopStart)
                .padding(vertical = 64.dp), navigationActions
        )
    }
}

@Composable
fun EditUser(modifier: Modifier, navigationActions: NavigationActions) {
    Box(
        modifier = modifier
    ) {
        Column(modifier = modifier) {
            Spacer(modifier = Modifier.padding(12.dp))
            Field('u')
            Spacer(modifier = Modifier.padding(12.dp))
            Field('e')
            Spacer(modifier = Modifier.padding(12.dp))
            Field('p')
            Spacer(modifier = Modifier.padding(12.dp))
            FileUploader(image = R.drawable.camara, size = 128, typeFile = "image/*")
            Spacer(modifier = Modifier.padding(6.dp))
            CancelAndAcceptButtons(navigationActions)
        }
    }
}

@Composable
fun CancelAndAcceptButtons(navigationActions: NavigationActions) {
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
            onClick = { navigationActions.navigateToHome() },
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
fun Field(fieldtype: Char) {
    val placeholdertext: String
    val keyboardtype: KeyboardType

    when (fieldtype) {
        'u' -> {
            placeholdertext = "Nombre de Usuario"
            keyboardtype = KeyboardType.Text
        }

        'p' -> {
            placeholdertext = "Contraseña"
            keyboardtype = KeyboardType.Password
        }

        else -> {
            placeholdertext = "Email"
            keyboardtype = KeyboardType.Email
        }
    }

    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFFFF)),
        placeholder = {
            Text(
                text = placeholdertext,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                color = Color(0xFFC49450)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardtype),
        singleLine = true,
        maxLines = 1,
    )
}
