package quizcraft

import android.net.Uri
import android.widget.Space
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import routes.NavigationActions
import uiPrincipal.poppinsFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun uiQuizCraft(navigationActions: NavigationActions, scrollState: ScrollState) {

    var text by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .verticalScroll(scrollState)
            .offset(x = 0.dp, y = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        
    ) {
        InsertTexField(text = text, inputLabel = "Título del cuestionario")
        FileUploader()
        InsertTexField(text = text, inputLabel = "#Tags")
        InsertTexField(text = text, inputLabel = "Descripción")
    }

}
@Composable
fun InsertTexField(text: String, inputLabel: String){
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
                .clip(RoundedCornerShape(20.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color(0x80C49450),
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
fun FileUploader() {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Activity launcher for file selection
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { launcher.launch("image/*") }) { // Set "image/*" to "application/*" for documents
            Text("Select Image");
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                )
            }
        }

    }
    Spacer(modifier = Modifier.height(50.dp))
}