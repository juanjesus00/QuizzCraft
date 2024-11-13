package uiGame

import android.graphics.Paint.Align
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun GameScreen(navigationActions: NavigationActions, scrollState: ScrollState, option: Char) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .padding(32.dp)
            .verticalScroll(scrollState)

    ) {
        Game(option, navigationActions)
    }

}

@Composable
fun Game(option: Char, navigationActions: NavigationActions) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 128.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black, shape = RoundedCornerShape(20))
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            if (option == 't') {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    text = "Pregunta 1",
                    color = Color.White,
                    fontSize = 36.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

            } else {
                Image(
                    painter = painterResource(id = R.drawable.huppty),
                    contentDescription = "Foto pregunta",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.padding(24.dp))
        Answers(4)
        BackOrContinue(navigationActions)
    }
}

@Composable
fun BackOrContinue(navigationActions: NavigationActions) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(256.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrowleft),
            contentDescription = "Foto pregunta",
            modifier = Modifier.size(width = 48.dp, height = 48.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.arrowright),
            contentDescription = "Foto pregunta",
            modifier = Modifier.size(width = 48.dp, height = 48.dp).clickable { navigationActions.navigateToResult() }
        )
    }
}

@Composable
fun Answers(count: Int) {
    var color: Long
    for (i in 1..count) {
        color = when (i) {
            1 -> {
                0xFF0303FF
            }

            2 -> {
                0xFFFF0004
            }

            3 -> {
                0xFF36D22E
            }

            else -> {
                0xFFEB9D0D
            }
        }
        Button(
            shape = RoundedCornerShape(20),
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(color))
        ) {
            Text(
                "Pregunta",
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 24.sp,
                color = Color(0xFFFFFFFF)
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}
