package quizcraft

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import routes.NavigationActions
import uiPrincipal.poppinsFamily

@Composable
fun uiQuizCraft(navigationActions: NavigationActions, scrollState: ScrollState) {

    var text by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .verticalScroll(scrollState)
            .padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        
    ) {
        InsertTexField(text = text, inputLabel = "Título del cuestionario", size = 56)
        FileUploader(image = R.drawable.camara, size = 150, typeFile = "image/*")
        FileUploader2(image = R.drawable.multiple_choice, size = 100, typeFile = "application/*", text = "Generar con documento")
        FileUploader2(image = R.drawable.document, size = 100, typeFile = "application/*", text = "Generar con texto")
        InsertTexField(text = text, inputLabel = "#Tags", size = 56)
        InsertTexField(text = text, inputLabel = "Descripción", size = 150)
    }

}

@Composable
fun FileUploader2(image: Int, size: Int, typeFile: String, text: String) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Activity launcher for file selection
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { launcher.launch(typeFile) }, //solo se seleccion una imagen
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(282.dp).height(56.dp).border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEADEE6),
            )
        ) {
            Row (
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = text, fontFamily = poppinsFamily, color = Color(0xFFC49450))
                Box{
                    Image(painter = painterResource(id = image), contentDescription = "imagen de agregacion", modifier = Modifier.alpha(if(selectedImageUri == null) 1f else 0f))
                    selectedImageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

        }

    }
    Spacer(modifier = Modifier.height(50.dp))

}

@Composable
fun InsertTexField(text: String, inputLabel: String, size: Int){
    var text by remember { mutableStateOf(text) }
    Box (
        modifier = Modifier
            .border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)) // Borde personalizado
    ){
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(text = inputLabel) },
            modifier = Modifier
                .background(color = Color(0xFFFFFDFD), shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .height(size.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0x80C49450),
                unfocusedIndicatorColor = Color.Transparent,

            )
        )
    }
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
fun FileUploader(image: Int, size: Int, typeFile: String) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Activity launcher for file selection
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { launcher.launch(typeFile) }, //solo se seleccion una imagen
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.size(size.dp).border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEADEE6),
            )
        ) {
            Box{
                Image(painter = painterResource(id = image), contentDescription = "imagen de agregacion", modifier = Modifier.alpha(if(selectedImageUri == null) 1f else 0f))
                selectedImageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

    }
    Spacer(modifier = Modifier.height(50.dp))
}