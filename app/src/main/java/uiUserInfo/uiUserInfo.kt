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
import com.example.myapplication.R
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun UserInfoScreen(navigationActions: NavigationActions) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)

    ) {
        UserInfo(Modifier.align(Alignment.Center), navigationActions)
    }
}

@Composable
fun UserInfo(modifier: Modifier, navigationActions: NavigationActions) {

    Column(modifier = modifier) {
        ImageProfile(Modifier.align(Alignment.CenterHorizontally), navigationActions)
        Spacer(modifier = Modifier.padding(12.dp))
        Field(Modifier.align(Alignment.CenterHorizontally), 'u')
        Spacer(modifier = Modifier.padding(12.dp))
        Field(Modifier.align(Alignment.CenterHorizontally), 'e')
        Spacer(modifier = Modifier.padding(12.dp))
        Field(Modifier.align(Alignment.CenterHorizontally), 'c')
        Spacer(modifier = Modifier.padding(12.dp))
        Field(Modifier.align(Alignment.CenterHorizontally), 'r')
    }
}

@Composable
fun ImageProfile(modifier: Modifier, navigationActions: NavigationActions) {
    Box(
        modifier = modifier.size(208.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.foto),
            contentDescription = "ProfileImage",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .size(208.dp)
                .clip(RoundedCornerShape(100.dp))
        )

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
fun Field(modifier: Modifier, type: Char) {

    val text: String = when (type) {
        'u' -> {
            "Nombre de usuario"
        }

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