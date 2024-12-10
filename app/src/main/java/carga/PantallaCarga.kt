package carga

import android.annotation.SuppressLint

import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide

import com.example.myapplication.R
import languagesBack.getStringByName
import routes.NavigationActions
import uiPrincipal.poppinsFamily


@Composable
fun LoadingScreen() {
    BackHandler (enabled = true){
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            factory = {context ->
                ImageView(context).apply{
                    Glide.with(context)
                        .load(R.drawable.carga)
                        .into(this)
                }
            },
            modifier = Modifier.size(150.dp)
        )
        getStringByName(LocalContext.current, "loading_loadscreen")?.let {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 30.sp,
                color = Color(0xFFB18F4F)

            )
        }
    }
}

