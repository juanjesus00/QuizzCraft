

package header

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import menuHamburguesa.CustomPopupMenu
import routes.NavigationActions


@Composable
fun getHeader(navigationActions: NavigationActions) {
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
            contentScale = ContentScale.Crop
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
                CustomPopupMenu(expanded = expanded,
                    onDismissRequest = {expanded = false},
                    color = 0xFF212325.toInt(),
                    size = 150,
                    type = "header",
                    navigationActions = navigationActions
                )
            }

        }

    }
}
