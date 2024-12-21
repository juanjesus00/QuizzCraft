package uiGame

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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

    val isMuted = remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    val correctQuestion = remember { mutableIntStateOf(0) }
    val wrongQuestion = remember { mutableIntStateOf(0) }

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
        isVisible = true
    }

    musicManager.playMusic()

    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    ) {

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                initialAlpha = 0f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = LinearOutSlowInEasing
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 90.dp)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,


                ) {


                Box(
                    modifier = Modifier
                        .background(Color.Black, shape = RoundedCornerShape(8.dp))
                        .padding(5.dp)
                ) {
                    Text(
                        text = "${currentQuestionIndex + 1} / ${questions.size}",
                        style = TextStyle(color = Color(0xFFB18F4F), fontSize = 17.sp),
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                Image(
                    painter = if (isMuted.value) {
                        painterResource(id = R.drawable.muted_volume)
                    } else {
                        painterResource(id = R.drawable.activated_volume)
                    },
                    contentDescription = "Volumen",
                    modifier = Modifier.clickable {
                        if (isMuted.value) {
                            musicManager.setVolume(1.0F)
                        } else {
                            musicManager.setVolume(0.0F)
                        }
                        isMuted.value = !isMuted.value
                    }
                )
            }
        }

        Game(
            option,
            question = questions[currentQuestionIndex],
            onNext = {
                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++

                } else {
                    navigationActions.navigateToResult(correctQuestion.intValue, wrongQuestion.intValue)
                }
            },
            soundManager,
            isVisible,
            correctQuestion,
            wrongQuestion
        )
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Game(
    option: Char,
    question: Question,
    onNext: () -> Unit,
    soundManager: SoundManager,
    isVisible: Boolean,
    correctQuestions: MutableState<Int>,
    wrongQuestions: MutableState<Int>
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            initialAlpha = 0f,
            animationSpec = tween(
                durationMillis = 600,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp),
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
            Answers(question.answer, onNext, soundManager, correctQuestions, wrongQuestions)
        }
    }
}


@Composable
fun Answers(answer: Answers, onNext: () -> Unit, soundManager: SoundManager, correctQuestions: MutableState<Int>, wrongQuestions: MutableState<Int>) {
    val options = answer.answers
    val selectedOptionIndex = remember { mutableIntStateOf(-1) }
    val isButtonEnabled = remember { mutableStateOf(true) }
    val isProcessingClick = remember { mutableStateOf(false) }
    val correctOptionIndex = remember { mutableIntStateOf(-1) }
    val hasSelectedIncorrect = remember { mutableStateOf(false) }

    options.forEachIndexed { index, option ->
        val color = when (index) {
            0 -> 0xFF0303FF
            1 -> 0xFFFF0004
            2 -> 0xFF36D22E
            else -> 0xFFEB9D0D
        }

        val buttonSize by animateDpAsState(
            targetValue = if (selectedOptionIndex.intValue == index && option.correct) 350.dp else 300.dp,
            animationSpec = tween(durationMillis = 100), label = ""
        )

        val borderColor by animateColorAsState(
            targetValue = when {
                selectedOptionIndex.intValue == index && option.correct -> Color.Green
                selectedOptionIndex.intValue == index && !option.correct -> Color.Red
                correctOptionIndex.intValue == index && hasSelectedIncorrect.value -> Color.Green
                else -> Color.Transparent
            },
            animationSpec = tween(durationMillis = 100), label = ""
        )


        Button(
            shape = RoundedCornerShape(20),
            onClick = {
                if (!isProcessingClick.value) {
                    isProcessingClick.value = true
                    isButtonEnabled.value = false
                    selectedOptionIndex.intValue = index
                    if (options[index].correct) {
                        soundManager.playSound(R.raw.correct_answer)
                        correctQuestions.value++
                    } else {
                        soundManager.playSound(R.raw.wrong_answer)
                        wrongQuestions.value++
                        correctOptionIndex.intValue = options.indexOfFirst { it.correct }
                        hasSelectedIncorrect.value = true
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        selectedOptionIndex.intValue = -1
                        hasSelectedIncorrect.value = false
                        onNext()
                        isButtonEnabled.value = true
                        isProcessingClick.value = false
                    }, 2000)
                }
            },
            modifier = Modifier
                .size(width = buttonSize, height = buttonSize / 4)
                .border(4.dp, borderColor, RoundedCornerShape(20))
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(color)),
            enabled = isButtonEnabled.value && selectedOptionIndex.intValue == -1
        ) {
            Text(
                option.respuesta,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = when {
                    option.respuesta.length <= 20 -> 24.sp
                    option.respuesta.length <= 40 -> 20.sp
                    else -> 14.sp
                },
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
                softWrap = true,
                modifier = Modifier.wrapContentWidth()
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}



