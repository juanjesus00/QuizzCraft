package uiPrincipal

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
        modifier = Modifier.size(200.dp).padding(16.dp).clip(RoundedCornerShape(100.dp))

    ){
        Column (
            modifier = Modifier.background(Color(0xFF212325)).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Foto cuestionario",
                modifier = Modifier
                    .size(width = 200.dp, 100.dp).padding(bottom = 10.dp).clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
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
    }
}