package uiPrincipal

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun getPrincipalMidSection(scrollState: ScrollState){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .verticalScroll(scrollState)
            .padding(top = 80.dp),

        ){

        /*INSERTAMOS LOS QUIZ FAVORITOS*/
        insertSectionQuiz(titleSection = "Mis Cuestionarios:", titleQuiz = "Titulo")

        /*iNSERTAMOS LOS QUIZ MAS RECIENTES*/
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo")
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo")
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo")
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo")
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo")
        insertSectionQuiz(titleSection = "Cuestionarios Recientes:", titleQuiz = "Titulo")


    }
}