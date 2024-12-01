package uiPrincipal

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import model.Quiz
import quizcraft.getQuizzesByUserId
import routes.NavigationActions

private var auth: FirebaseAuth = Firebase.auth

@Composable
fun insertSectionQuiz(titleSection: String, titleQuiz: String, navigationActions: NavigationActions){
    Spacer(modifier = Modifier.height(50.dp))
    Text(
        text = titleSection,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = 15.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = poppinsFamily,
    )
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,

        ) {

        val quizzes = remember { mutableStateOf<List<Quiz>>(emptyList()) }

        LaunchedEffect(Unit) {
            getQuizzesByUserId(
                userId = auth.currentUser?.uid ?: "",
                onResult = { result ->
                    println(result)
                    quizzes.value = result // Se actualiza el estado después de recibir los datos
                },
                onError = { exception ->
                    // Manejo de errores
                    println("Error al obtener quizzes: ${exception.message}")
                }
            )
        }

        for(quiz in quizzes.value) {
            println(quiz)
            favQuiz(imageResource = quiz.quizImageUrl, title = quiz.name, titleSection = titleSection, navigationActions, quizId = quiz.quizId)
        }
    }
}