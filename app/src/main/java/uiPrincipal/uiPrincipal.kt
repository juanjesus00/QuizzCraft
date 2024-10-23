package uiPrincipal



import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import kotlinx.coroutines.launch
import navigator.uiNavigator


val poppinsFamily = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.poppinsregular, FontWeight.Normal),
    androidx.compose.ui.text.font.Font(R.font.poppinsbold, FontWeight.Bold)
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyComposeApp() {
    val scrollState = rememberScrollState()
    var showBars by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.foto),
                        contentDescription = "Imagen De ejemplo",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(100.dp)),
                        contentScale = ContentScale.Crop //recorta la imagen si no esta muy cuadrada
                    )

                    IconButton(onClick = { /*Hamburguer*/ },
                    ) {
                        Image(
                            painterResource(id = R.drawable.menu),
                            contentDescription = "Menu",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = showBars) {
                Box (
                    modifier =  Modifier.offset(y = (-10).dp)
                ){
                    uiNavigator()
                }
            }
        }
    ) {
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

}



