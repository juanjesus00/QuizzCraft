package uiUserInfo

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import languagesBack.getStringByName
import menuHamburguesa.CustomPopupAreYouSure
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun UserInfoScreen(
    navigationActions: NavigationActions,
    viewModelUser: userInfoBack = viewModel(),
    scrollState: ScrollState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    ) {
        UserInfo(Modifier.align(Alignment.Center), navigationActions, viewModelUser)
    }
}

@Composable
fun UserInfo(
    modifier: Modifier,
    navigationActions: NavigationActions,
    viewModelUser: userInfoBack
) {
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf<String?>(null) }
    var userEmail by remember { mutableStateOf<String?>(null) }
    var createdQuiz by remember { mutableStateOf<Int?>(null) }
    var passQuiz by remember { mutableStateOf<Int?>(null) }
    var userId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModelUser.getInfoUser { user ->
            profileImageUrl = user?.get("PerfilImage") as? String
            userName = user?.get("userName") as? String
            userEmail = user?.get("email") as? String
            createdQuiz = user?.get("createdQuiz") as? Int
            passQuiz = user?.get("passQuiz") as? Int
            userId = user?.get("user_id") as? String
        }
    }
    Column(modifier = modifier) {
        ImageProfile(
            Modifier.align(Alignment.CenterHorizontally),
            navigationActions,
            profileImageUrl
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Field(
            Modifier.align(Alignment.CenterHorizontally),
            'u',
            userName,
            userEmail,
            createdQuiz,
            passQuiz,
            1.25,
            LocalContext.current
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Field(
            Modifier.align(Alignment.CenterHorizontally),
            'e',
            userName,
            userEmail,
            createdQuiz,
            passQuiz,
            1.5,
            LocalContext.current
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Field(
            Modifier.align(Alignment.CenterHorizontally),
            'c',
            userName,
            userEmail,
            createdQuiz,
            passQuiz,
            1.75,
            LocalContext.current
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Field(
            Modifier.align(Alignment.CenterHorizontally),
            'r',
            userName,
            userEmail,
            createdQuiz,
            passQuiz,
            2.0,
            LocalContext.current
        )
        Spacer(modifier = Modifier.padding(12.dp))
        DeleteUserButton(userId, navigationActions)

    }
}

@Composable
fun DeleteUserButton(userId: String?, navigationActions: NavigationActions) {
    val showPopupAreYouSure = remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            initialScale = 0.3f,
            animationSpec = tween(durationMillis = 800)
        ) + fadeIn(animationSpec = tween(durationMillis = 800))
    ){
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Button(
                shape = RoundedCornerShape(20),
                onClick = { showPopupAreYouSure.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
            ) {
                getStringByName(LocalContext.current, "delete_user")?.let {
                    Text(
                        it,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily,
                        fontSize = 20.sp,
                        color = Color(0xFFB18F4F)
                    )
                }
            }
        }
    }
    if (showPopupAreYouSure.value) {
        CustomPopupAreYouSure(
            'u', userId.toString(), navigationActions, showPopupAreYouSure
        )
    }

}

@Composable
fun ImageProfile(
    modifier: Modifier,
    navigationActions: NavigationActions,
    profileImageUrl: String?
) {

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val iconScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 800)
    )

    Box(
        modifier = modifier.size(180.dp)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(
                initialScale = 0.3f,
                animationSpec = tween(durationMillis = 800)
            ) + fadeIn(animationSpec = tween(durationMillis = 800))
        ) {
            if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.foto),
                    contentDescription = "ProfileImage",
                    contentScale = ContentScale.Fit,
                    modifier = modifier
                        .size(208.dp)
                        .clip(RoundedCornerShape(100.dp))
                )
            } else {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Foto de perfil",
                    error = painterResource(id = R.drawable.perro_mordor), // Opcional, si tienes una imagen de error
                    modifier = modifier
                        .size(208.dp)
                        .clip(RoundedCornerShape(100.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Icon(
            painter = painterResource(id = R.drawable.editar),
            contentDescription = "EditProfile",
            tint = Color(0xFFB18F4F),
            modifier = Modifier
                .size(48.dp * iconScale)
                .align(Alignment.BottomEnd)
                .clickable { navigationActions.navigateToEditUser() }
        )

    }

}

@Composable
fun Field(
    modifier: Modifier,
    type: Char,
    userName: String?,
    userEmail: String?,
    createdQuiz: Int?,
    passQuiz: Int?,
    delay: Double,
    context: Context
) {

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val text: String? = when (type) {

        'u' -> userName.toString()

        'e' -> userEmail.toString()

        'c' -> getStringByName(context, "created_quizzes")

        else -> getStringByName(context, "passed_quizzes")
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            initialScale = 0.3f,
            animationSpec = tween(durationMillis = (800 * delay).toInt())
        ) + fadeIn(animationSpec = tween(durationMillis = (800 * delay).toInt()))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF212325))
                .padding(vertical = 6.dp)
        ) {

            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                text?.let {
                    Text(
                        text = it,
                        color = Color(0xFFB18F4F),
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily,
                        fontSize = if (type == 'e') 13.sp else 18.sp,
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp),
                        textAlign = TextAlign.Center
                    )
                }

                if (type == 'c') {
                    Text(
                        text = createdQuiz.toString(),
                        color = Color(0xFFB18F4F),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        fontFamily = poppinsFamily,
                    )
                } else if (type == 'r') {
                    Text(
                        text = passQuiz.toString(),
                        color = Color(0xFFB18F4F),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        fontFamily = poppinsFamily
                    )
                }
            }
        }
    }
}