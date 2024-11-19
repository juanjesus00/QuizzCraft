package uiPrincipal


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.R
import header.getHeader
import kotlinx.coroutines.launch
import navigator.uiNavigator
import quizcraft.uiQuizCraft
import routes.NavigationActions
import routes.Routes
import uiEditQuiz.EditQuizScreen
import uiEditUser.EditUserScreen
import uiGame.GameScreen
import uiInfoQuiz.InfoQuizScreen
import uiResult.ResultScreen
import uiUserInfo.UserInfoScreen


val poppinsFamily = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.poppinsregular, FontWeight.Normal),
    androidx.compose.ui.text.font.Font(R.font.poppinsbold, FontWeight.Bold)
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyComposeApp(navigationActions: NavigationActions, navController: NavHostController) {
    val scrollState = rememberScrollState()
    var showBars by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    LaunchedEffect(scrollState.value) {
        coroutineScope.launch {
            val currentOffset = scrollState.value

            // Mostrar/ocultar barras basado en la dirección del scroll
            if (currentOffset > previousScrollOffset) {
                showBars = false // Ocultar cuando el usuario scrollea hacia arriba
            } else {
                showBars = true // Mostrar cuando el usuario scrollea hacia abajo
            }
            previousScrollOffset = currentOffset
        }
    }
    Scaffold(
        topBar = {
            AnimatedVisibility(visible = showBars) {
                getHeader(navigationActions)
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = showBars) {
                Box(
                    modifier = Modifier.offset(y = (-10).dp)
                ) {
                    uiNavigator(navigationActions)
                }
            }
        }
    ) {
        if (currentRoute == Routes.HOME) {
            getPrincipalMidSection(scrollState = scrollState, navigationActions, navController)
        } else if (currentRoute == Routes.CRAFT) {
            uiQuizCraft(navigationActions = navigationActions, scrollState)
        } else if (currentRoute == Routes.USERINFO) {
            UserInfoScreen(navigationActions, scrollState = scrollState)
        } else if (currentRoute == Routes.EDITUSER) {
            EditUserScreen(navigationActions)
        } else if (currentRoute == Routes.EDITQUIZ) {
            EditQuizScreen(navigationActions, scrollState = scrollState)
        } else if (currentRoute == Routes.INFOQUIZ) {
            InfoQuizScreen(navigationActions, scrollState = scrollState)
        } else if (currentRoute == Routes.GAME) {
            GameScreen(navigationActions, scrollState = scrollState, 'i')
        } else if (currentRoute == Routes.RESULT) {
            ResultScreen(navigationActions, scrollState = scrollState)
        }

    }
}



