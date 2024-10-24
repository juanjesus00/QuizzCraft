

package header

import android.widget.PopupMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    surface = Color.Transparent // Color de fondo personalizado
                ),
                shapes = MaterialTheme.shapes.copy(
                    medium = RoundedCornerShape(20.dp) // Uso de dp en lugar de f
                )
            ) {
                Box (

                ){

                }
                /*DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)// Aplicar el color y la forma personalizada
                ){
                    DropdownMenuItem(text = { Text(text = "hello")}, onClick = { expanded = false}, modifier = Modifier.clip(RoundedCornerShape(20.dp)))
                }*/
            }

            /*DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }, // Cierra el menú al hacer clic fuera
                modifier = Modifier.size(200.dp).clip(RoundedCornerShape(20.dp)),
            ) {
                DropdownMenuItem(text = { Text(text = "hello")}, onClick = { expanded = false}, modifier = Modifier.clip(RoundedCornerShape(20.dp)))
            }*/


        }

    }
}


