package uiAbout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import languagesBack.getStringByName
import routes.NavigationActions
import uiPrincipal.poppinsFamily


@Composable
fun AboutScreen(
    navigationActions: NavigationActions
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {
        About()
    }
}

@Composable
fun About() {

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            initialAlpha = 0f, // Comienza completamente invisible
            animationSpec = tween(
                durationMillis = 1000, // Duración de 3 segundos
                easing = LinearOutSlowInEasing // Efecto de animación suave
            )
        ) + slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight / 2 },
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp),
            verticalArrangement = Arrangement.Center,

            ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF212325))
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    text = "QuizCraft",
                    color = Color(0xFFB18F4F),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 30.sp,
                )
            }
            Spacer(modifier = Modifier.padding(14.dp))
            getStringByName(LocalContext.current, "about_quizcraft")?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 13.sp,
                    lineHeight = 20.sp

                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            getStringByName(LocalContext.current, "developers")?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFB18F4F),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF212325))
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    "Antonio Sánchez Ramírez",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF212325))
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    "Juan Jesús Suárez Miranda",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }


            Spacer(modifier = Modifier.padding(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF212325))
                    .padding(vertical = 6.dp)
            ) {
                getStringByName(LocalContext.current, "about_version")?.let {
                    Text(
                        text = it + " Beta 1.0.0",
                        color = Color(0xFFB18F4F),
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily,
                        fontSize = 25.sp,
                    )
                }
            }
        }
    }
}