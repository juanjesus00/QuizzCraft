package menuHamburguesa

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.myapplication.R

@Composable
fun CustomPopupMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    color: Int,
    size: Int
) {
    if (expanded) {
        Popup(
            alignment = Alignment.TopEnd, // Posición del popup en la pantalla
            onDismissRequest = onDismissRequest
        ) {
            Box(
                modifier = Modifier
                    .background(Color(color), shape = RoundedCornerShape(16.dp)) // Personaliza el fondo y las esquinas redondeadas
                    .size(size.dp)
            ) {
                Column (
                    modifier = Modifier.fillMaxSize().padding(start = 10.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start
                ){
                    // Opción 1 del menú
                    popupInformation(icon = R.drawable.solar_user_bold, text = "Iniciar sesión")
                    popupInformation(icon = R.drawable.ph_sign_in_bold, text = "Registrarse")
                    popupInformation(icon = R.drawable.ligth_mode, text = "Modo claro")



                }
            }
        }
    }
}

@Composable
fun popupInformation(icon: Int, text: String){
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = text,
            modifier = Modifier
                .clickable { /*nada*/ },
            color = Color(0xFFB18F4F)
        )

        Image(
            painterResource(id = icon ),
            contentDescription = text,
        )
    }
}