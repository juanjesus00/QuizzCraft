package quizcraft

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import routes.NavigationActions
import uiPrincipal.poppinsFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun uiQuizCraft(navigationActions: NavigationActions, scrollState: ScrollState) {

    var text by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .verticalScroll(scrollState)
            .padding(top = 150.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column (modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally){
            Box (
                modifier = Modifier
                    .border(1.dp, Color.Gray, RoundedCornerShape(20.dp)) // Borde personalizado
                    .padding(4.dp) // Espacio entre el borde y el TextField
            ){
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = "Ingrese un texto", modifier = Modifier) },
                    modifier = Modifier.background(color = Color(0xFFFFFDFD), shape = RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )

                )
            }
        }
    }

}
