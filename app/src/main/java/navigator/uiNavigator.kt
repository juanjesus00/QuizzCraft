package navigator

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import header.quizzes
import languagesBack.getStringByName
import menuHamburguesa.CustomPopupMenu
import model.Quiz
import quizcraft.searchQuizzesByTag
import routes.NavigationActions
import uiLogin.loginbacked
import uiPrincipal.SharedState

@Composable
fun uiNavigator(navigationActions: NavigationActions) {

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
            printImage(imageResource = R.drawable.house , description = "home",navigationActions)
            Box {
                printImage(imageResource = R.drawable.moreoptions , description = "options",navigationActions)
            }

            printImage(imageResource = R.drawable.lupa , description = "search",navigationActions)
        }
    }
}

@Composable
fun printImage(imageResource: Int, description: String, navigationActions: NavigationActions, viewModel: loginbacked = androidx.lifecycle.viewmodel.compose.viewModel()){
    val context = LocalContext.current
    val isVerified by viewModel.isEmailVerified.observeAsState(false)
    LaunchedEffect(Unit) {
        viewModel.checkIfEmailVerified()
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(
        onClick = {
            when(description) {
                "home" -> {
                    SharedState.isSearchActive = false
                    SharedState.isSearched = false
                    SharedState.isClickedSuggestion = false
                    header.quizzes.value = emptyList()
                    navigationActions.navigateToHome()
                }
                "options" -> {
                    val user = FirebaseAuth.getInstance().currentUser
                    if(user == null) {
                        Toast.makeText(context, getStringByName(context, "warning_try_create_a_quiz"), Toast.LENGTH_SHORT).show()
                    }else if(!isVerified){
                        Toast.makeText(context, getStringByName(context, "warning_try_create_a_quiz_unverified"), Toast.LENGTH_SHORT).show()
                    } else {
                        expanded = !expanded
                    }
                }
                "search" -> {
                    SharedState.isSearchActive = true
                    SharedState.isSearched = true
                }
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
        Popup (){
            Box (modifier = Modifier
                .fillMaxWidth()
                .offset(x = -(75).dp, y = 350.dp)){
                CustomPopupMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false},
                    color = 0xFFEBEBEC.toInt(),
                    size = 250,
                    type = "navigator",
                    navigationActions = navigationActions,
                    quizId = ""
                )
            }
        }
    }
}