package uiPrincipal

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun favQuiz(){
    Box(

    ){
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.rectanglewhite),
                contentDescription = "Foto cuestionario",
                modifier = Modifier
                    .size(60.dp),
                contentScale = ContentScale.Crop //recorta la imagen si no esta muy cuadrada
            )
            Text(
                text = "Titulo",
                modifier = Modifier.fillMaxWidth().offset(x = 10.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily
            )
        }
    }
}