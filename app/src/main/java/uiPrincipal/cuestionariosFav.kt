package uiPrincipal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.R
import languagesBack.getStringByName
import menuHamburguesa.CustomPopupMenu
import routes.NavigationActions

@Composable
fun favQuiz(imageResource: String, title: String, titleSection: String,navigationActions: NavigationActions, quizId: String){
    var expanded  by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp)
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0xFF212325))
                    .clickable { navigationActions.navigateToInfoQuiz(quizId) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = imageResource,
                    contentDescription = "Foto cuestionario",
                    modifier = Modifier
                        .size(width = 150.dp, height = 75.dp)
                        .clip(RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp)),
                    contentScale = Crop
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
            Box (modifier = Modifier.alpha(if (titleSection == getStringByName(LocalContext.current, "my_quizzes")) 1f else 0f)){
                IconButton(onClick = {expanded = !expanded},
                    modifier = Modifier.padding(bottom = 100.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.trespuntos),
                        contentDescription = "Menu tres puntos",
                        contentScale = Crop
                    )
                }
                Box {
                    CustomPopupMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false},
                        color = 0xFF212325.toInt(),
                        size = 100,
                        type = "CrudQuiz",
                        navigationActions = navigationActions,
                        quizId = quizId
                    )
                }
            }
        }

    }

}