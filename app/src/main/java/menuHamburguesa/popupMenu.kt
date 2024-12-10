package menuHamburguesa

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import routes.NavigationActions
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import languagesBack.getStringByName
import quizcraft.deleteQuizFromFirestore
import uiPrincipal.LanguageManager
import uiPrincipal.poppinsFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPopupMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    color: Int,
    size: Int,
    type: String,
    navigationActions: NavigationActions,
    quizId: String
) {

    val languageMenuExpanded = remember { mutableStateOf(false) }


    if (expanded) {
        if (type == "header") {
            Popup(
                alignment = Alignment.TopEnd,
                onDismissRequest = onDismissRequest
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(color),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .size(size.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.Start
                    ) {
                        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
                            getStringByName(LocalContext.current, "login")?.let {
                                popupInformation(
                                    icon = R.drawable.solar_user_bold,
                                    text = it,
                                    navigationActions,
                                    LocalContext.current

                                )
                            }
                            getStringByName(LocalContext.current, "register")?.let {
                                popupInformation(
                                    icon = R.drawable.ph_sign_in_bold,
                                    text = it,
                                    navigationActions,
                                    LocalContext.current

                                )
                            }
                        } else {
                            getStringByName(LocalContext.current, "logout")?.let {
                                popupInformation(
                                    icon = R.drawable.log_out,
                                    text = it,
                                    navigationActions = navigationActions,
                                    LocalContext.current
                                )
                            }
                        }
                        getStringByName(LocalContext.current, "light_mode")?.let {
                            popupInformation(
                                icon = R.drawable.ligth_mode,
                                text = it,
                                navigationActions,
                                LocalContext.current
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            getStringByName(LocalContext.current, "language")?.let {
                                popupInformation(
                                    icon = R.drawable.language,
                                    text = it,
                                    navigationActions = navigationActions,
                                    LocalContext.current,
                                    onClick = {
                                        println("Antes de alternar: languageMenuExpanded = ${languageMenuExpanded.value}")
                                        languageMenuExpanded.value = !languageMenuExpanded.value
                                        println("Despues de alternar: languageMenuExpanded = ${languageMenuExpanded.value}")
                                    },
                                )
                            }

                            DropdownMenu(
                                expanded = languageMenuExpanded.value,
                                onDismissRequest = { languageMenuExpanded.value = false },
                                offset = DpOffset(
                                    245.dp,
                                    87.dp
                                ),
                                modifier = Modifier
                                    .width(80.dp)
                                    .background(color = Color(color)
                                )
                            ) {
                                DropdownMenuItem(
                                    text = { getStringByName(LocalContext.current, "english")?.let {
                                        Text(
                                            it, color = Color(0xFFB18F4F))
                                    } },
                                    onClick = {
                                        languageMenuExpanded.value = false
                                        LanguageManager.languageCode = "en"
                                        println(LanguageManager.languageCode)

                                    }
                                )
                                DropdownMenuItem(
                                    text = { getStringByName(LocalContext.current, "spanish")?.let {
                                        Text(
                                            it, color = Color(0xFFB18F4F))
                                    } },
                                    onClick = {
                                        languageMenuExpanded.value = false
                                        LanguageManager.languageCode = ""

                                    }
                                )
                            }
                        }


                    }
                }
            }
        } else if (type == "navigator") {
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        getStringByName(LocalContext.current, "what_option_create_quiz_popup")?.let {
                            Text(
                                text = it,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsFamily,
                                color = Color(0xFF212325)
                            )
                        }
                        getStringByName(LocalContext.current, "select_option_create_quiz_popup")?.let {
                            popupInformationNavigator(
                                padding = 0,
                                text = it,
                                icon = R.drawable.elecction,
                                navigationActions,
                                LocalContext.current
                            )
                        }
                        getStringByName(LocalContext.current, "interaction_option_create_quiz_popup")?.let {
                            popupInformationNavigator(
                                padding = 10,
                                text = it,
                                icon = R.drawable.interactivo,
                                navigationActions,
                                context = LocalContext.current
                            )
                        }

                    }
                }
            }
        } else if (type == "CrudQuiz") {
            Popup(
                alignment = Alignment.CenterEnd, // Posición del popup en la pantalla
                onDismissRequest = onDismissRequest
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(color),
                            shape = RoundedCornerShape(16.dp)
                        ) // Personaliza el fondo y las esquinas redondeadas
                        .size(width = (size / 2).dp, height = size.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Image(
                            painterResource(id = R.drawable.basura),
                            contentDescription = "Eliminar",
                            modifier = Modifier.clickable {
                                CoroutineScope(Dispatchers.Main).launch {
                                    val wasDeleted = deleteQuizFromFirestore(quizId)
                                    if (wasDeleted) {
                                        navigationActions.navigateToHome()
                                    } else {
                                        println("No se pudo eliminar el cuestionario")
                                    }
                                }
                            }

                        )
                        Image(
                            painterResource(id = R.drawable.editar),
                            contentDescription = "Editar",
                            modifier = Modifier.clickable {
                                navigationActions.navigateToEditQuiz(
                                    quizId
                                )
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun popupInformationNavigator(
    padding: Int,
    text: String,
    icon: Int,
    navigationActions: NavigationActions,
    context: Context
) {
    Row(
        modifier = Modifier
            .background(Color(0xFF212325), shape = RoundedCornerShape(20.dp))
            .size(height = 70.dp, width = 200.dp)
            .clickable {
                when (text) {
                    getStringByName(context,"select_option_create_quiz_popup" ) -> navigationActions.navigateToQuizCraft()
                    getStringByName(context, "interaction_option_create_quiz_popup") -> print("proximamente")
                    else -> print("opcion no valida")
                }
            }
            .padding(padding.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, shape = RoundedCornerShape(50.dp))
                .clickable { /*algo*/ }, contentAlignment = Alignment.Center
        ) {
            Image(painterResource(id = icon), contentDescription = text)
        }
        Text(
            text = text,
            modifier = Modifier
                .clickable {/*algo*/ },
            color = Color(0xFFB18F4F),
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            fontSize = 15.sp
        )
    }
}

@Composable
fun popupInformation(
    icon: Int,
    text: String,
    navigationActions: NavigationActions,
    context: Context,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = text,
            modifier = Modifier
                .clickable {
                    when (text) {
                        getStringByName(context, "login") -> navigationActions.navigateToLogin()
                        getStringByName(context, "register") -> navigationActions.navigateToRegister()
                        getStringByName(context, "logout") -> {
                            logOut(navigationActions)
                            navigationActions.navigateToHome()
                        }

                        getStringByName(context, "language") -> {
                            onClick?.invoke()
                        }

                        else -> print("opcion no valida")
                    }
                },
            color = Color(0xFFB18F4F)
        )

        Image(
            painterResource(id = icon),
            contentDescription = text,
        )
    }
}

fun logOut(navigationActions: NavigationActions) {
    FirebaseAuth.getInstance().signOut()
    navigationActions.navigateToHome()
}


