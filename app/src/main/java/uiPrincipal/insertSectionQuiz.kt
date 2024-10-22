package uiPrincipal

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun insertSectionQuiz(titleSection: String, titleQuiz: String, ){
    Spacer(modifier = Modifier.height(50.dp))
    Text(
        text = titleSection,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = 15.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = poppinsFamily,
    )
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,

        ) {
        //falta poner un for cuando tengamos la base de datos conectada
        favQuiz(imageResource = R.drawable.rectanglewhite, title = titleQuiz)
        favQuiz(imageResource = R.drawable.rectanglewhite, title = titleQuiz)
        favQuiz(imageResource = R.drawable.rectanglewhite, title = titleQuiz)
        favQuiz(imageResource = R.drawable.rectanglewhite, title = titleQuiz)
        favQuiz(imageResource = R.drawable.rectanglewhite, title = titleQuiz)
    }
}