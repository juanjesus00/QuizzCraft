package uiResult

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import languagesBack.getStringByName
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@SuppressLint("DefaultLocale")
@Composable
fun ResultScreen(navigationActions: NavigationActions, navController: NavHostController) {

    val navBackStackEntry = remember { navController.currentBackStackEntry }
    val correctQuestions = navBackStackEntry?.arguments?.getString("correctQuestion")?.toInt() ?: 0
    val wrongQuestions = navBackStackEntry?.arguments?.getString("wrongQuestion")?.toInt() ?: 0

    val result = (correctQuestions.toDouble() / (correctQuestions + wrongQuestions)) * 10
    val formattedresult = String.format("%.2f", result)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {

        var isVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            isVisible = true
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(
                initialScale = 0.3f,
                animationSpec = tween(durationMillis = 800)
            ) + fadeIn(animationSpec = tween(durationMillis = 800))
        ) {
            Result(navigationActions, correctQuestions, wrongQuestions, formattedresult)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun Result(
    navigationActions: NavigationActions,
    correctQuestions: Int,
    wrongQuestions: Int,
    result: String
) {


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            modifier = Modifier
                .background(Color.Black, shape = RoundedCornerShape(5))
                .size(width = 400.dp, height = 400.dp)
        ) {
            getStringByName(LocalContext.current, "score")?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 38.sp, color = Color(0xFFFFFFFF),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = result, fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 64.sp, color = Color(0xFFB18F4F),
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                getStringByName(
                    LocalContext.current,
                    "correct_answers"
                ) + " " + "$correctQuestions", fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 18.sp, color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 96.dp)
            )
            Text(
                getStringByName(
                    LocalContext.current,
                    "incorrect_answers"
                ) + " " + "$wrongQuestions", fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 18.sp, color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp)
            )

        }
        Spacer(modifier = Modifier.padding(8.dp))
        HomeButton(navigationActions)
    }
}

@Composable
fun HomeButton(navigationActions: NavigationActions) {
    Button(
        shape = RoundedCornerShape(20),
        onClick = {
            navigationActions.navigateToHome()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
    ) {
        getStringByName(LocalContext.current, "home_button_results")?.let {
            Text(
                it,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 24.sp,
                color = Color(0xFFB18F4F)
            )
        }
    }
}
