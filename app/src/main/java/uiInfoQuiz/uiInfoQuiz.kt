package uiInfoQuiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun InfoQuizScreen(navigationActions: NavigationActions, scrollState: ScrollState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    )
    InfoQuiz(navigationActions)
}

@Composable
fun InfoQuiz(navigationActions: NavigationActions) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ImageQuiz()
            NameAndTag("Titulo", arrayOf("#tag1", "#tag2"))
        }
        Text(
            text = "Descripcion",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            color = Color(0x00000000)
        )
        Text(
            text = "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum."
        )
        Spacer(modifier = Modifier.padding(8.dp))
        ButtonPlay(navigationActions)
    }
}

@Composable
fun NameAndTag(title: String, tags: Array<String>) {
    Column() {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            color = Color(0x00000000)
        )
        Text(
            text = tags.joinToString(", "),
            fontFamily = poppinsFamily,
            color = Color(0x00000000)

        )
    }
}

@Composable
fun ImageQuiz() {
    Image(
        painter = painterResource(id = R.drawable.huppty),
        contentDescription = "Foto cuestionario",
        modifier = Modifier
            .size(width = 150.dp, height = 75.dp)
            .clip(RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp)),
        contentScale = Crop
    )
}

@Composable
fun ButtonPlay(navigationActions: NavigationActions) {
    Button(
        shape = RoundedCornerShape(20),
        onClick = { navigationActions.navigateToHome() },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
    ) {
        Text(
            "JUGAR",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            fontSize = 20.sp,
            color = Color(0xFFB18F4F)
        )
    }
}

