package uiUserInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun UserInfoScreen(navigationActions: NavigationActions, viewModelUser: userInfoBack = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {
        UserInfo(Modifier.align(Alignment.Center), navigationActions, viewModelUser)
    }
}

@Composable
fun UserInfo(modifier: Modifier, navigationActions: NavigationActions, viewModelUser: userInfoBack) {
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf<String?>(null) }
    //var userEmail by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        viewModelUser.getInfoUser { user ->
            profileImageUrl = user?.get("PerfilImage") as? String
            userName = user?.get("userName") as? String
        }
    }
    Column(modifier = modifier) {
        ImageProfile(Modifier.align(Alignment.CenterHorizontally), navigationActions, profileImageUrl)
        Spacer(modifier = Modifier.padding(12.dp))
        Field(Modifier.align(Alignment.CenterHorizontally), 'u', userName)
        Spacer(modifier = Modifier.padding(12.dp))
        Field(Modifier.align(Alignment.CenterHorizontally), 'e', userName)
        Spacer(modifier = Modifier.padding(12.dp))
        Field(Modifier.align(Alignment.CenterHorizontally), 'c', userName)
        Spacer(modifier = Modifier.padding(12.dp))
        Field(Modifier.align(Alignment.CenterHorizontally), 'r', userName)
    }
}

@Composable
fun ImageProfile(modifier: Modifier, navigationActions: NavigationActions, profileImageUrl: String?) {
    Box(
        modifier = modifier.size(208.dp)
    ) {
        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            Image(
                painter = painterResource(id = R.drawable.foto),
                contentDescription = "ProfileImage",
                contentScale = ContentScale.Fit,
                modifier = modifier
                    .size(208.dp)
                    .clip(RoundedCornerShape(100.dp))
            )
        }else{
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
        Icon(
            painter = painterResource(id = R.drawable.editar),
            contentDescription = "EditProfile",
            tint = Color(0xFFB18F4F),
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.BottomEnd)
                .clickable { navigationActions.navigateToEditUser() }
        )
    }
}

@Composable
fun Field(modifier: Modifier, type: Char, userName: String?) {

    val text: String = when (type) {
        'u' -> ({
            userName
        }).toString()

        'e' -> {
            "Email"
        }

        'c' -> {
            "Cuestionarios realizados"
        }

        else -> {
            "Cuestionarios superados"
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 60.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF212325))
            .padding(vertical = 6.dp)
    ) {

        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                color = Color(0xFFB18F4F),
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp),
                textAlign = TextAlign.Center
            )

            if (type == 'c' || type == 'r') {
                Text(
                    text = "6",
                    color = Color(0xFFB18F4F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    fontFamily = poppinsFamily,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 5.dp)
                )
            }
        }
    }

}