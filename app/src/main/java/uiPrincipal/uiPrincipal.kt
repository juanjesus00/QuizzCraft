package uiPrincipal


import android.graphics.fonts.Font
import android.graphics.fonts.FontStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R



val poppinsFamily = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.poppinsregular, FontWeight.Normal),
    androidx.compose.ui.text.font.Font(R.font.poppinsbold, FontWeight.Bold)
)


@Composable
fun MyComposeApp() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8)),

    ){
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
                       modifier = Modifier.size(30.dp))
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Text(text = "Mis Cuestionarios: ",
            modifier = Modifier.fillMaxWidth().offset(x = 15.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            )
    }
    

}
