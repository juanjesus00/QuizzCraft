package navigator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.myapplication.R
import header.CustomPopupMenu
import header.popupInformation

@Composable
fun uiNavigator(){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ){
        Row (
            modifier = Modifier
                .fillMaxHeight()
                .width(350.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFEBEBEC)),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ){
            printImage(imageResource = R.drawable.house , description = "home")
            printImage(imageResource = R.drawable.moreoptions , description = "options")
            printImage(imageResource = R.drawable.lupa , description = "search")
        }
    }
}

@Composable
fun printImage(imageResource: Int, description: String){
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(
        onClick = {
            when(description){
                "home" -> print("home")
                "options" -> expanded = !expanded
                "search" -> print("search")
                else -> print("opcion incorrecta")
            }
        }
    ) {
        Image(
            painterResource(id = imageResource),
            contentDescription = description
        )
    }

    if (description == "options"){
        CustomPopupMenu(expanded = expanded, onDismissRequest = { expanded = false})
    }
}


