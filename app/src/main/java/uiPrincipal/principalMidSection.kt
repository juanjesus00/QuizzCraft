package uiPrincipal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

        if (!SharedState.isSearched && !SharedState.isSearchActive) {
            getStringByName(LocalContext.current, "my_quizzes")?.let {
                insertSectionQuiz(
                    titleSection = it,
                    titleQuiz = "Titulo",
                    navigationActions
                )
            }
            insertSectionLastQuizzies(navigationActions)
        } else {
            SearchedQuizzes(navigationActions)
        }

    }

}

@Composable
fun SearchedQuizzes(navigationActions: NavigationActions) {

    var isVisible by remember { mutableStateOf(false) }

    val animatedHeight by animateDpAsState(
        targetValue = if (SharedState.isClickedSuggestion) 125.dp else 50.dp,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Spacer(
        modifier = Modifier.height(animatedHeight)
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(initialOffsetY = { -it })
    ) {
        getStringByName(LocalContext.current, "result_search")?.let {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .heightIn(max = 700.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        header.quizzes.value.chunked(2).forEachIndexed { rowIndex, chunk ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chunk.forEachIndexed { index, quiz ->

                    val individualVisibility = remember { mutableStateOf(false) }
                    LaunchedEffect(quiz.quizId) {
                        individualVisibility.value = true
                    }

                    AnimatedVisibility(
                        visible = individualVisibility.value,
                        enter = scaleIn(
                            initialScale = 0.3f,
                            animationSpec = tween(durationMillis = 600, delayMillis = 100 * (rowIndex * chunk.size + index)) // Duración de la animación
                        ) + fadeIn(animationSpec = tween(durationMillis = 600))
                    ) {
                        favQuiz(
                            imageResource = quiz.quizImageUrl,
                            title = quiz.name,
                            titleSection = "Resultados",
                            navigationActions,
                            quizId = quiz.quizId
                        )
                        }

                }
            }
        }
    }
}
