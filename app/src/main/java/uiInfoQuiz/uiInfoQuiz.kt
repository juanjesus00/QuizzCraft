package uiInfoQuiz

import android.icu.text.IDNA.Info
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.R
import model.Quiz
import quizcraft.getQuizById
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun InfoQuizScreen(
    navigationActions: NavigationActions,
    scrollState: ScrollState,
    navController: NavHostController
) {

    val navBackStackEntry = remember { navController.currentBackStackEntry }
    val quizId = navBackStackEntry?.arguments?.getString("quizId") ?: ""
    var quiz by remember { mutableStateOf<Quiz?>(null) }

    LaunchedEffect(Unit) {
        getQuizById(
            quizId,
            onResult = { result ->
                println(result)
                quiz = result.copy()
            },
            onError = { exception ->
                // Manejo de errores
                println("Error al obtener quizzes: ${exception.message}")
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    ) {
        quiz?.let { loadedQuiz ->
            InfoQuiz(navigationActions, quiz!!)
        } ?: Text(
            text = "No se encontró el quiz.",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 18.sp,
            color = Color.Red
        )
    }
}

@Composable
fun InfoQuiz(navigationActions: NavigationActions, quiz: Quiz) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 128.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            ImageQuiz(quiz.quizImageUrl)
            NameAndTag(quiz.name, quiz.tags)
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Descripción:",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            color = Color.Black,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = quiz.description,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.padding(24.dp))
        ButtonPlay(navigationActions, Modifier.align(Alignment.CenterHorizontally), quiz.quizId)
    }
}

@Composable
fun NameAndTag(title: String, tags: List<String>) {
    Column() {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            color = Color.Black,
            fontSize = 32.sp
        )
        Text(
            text = tags.joinToString(", "),
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Bold,
            color = Color.Gray

        )
    }
}

@Composable
fun ImageQuiz(imageurl: String) {
    AsyncImage(
        model = imageurl,
        contentDescription = "Foto cuestionario",
        modifier = Modifier
            .size(width = 150.dp, height = 150.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 100.dp,
                    topEnd = 100.dp,
                    bottomStart = 100.dp,
                    bottomEnd = 100.dp
                )
            ),
        contentScale = Crop
    )
}

@Composable
fun ButtonPlay(navigationActions: NavigationActions, modifier: Modifier, quizId: String) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(20),
        onClick = { navigationActions.navigateToGame()
                  userAddLastQuiz(quizId)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
    ) {
        Text(
            "JUGAR",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            fontSize = 24.sp,
            color = Color(0xFFB18F4F)
        )
    }
}

