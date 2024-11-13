package uiEditQuiz

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import quizcraft.FileUploader
import quizcraft.InsertTexField
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun EditQuizScreen(navigationActions: NavigationActions, scrollState: ScrollState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    ) {
        EditQuiz(
            Modifier
                .align(Alignment.TopCenter)
                .padding(vertical = 64.dp), navigationActions
        )
    }
}

@Composable
fun EditQuiz(modifier: Modifier, navigationActions: NavigationActions) {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = modifier
    ) {
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            InsertTexField(text = text, inputLabel = "Título del cuestionario", size = 56)
            FileUploader(image = R.drawable.camara, size = 128, typeFile = "image/*")
            InsertTexField(text = text, inputLabel = "#Tags", size = 56)
            InsertTexField(text = text, inputLabel = "Descripción", size = 150)
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
            onClick = { navigationActions.navigateToHome() },
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
