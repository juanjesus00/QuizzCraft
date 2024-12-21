package uiPrincipal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import languagesBack.getStringByName
import model.Quiz
import quizcraft.getQuizzesByLastQuizzesUser
import quizcraft.getQuizzesByUserId
import routes.NavigationActions
import uiInfoQuiz.getLastQuizByUserId
import uiLogin.loginbacked

private var auth: FirebaseAuth = Firebase.auth

@Composable
fun insertSectionQuiz(titleSection: String, titleQuiz: String, navigationActions: NavigationActions, viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel()){
    var isVisible by remember { mutableStateOf(false) }
    val isVerified by viewModel.isEmailVerified.observeAsState(false)
    LaunchedEffect(Unit) {
        delay(250)
        isVisible = true
    }

    Spacer(modifier = Modifier.height(50.dp))

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(initialOffsetY = { -it })
    ) {
        Text(
            text = titleSection,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 15.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
        )
    }


    Row (
        modifier = Modifier
            .fillMaxWidth()
            .then(if(!(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())) Modifier.horizontalScroll(rememberScrollState()) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,

        ) {

        if(!(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())) {
            val quizzes = remember { mutableStateOf<List<Quiz>>(emptyList()) }
            LaunchedEffect(Unit) {
                getQuizzesByUserId(
                    userId = auth.currentUser?.uid ?: "",
                    onResult = { result ->
                        quizzes.value = result // Se actualiza el estado después de recibir los datos
                    },
                    onError = { exception ->
                        // Manejo de errores
                        println("Error al obtener quizzes: ${exception.message}")
                    }
                )
            }

            quizzes.value.forEachIndexed { index, quiz ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = scaleIn(
                        initialScale = 0.3f,
                        animationSpec = tween(durationMillis = 600, delayMillis = 200 * index) // Duración de la animación
                    ) + fadeIn(animationSpec = tween(durationMillis = 600))
                )  {
                    favQuiz(
                        imageResource = quiz.quizImageUrl,
                        title = quiz.name,
                        titleSection = titleSection,
                        navigationActions = navigationActions,
                        quizId = quiz.quizId
                    )
                }
            }
        } else if(FirebaseAuth.getInstance().currentUser == null){

            getStringByName(LocalContext.current, "my_quizzes_warning")?.let {
                Text(text = it,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 25.dp),
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily
                )
            }
        }else if(!isVerified) {
            getStringByName(LocalContext.current, "my_quizzes_warning_unverified")?.let {
                Text(text = it,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 25.dp),
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily
                )
            }
        }
    }
}

@Composable
fun insertSectionLastQuizzies(navigationActions: NavigationActions,viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel()){
    val isVerified by viewModel.isEmailVerified.observeAsState(false)
    LaunchedEffect(Unit) {
        viewModel.checkIfEmailVerified()
    }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Spacer(modifier = Modifier.height(50.dp))

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(initialOffsetY = { -it })
    ) {
        getStringByName(LocalContext.current, "recent_quizzes")?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = 15.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
            )
    }

    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .then(if(!(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())) Modifier.horizontalScroll(rememberScrollState()) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,

        ) {

        if(!(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())) {
            val quizzes = remember { mutableStateOf<List<Quiz>>(emptyList()) }
            LaunchedEffect(Unit) {
                getLastQuizByUserId { result ->
                    getQuizzesByLastQuizzesUser(
                        result,
                        onResult = { result_quizzes ->
                            quizzes.value = result_quizzes
                        },
                        onError = { exception ->
                            println("Error al obtener quizzes: ${exception.message}")
                        })
                }
            }

            quizzes.value.forEachIndexed { index, quiz ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = scaleIn(
                        initialScale = 0.3f,
                        animationSpec = tween(durationMillis = 600, delayMillis = 200 * index) // Duración de la animación
                    ) + fadeIn(animationSpec = tween(durationMillis = 600))
                )  {
                    favQuiz(
                        imageResource = quiz.quizImageUrl,
                        title = quiz.name,
                        titleSection = "Cuestionarios Recientes",
                        navigationActions = navigationActions,
                        quizId = quiz.quizId
                    )
                }
            }

        } else if(FirebaseAuth.getInstance().currentUser == null){
            getStringByName(LocalContext.current, "recent_quizzes_warning")?.let {
                Text(text = it,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 25.dp),
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily
                )
            }
        }else if(!isVerified){
            getStringByName(LocalContext.current, "recent_quizzes_warning_unverified")?.let {
                Text(text = it,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 25.dp),
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily
                )
            }
        }
    }
}