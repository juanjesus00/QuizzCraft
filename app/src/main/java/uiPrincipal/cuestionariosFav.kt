package uiPrincipal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun favQuiz(imageResource: Int, title: String){
    Box(
        modifier = Modifier.size(200.dp).padding(16.dp)
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.size(150.dp).clip(RoundedCornerShape(100.dp)).background(Color(0xFF212325)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = "Foto cuestionario",
                    modifier = Modifier
                        .size(width = 150.dp, height = 75.dp)
                        .clip(RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp)),
                    contentScale = ContentScale.Crop //recorta la imagen si no esta muy cuadrada
                )
                Text(
                    text = title,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    color = Color(0xFFFFFFFF)
                )
            }
            IconButton(onClick = {/*opciones*/},
                modifier = Modifier.padding(bottom = 100.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.trespuntos),
                    contentDescription = "Menu tres puntos",
                    contentScale = ContentScale.Crop //recorta la imagen si no esta muy cuadrada
                )
            }


        }
    }
}