package quizcraft

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import routes.NavigationActions
import uiPrincipal.insertSectionQuiz

@Composable
fun uiQuizCraft(navigationActions: NavigationActions, scrollState: ScrollState) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .verticalScroll(scrollState)
            .padding(top = 80.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo", navigationActions)
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo", navigationActions)
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo", navigationActions)
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo", navigationActions)
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo", navigationActions)
    }

}
