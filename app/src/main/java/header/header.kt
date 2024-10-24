

package header

import android.widget.PopupMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.myapplication.R



@Composable
fun getHeader(){
    var expanded by remember {
        mutableStateOf(false)
    }
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


        Box{
            IconButton(onClick = { expanded = !expanded },
            ) {
                Image(
                    painterResource(id = R.drawable.menu),
                    contentDescription = "Menu",
                    modifier = Modifier.size(30.dp)
                )
            }
            Box{
                CustomPopupMenu(expanded = expanded, onDismissRequest = {expanded = false})
            }

        }

    }
}

@Composable
fun CustomPopupMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    if (expanded) {
        Popup(
            alignment = Alignment.TopEnd, // Posición del popup en la pantalla
            onDismissRequest = onDismissRequest
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF212325), shape = RoundedCornerShape(16.dp)) // Personaliza el fondo y las esquinas redondeadas
                    .size(150.dp)
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