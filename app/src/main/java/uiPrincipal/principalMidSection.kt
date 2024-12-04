package uiPrincipal

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import quizcraft.uiQuizCraft
import routes.Routes
import routes.NavigationActions

@Composable
fun getPrincipalMidSection(
    scrollState: ScrollState,
    navigationActions: NavigationActions,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .verticalScroll(scrollState)
            .padding(top = 80.dp)

    ) {

        if (!SharedState.isSearched) {
            insertSectionQuiz(
                titleSection = "Mis Cuestionarios:",
                titleQuiz = "Titulo",
                navigationActions
            )
            insertSectionQuiz(
                titleSection = "Cuestionarios Recientes:",
                titleQuiz = "Titulo",
                navigationActions
            )
        } else {
            SearchedQuizzes(navigationActions)
        }

    }

}

@Composable
fun SearchedQuizzes(navigationActions: NavigationActions) {
    Spacer(modifier = Modifier.height(50.dp))
    Text(
        text = "Resultados: ",
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = 15.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = poppinsFamily,
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        for(quiz in header.quizzes.value) {
            println(quiz)
            favQuiz(imageResource = quiz.quizImageUrl, title = quiz.name, titleSection = "Resultados", navigationActions, quizId = quiz.quizId)
        }
    }
}