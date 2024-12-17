package SplashVideo

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1

@Composable
fun VideoSplashScreen(viewModel: SplashViewModel, onSplashFinished: () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val color = Color(0xFF000000) // Cambia por el color de tu aplicación

    LaunchedEffect1(Unit) {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = false // Cambia a true si usas íconos oscuros
        )
        systemUiController.setNavigationBarColor(
            color = color,
            darkIcons = false // Cambia a true si usas íconos oscuros
        )
    }
    val context = LocalContext.current

    val isLoadingComplete by viewModel.isLoadingComplete.collectAsState()
    val navController = rememberNavController()

    LaunchedEffect1(isLoadingComplete) {
        // Supongamos que el video dura 5 segundos
        delay(5000)
        if (isLoadingComplete) {
            onSplashFinished()
        }
    }

    // Caja para centrar el VideoView
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF000000))) {
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    val videoUri = Uri.parse("android.resource://${context.packageName}/raw/splash_video")
                    setVideoURI(videoUri)

                    setOnCompletionListener {
                        // Cuando el video termine, ejecuta la función de callback
                        onSplashFinished()
                    }

                    start() // Inicia la reproducción del video
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
    LaunchedEffect1(Unit) {
        delay(1000)
    }
}
