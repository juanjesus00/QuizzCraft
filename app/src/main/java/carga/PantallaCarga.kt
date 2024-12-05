package carga

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.myapplication.R
import routes.NavigationActions


@Composable
fun LoadingScreen(navigationActions: NavigationActions) {
    LoadingScreen2(gifUrl = "https://firebasestorage.googleapis.com/v0/b/quizcraft-342d2.firebasestorage.app/o/LoadingGifs%2Ficons8-mapa-mental.gif?alt=media&token=5fe5d17d-9e5b-45c2-a8a4-7677b8211c77")
}

@Composable
fun LoadingScreen2(gifUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.carga) // Nombre del archivo sin extensión
                .crossfade(true) // Añade un efecto de transición
                .build(),
            contentDescription = "Loading...",
            modifier = Modifier.size(150.dp)
        )
    }
}

