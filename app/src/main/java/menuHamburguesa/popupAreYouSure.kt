package menuHamburguesa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import languagesBack.getStringByName
import quizcraft.deleteQuizFromFirestore
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun CustomPopupAreYouSure(
    type: Char,
    userOrquizId: String,
    navigationActions: NavigationActions,
    expanded: MutableState<Boolean>
) {

    if (expanded.value) {
        Popup(
            alignment = Alignment.TopEnd,
            onDismissRequest = { expanded.value = false }
        ) {
            Column(
                modifier = Modifier
                    .padding(45.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF212325), shape = RoundedCornerShape(16.dp)),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                (if (type == 'q') getStringByName(LocalContext.current, "are_you_sure_deletequiz") else "Aqui pondrias el de usuario")?.let {
                    Text(
                        text = it,
                        fontSize = 17.sp,
                        color = Color(0xFFB18F4F),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TypeButton('a', type, userOrquizId, navigationActions, state = expanded)
                    TypeButton('c', type, userOrquizId, navigationActions, state = expanded)
                }
            }
        }
    }
}


@Composable
fun TypeButton(type: Char, option: Char, userOrquizId: String, navigationActions: NavigationActions, state: MutableState<Boolean>) {
    Button(
        onClick = {
            if (option == 'q') {
                if(type == 'a') {
                    CoroutineScope(Dispatchers.Main).launch {
                        val wasDeleted = deleteQuizFromFirestore(userOrquizId)
                        if (wasDeleted) {
                            navigationActions.navigateToHome()
                        } else {
                            println("No se pudo eliminar el cuestionario")
                        }
                    }
                }
                state.value = false
            } else {
                /* Aqui pondrias la logica que quieras para el de usuario*/
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (type == 'a') Color(0xFF4CCC4C) else Color(
                0xFFCC4C4C
            )
        ),
        modifier = Modifier.padding(10.dp)
    ) {
        (if (type == 'a') getStringByName(LocalContext.current, "accept_button_text") else getStringByName(
            LocalContext.current, "cancel_button_text"))?.let {
            Text(
                text = it,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
            )
        }
    }
}