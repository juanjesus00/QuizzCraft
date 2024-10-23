package uiPrincipal



import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import header.getHeader
import kotlinx.coroutines.launch
import navigator.uiNavigator


val poppinsFamily = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.poppinsregular, FontWeight.Normal),
    androidx.compose.ui.text.font.Font(R.font.poppinsbold, FontWeight.Bold)
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyComposeApp() {
    val scrollState = rememberScrollState()
    var showBars by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(scrollState.value) {
        coroutineScope.launch {
            val currentOffset = scrollState.value

            // Mostrar/ocultar barras basado en la dirección del scroll
            if (currentOffset > previousScrollOffset) {
                showBars = false // Ocultar cuando el usuario scrollea hacia arriba
            } else {
                showBars = true // Mostrar cuando el usuario scrollea hacia abajo
            }
            previousScrollOffset = currentOffset
        }
    }
    Scaffold(
        topBar = {
            AnimatedVisibility(visible = showBars) {
                getHeader()
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = showBars) {
                Box (
                    modifier =  Modifier.offset(y = (-10).dp)
                ){
                    uiNavigator()
                }
            }
        }
    ) {
        getPrincipalMidSection(scrollState = scrollState)
    }

}



