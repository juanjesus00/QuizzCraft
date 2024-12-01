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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import com.example.myapplication.R
import model.Quiz
import quizcraft.FileUploader
import quizcraft.FileUploader3
import quizcraft.InsertTexField
import quizcraft.getQuizById
import quizcraft.parseTags
import quizcraft.updateQuizInFirestore
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun EditQuizScreen(
    navigationActions: NavigationActions,
    scrollState: ScrollState,
    navController: NavHostController
) {

    val navBackStackEntry = remember { navController.currentBackStackEntry }
    val quizId = navBackStackEntry?.arguments?.getString("quizId") ?: ""
    println(quizId)
    var quiz by remember { mutableStateOf<Quiz?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        getQuizById(
            quizId,
            onResult = { result ->
                println(result)
                quiz = result.copy()
                isLoading = false
            },
            onError = { exception ->
                // Manejo de errores
                println("Error al obtener quizzes: ${exception.message}")
                isLoading = false
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
        if (isLoading) {
            Text( // Mostrar un mensaje de carga mientras el quiz no está disponible
                text = "Cargando...",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                color = Color.Gray
            )
        } else {
            quiz?.let { loadedQuiz ->
                EditQuiz(
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(vertical = 64.dp),
                    navigationActions,
                    loadedQuiz
                )
            } ?: Text(
                text = "No se encontró el quiz.",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp,
                color = Color.Red
            )
        }
    }
}

@Composable
fun EditQuiz(modifier: Modifier, navigationActions: NavigationActions, quiz: Quiz) {
    var name by remember { mutableStateOf(quiz.name) }
    var tags by remember { mutableStateOf(quiz.tags.joinToString(separator = ", ")) }
    var description by remember { mutableStateOf(quiz.description) }
    var quizImageUrl by remember { mutableStateOf(quiz.description) }

    Box(
        modifier = modifier
    ) {
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            InsertTexField(text = name, inputLabel = "Nombre del cuestionario", size = 56) {
                name = it
            }
            FileUploader3(
                image = R.drawable.camara,
                size = 128,
                typeFile = "image/*",
                onImageUrlReady = { url -> quizImageUrl = url })
            InsertTexField(
                text = tags,
                inputLabel = "Etiquetas",
                size = 56
            ) { tags = it }
            InsertTexField(
                text = description,
                inputLabel = "Descripcion",
                size = 150
            ) { description = it }
            CancelAndAcceptButtons(
                navigationActions,
                quiz.quizId,
                name,
                tags,
                description,
                quizImageUrl
            )
        }
    }
}

@Composable
fun CancelAndAcceptButtons(
    navigationActions: NavigationActions,
    quizId: String,
    name: String,
    tags: String,
    description: String,
    imageUrl: String
) {
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
            onClick = {
                updateQuizInFirestore(
                    quizId, mapOf(
                        "quizName" to name,
                        "quizDescription" to description,
                        "quizImage" to imageUrl,
                        "quizTags" to parseTags(tags)
                    )
                )
                navigationActions.navigateToHome()

            },
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
