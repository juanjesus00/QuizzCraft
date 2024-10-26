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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import routes.NavigationActions
import com.example.myapplication.R
import uiPrincipal.poppinsFamily

@Composable
fun CustomPopupMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    color: Int,
    size: Int,
    type: String,
    navigationActions: NavigationActions
) {
    if (expanded) {
        if(type == "header"){
            Popup(
                alignment = Alignment.TopEnd, // Posición del popup en la pantalla
                onDismissRequest = onDismissRequest
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(color),
                            shape = RoundedCornerShape(16.dp)
                        ) // Personaliza el fondo y las esquinas redondeadas
                        .size(size.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.Start
                    ){
                        popupInformation(icon = R.drawable.solar_user_bold, text = "Iniciar sesión",navigationActions)
                        popupInformation(icon = R.drawable.ph_sign_in_bold, text = "Registrarse",navigationActions)
                        popupInformation(icon = R.drawable.ligth_mode, text = "Modo claro",navigationActions)
                    }
                }
            }
        }else if(type == "navigator"){
            Popup(
                alignment = Alignment.TopEnd, // Posición del popup en la pantalla
                onDismissRequest = onDismissRequest
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(color),
                            shape = RoundedCornerShape(16.dp)
                        ) // Personaliza el fondo y las esquinas redondeadas
                        .size(size.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "¿Qué quieres hacer?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFamily,
                            color = Color(0xFF212325)
                        )

                        Row (
                            modifier = Modifier
                                .background(Color(0xFF212325), shape = RoundedCornerShape(20.dp))
                                .size(height = 70.dp, width = 200.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Elección",
                                modifier = Modifier
                                /*.clickable {
                                    when(text){
                                        "Iniciar sesión" -> navigationActions.navigateToLogin()
                                        "Registrarse" -> navigationActions.navigateToRegister()
                                        else -> print("opcion no valida")
                                    }
                                }*/,
                                color = Color(0xFFB18F4F),
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsFamily,
                                fontSize = 15.sp
                            )
                        }

                        Row (
                            modifier = Modifier
                                .background(Color(0xFF212325),shape = RoundedCornerShape(20.dp))
                                .size(height = 70.dp, width = 200.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center

                        ) {
                            Text(
                                text = "Interacción",
                                modifier = Modifier
                                    /*.clickable {
                                        when(text){
                                            "Iniciar sesión" -> navigationActions.navigateToLogin()
                                            "Registrarse" -> navigationActions.navigateToRegister()
                                            else -> print("opcion no valida")
                                        }
                                    }*/,
                                color = Color(0xFFB18F4F),
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsFamily,
                                fontSize = 15.sp
                            )
                        }




                    }
                }
            }
        }

    }
}

@Composable
fun popupInformation(icon: Int, text: String, navigationActions: NavigationActions){
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = text,
            modifier = Modifier
                .clickable {
                    when(text){
                        "Iniciar sesión" -> navigationActions.navigateToLogin()
                        "Registrarse" -> navigationActions.navigateToRegister()
                        else -> print("opcion no valida")
                    }
                           },
            color = Color(0xFFB18F4F)
        )

        Image(
            painterResource(id = icon ),
            contentDescription = text,
        )
    }
}