package uiGame

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.myapplication.R
import routes.NavigationActions
import uiPrincipal.poppinsFamily
import java.util.Timer
import kotlin.concurrent.schedule

var correctQuestion: Int = 0
var wrongQuestion: Int = 0




@Composable
fun GameScreen(
    navigationActions: NavigationActions,
    scrollState: ScrollState,
    option: Char,
    navController: NavHostController
) {

    val navBackStackEntry = remember { navController.currentBackStackEntry }
    val content = navBackStackEntry?.arguments?.getString("content") ?: ""
    val questions = ParseAndGetJson(content).questions

    val context = LocalContext.current

    val soundManager = remember { SoundManager(context) }
    val musicManager = remember { MusicManager(context) }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    musicManager.stopMusic()
                }
                Lifecycle.Event.ON_RESUME -> {
                    musicManager.playMusic()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    musicManager.stopMusic()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            musicManager.stopMusic()
        }
    }

    LaunchedEffect(Unit) {
        soundManager.loadSound(R.raw.correct_answer, context)
        soundManager.loadSound(R.raw.wrong_answer, context)
    }

    musicManager.playMusic()

    var currentQuestionIndex by rememberSaveable { mutableStateOf(0) }

    println(questions)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    ) {
        Game(
            option,
            question = questions[currentQuestionIndex],
            onNext = {
                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                } else {
                    navigationActions.navigateToResult(correctQuestion, wrongQuestion)
                    correctQuestion = 0
                    wrongQuestion = 0
                }
            },
            navigationActions = navigationActions,
            soundManager
        )
    }

}

@Composable
fun Game(
    option: Char,
    question: Question,
    onNext: () -> Unit,
    navigationActions: NavigationActions,
    soundManager: SoundManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 128.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black, shape = RoundedCornerShape(20))
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            if (option == 't') {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    text = question.question,
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

            } else {
                Image(
                    painter = painterResource(id = R.drawable.huppty),
                    contentDescription = "Foto pregunta",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.padding(24.dp))
        Answers(question.answer, onNext, soundManager)
    }
}

@Composable
fun Answers(answer: Answers, onNext: () -> Unit, soundManager: SoundManager) {
    val options = answer.answers
    val selectedOptionIndex = remember { mutableStateOf(-1) }

    options.forEachIndexed { index, option ->
        val color = when (index) {
            0 -> 0xFF0303FF
            1 -> 0xFFFF0004
            2 -> 0xFF36D22E
            else -> 0xFFEB9D0D
        }

        val buttonSize by animateDpAsState(
            targetValue = if (selectedOptionIndex.value == index && option.correct) 350.dp else 300.dp,
            animationSpec = tween(durationMillis = 500)
        )

        val borderColor by animateColorAsState(
            targetValue = when {
                selectedOptionIndex.value == index && option.correct -> Color.Green
                selectedOptionIndex.value == index && !option.correct -> Color.Red
                else -> Color.Transparent
            },
            animationSpec = tween(durationMillis = 500)
        )



        Button(
            shape = RoundedCornerShape(20),
            onClick = { selectedOptionIndex.value = index
                        if (options[index].correct) {
                            soundManager.playSound(R.raw.correct_answer)
                            correctQuestion++
                        } else {
                            soundManager.playSound(R.raw.wrong_answer)
                            wrongQuestion++
                        }
                        Timer().schedule(2000) {
                            Handler(Looper.getMainLooper()).post {
                                selectedOptionIndex.value = -1
                                onNext()
                            }
                        }
                      },
            modifier = Modifier
                .size(width = buttonSize, height = buttonSize / 4)
                .border(4.dp, borderColor, RoundedCornerShape(20))
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color))
        ) {
            Text(
                option.respuesta,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 24.sp,
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}


