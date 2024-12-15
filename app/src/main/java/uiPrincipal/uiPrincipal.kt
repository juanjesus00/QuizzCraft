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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import carga.LoadingScreen
import com.example.myapplication.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import header.getHeader
import kotlinx.coroutines.launch
import navigator.uiNavigator
import quizcraft.uiQuizCraft
import routes.NavigationActions
import routes.Routes
import uiAbout.AboutScreen
import uiEditQuiz.EditQuizScreen
import uiEditUser.EditUserScreen
import uiGame.GameScreen
import uiInfoQuiz.InfoQuizScreen
import uiLogin.LoginScreen
import uiRegister.RegisterScreen
import uiResult.ResultScreen
import uiUserInfo.UserInfoScreen

val jsonString = """
{
  "questions": [
    {
      "question": "which of these are management methodologies?",
      "answer": {
        "answers": [
          { "respuesta": "Metrica3, King2, Scrum, Lean", "correct": false },
          { "respuesta": "Metrica3, prince2, Rugby, Lean", "correct": false },
          { "respuesta": "metrica3, prince2, Scrum, Gangnam", "correct": false },
          { "respuesta": "Metrica3, Prince2, Scrum, Lean", "correct": true }
        ]
      }
    },
    {
      "question": "de locos y si",
      "answer": {
        "answers": [
          { "respuesta": "Metrica3, King2, Scrum, Lean", "correct": false },
          { "respuesta": "Metrica3, prince2, Rugby, Lean", "correct": true },
          { "respuesta": "metrica3, prince2, Scrum, Gangnam", "correct": false },
          { "respuesta": "Metrica3, Prince2, Scrum, Lean", "correct": false }
        ]
      }
    }
  ]
}
""".trimIndent()


val poppinsFamily = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.poppinsregular, FontWeight.Normal),
    androidx.compose.ui.text.font.Font(R.font.poppinsbold, FontWeight.Bold)
)

object SharedState {
    var isSearchActive by mutableStateOf(false)
    var isSearched by mutableStateOf(false)
    var isClickedSuggestion by mutableStateOf(false)
}

object LanguageManager {
    var languageCode by mutableStateOf("")
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyComposeApp(navigationActions: NavigationActions, navController: NavHostController) {
    val scrollState = rememberScrollState()
    var showBars by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val systemUiController = rememberSystemUiController()
    val color = Color(0xFFE0D4C8) // Cambia por el color de tu aplicación

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = false // Cambia a true si usas íconos oscuros
        )
        systemUiController.setNavigationBarColor(
            color = color,
            darkIcons = false // Cambia a true si usas íconos oscuros
        )
    }


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
                getHeader(navigationActions, navController)
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
            EditUserScreen(navigationActions, scrollState)
        } else if (currentRoute == Routes.EDITQUIZ) {
            EditQuizScreen(navigationActions, scrollState = scrollState, navController)
        } else if (currentRoute == Routes.INFOQUIZ) {
            InfoQuizScreen(navigationActions, scrollState = scrollState, navController)
        } else if (currentRoute == Routes.GAME) {
            GameScreen(navigationActions, scrollState = scrollState, 't', navController)
        } else if (currentRoute == Routes.ABOUT) {
            AboutScreen(navigationActions)
        }
    }
}



