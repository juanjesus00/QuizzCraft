package quizcraft

import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.NebiusViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import model.Quiz
import routes.NavigationActions
import uiPrincipal.poppinsFamily


private var auth: FirebaseAuth = Firebase.auth
var typePrompt = mutableStateOf("")
@Composable
fun uiQuizCraft(
    navigationActions: NavigationActions,
    scrollState: ScrollState,
    viewModelApi: NebiusViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    var name by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var quizImageUrl by remember { mutableStateOf("") }
    var pdfText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0D4C8))
            .verticalScroll(scrollState)
            .padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        InsertTexField(text = name, inputLabel = "Título del cuestionario", size = 56) {name = it}
        FileUploader3(
            image = R.drawable.galery_icon,
            size = 150,
            typeFile = "image/*",
            onImageUrlReady = { url -> quizImageUrl = url })
        FileUploader2(
            image = R.drawable.multiple_choice,
            size = 100,
            typeFile = "application/*",
            text = "Generar con documento"
        ) {pdfText = it}
        FileUploader2(
            image = R.drawable.document,
            size = 100,
            typeFile = "application/*",
            text = "Generar con texto"
        ){pdfText = it}
        InsertTexField(text = tags, inputLabel = "#Tags", size = 56) {tags = it}
        InsertTexField(text = descripcion, inputLabel = "Descripción", size = 150) {descripcion = it}
        AcceptButton(
            navigationActions = navigationActions,
            viewModelApi,
            name,
            tags,
            descripcion,
            quizImageUrl,
            pdfText
        )
    }

}

@Composable
fun FileUploader2(image: Int, size: Int, typeFile: String, text: String, function: (String) -> Unit) {
    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var pdfContent by remember { mutableStateOf("")}
    // Activity launcher for file selection
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedPdfUri = it
                if(text == "Generar con documento"){ //formato pregunta respuestas
                    typePrompt.value = "puedes procesar este texto con formato preguntas y respuestas, y adaptar las preguntas a un json con esta estructura ${uiPrincipal.jsonString}, el texto a procesar: "
                } else if (text == "Generar con texto"){ //formato texto plano
                    typePrompt.value = "puedes procesar todo el texto y hacer 20 preguntas en un formato json, con esta estructura ${uiPrincipal.jsonString}, texto a procesar: "
                }
                try {
                    // Abrir el archivo PDF y procesarlo usando iTextPDF
                    val inputStream = context.contentResolver.openInputStream(it)
                    if (inputStream != null) {
                        val pdfReader = com.itextpdf.text.pdf.PdfReader(inputStream)
                        val totalPages = pdfReader.numberOfPages
                        val stringBuilder = StringBuilder()

                        for (i in 1..totalPages) {
                            stringBuilder.append(com.itextpdf.text.pdf.parser.PdfTextExtractor.getTextFromPage(pdfReader, i))
                        }
                        pdfContent = stringBuilder.toString()
                        function(stringBuilder.toString())
                        pdfReader.close()
                        inputStream.close()
                        Log.d("PDF_TEXT", stringBuilder.toString()) // Imprimir el texto extraído en los logs
                    }
                } catch (e: Exception) {
                    Log.e("PDF_ERROR", "Error al procesar el PDF: ${e.message}")
                }
            }
        }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                if(pdfContent.isNullOrEmpty()){
                    launcher.launch(typeFile)
                }else if(pdfContent.isNotEmpty()){
                    // tengo que hacer un toast, que en el launcher que no se ha seleccionado, tengo que investigar con la imagen como condicional
                }


                      }, // Abrir selector de archivos
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .width(282.dp)
                .height(56.dp)
                .border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEADEE6),
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = text, fontFamily = poppinsFamily, color = Color(0xFFC49450))
                Box {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "Imagen de agregación",
                        modifier = Modifier.alpha(if (pdfContent.isNullOrEmpty()) 1f else 0f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = "Imagen de agregación",
                        modifier = Modifier.alpha(if (pdfContent.isNotEmpty()) 1f else 0f)
                    )

                }
            }
        }
    }
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
fun InsertTexField(text: String, inputLabel: String, size: Int, function: (String) -> Unit) {

    Box(
        modifier = Modifier
            .border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)) // Borde personalizado
    ) {
        TextField(
            value = text,
            onValueChange = function,
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
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { launcher.launch(typeFile) }, //solo se seleccion una imagen
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(size.dp)
                .border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEADEE6),
            )
        ) {
            Box {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "imagen de agregacion",
                    modifier = Modifier
                        .size(50.dp)
                        .alpha(if (selectedImageUri == null) 1f else 0f)
                )
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

@Composable
fun AcceptButton(
    navigationActions: NavigationActions,
    viewModelApi: NebiusViewModel,
    name: String,
    tags: String,
    description: String,
    imageUrl: String,
    pdfText: String
) {
    val context = LocalContext.current
    var resultText by remember { mutableStateOf("Resultado aquí...") }

    Button(
        shape = RoundedCornerShape(20),
        onClick = {
            val apiKey =
                "eyJhbGciOiJIUzI1NiIsImtpZCI6IlV6SXJWd1h0dnprLVRvdzlLZWstc0M1akptWXBvX1VaVkxUZlpnMDRlOFUiLCJ0eXAiOiJKV1QifQ.eyJzdWIiOiJnb29nbGUtb2F1dGgyfDEwMDU1Njk3MTMzOTIzMTIxMzM4NyIsInNjb3BlIjoib3BlbmlkIG9mZmxpbmVfYWNjZXNzIiwiaXNzIjoiYXBpX2tleV9pc3N1ZXIiLCJhdWQiOlsiaHR0cHM6Ly9uZWJpdXMtaW5mZXJlbmNlLmV1LmF1dGgwLmNvbS9hcGkvdjIvIl0sImV4cCI6MTg4OTYzMTUxOSwidXVpZCI6ImIwYWU0MmM2LWVhN2YtNDI1NS04MWI2LTM0MjgzYjk3MWM5NiIsIm5hbWUiOiJ0ZXN0S2V5IiwiZXhwaXJlc19hdCI6IjIwMjktMTEtMTdUMTc6Mzg6MzkrMDAwMCJ9.YIWppuSz_gfy7jp-zSOoqoRGQgfzO2UVSx7eKuU8AH0" // Usa una clave de API segura
            viewModelApi.generateText(
                apiKey, typePrompt.value + pdfText
            )
            { response ->
                resultText = response
                addQuizToFirestore(
                    Quiz(
                        name,
                        "",
                        description,
                        imageUrl,
                        parseTags(tags),
                        content = resultText,
                        userId = auth.currentUser?.uid ?: ""
                    )
                )

            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF212325))
    ) {
        Text(
            "Aceptar",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            fontSize = 20.sp,
            color = Color(0xFFB18F4F)
        )
    }
    Text(text = resultText)
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
fun FileUploader3(image: Int, size: Int, typeFile: String, onImageUrlReady: (String) -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Activity launcher for file selection
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { launcher.launch(typeFile) }, //solo se seleccion una imagen
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(size.dp)
                .border(2.dp, Color(0xFFC49450), RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEADEE6),
            )
        ) {
            Box {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "imagen de agregacion",
                    modifier = Modifier
                        .size(50.dp)
                        .alpha(if (selectedImageUri == null) 1f else 0f)
                )
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

    // Subir la imagen a Firebase Storage si se seleccionó una
    selectedImageUri?.let { uri ->
        uploadImageToFirebase(uri, onImageUrlReady)

    }

}