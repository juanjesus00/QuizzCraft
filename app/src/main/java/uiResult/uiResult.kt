package uiResult

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun ResultScreen(navigationActions: NavigationActions, scrollState: ScrollState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    ) {
        Spacer(modifier = Modifier.padding(top = 128.dp))
        Result(navigationActions)
    }
}

@Composable
fun Result(navigationActions: NavigationActions) {
    Box(
        modifier = Modifier
            .background(Color.Black, shape = RoundedCornerShape(20))
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Text(text = "Calificación")
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "7.5")
        Spacer(modifier = Modifier.padding(16.dp))
        Column() {
            Text("Correctas: 6")
            Text("Incorrectas: 8")
        }

    }
    Spacer(modifier = Modifier.padding(8.dp))
    HomeButton(navigationActions)
}

@Composable
fun HomeButton(navigationActions: NavigationActions) {
    Button(
        shape = RoundedCornerShape(20),
        onClick = { navigationActions.navigateToHome() },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
    ) {
        Text(
            "Inicio",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            fontSize = 24.sp,
            color = Color(0xFFB18F4F)
        )
    }
}
