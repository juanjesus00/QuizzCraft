package quizcraft

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import model.Quiz
import java.util.UUID

fun addQuizToFirestore(quiz: Quiz) {
    val db = FirebaseFirestore.getInstance()
    val quizCollection = db.collection("Quizzes")

    quizCollection.add(quiz.toMap()) // Firebase genera un quizId único
        .addOnSuccessListener { documentReference ->
            val generatedQuizId = documentReference.id
            println("Quiz agregado con éxito con ID: ${documentReference.id}")
            quizCollection.document(generatedQuizId)
                .update("quiz_id", generatedQuizId)
                .addOnSuccessListener {
                    println("Quiz agregado con éxito con ID: $generatedQuizId")
                }
                .addOnFailureListener { e ->
                    println("Error al actualizar el quizId: ${e.message}")
                }
        }
        .addOnFailureListener { e ->
            println("Error al agregar el quiz: ${e.message}")
        }
}

fun updateQuizInFirestore(quizId: String, updatedFields: Map<String, Any>) {
    val db = FirebaseFirestore.getInstance()

    db.collection("Quizzes").document(quizId)
        .update(updatedFields)
        .addOnSuccessListener {
            println("Quiz actualizado exitosamente")
        }
        .addOnFailureListener { e ->
            println("Error al actualizar el quiz: ${e.message}")
        }
}

suspend fun deleteQuizFromFirestore(quizId: String): Boolean {
    val db = FirebaseFirestore.getInstance()
    return try {
        db.collection("Quizzes").document(quizId).delete().await()
        val users = db.collection("Usuarios").get().await()
        for (userDoc in users.documents) {
            db.collection("Usuarios").document(userDoc.id)
                .update("lastQuizzes", FieldValue.arrayRemove(quizId))
                .await()
        }
        true
    } catch (e: Exception) {
        println("Error al eliminar el quiz: ${e.message}")
        false
    }
}
fun uploadImageToFirebase(imageUri: Uri, onImageUrlReady: (String) -> Unit) {
    val storageReference = FirebaseStorage.getInstance().reference
    val imageRef =
        storageReference.child("ImageQuizzes/${UUID.randomUUID()}.jpg") // Puedes personalizar el nombre
    val uploadTask = imageRef.putFile(imageUri)

    uploadTask.addOnSuccessListener {
        // Una vez que la imagen se haya subido, obtenemos la URL de descarga
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            // Enviar la URL de la imagen al callback
            onImageUrlReady(uri.toString())
        }
    }.addOnFailureListener { exception ->
        // Manejo de error en caso de que la subida falle
        println("Error al subir la imagen: ${exception.message}")
    }
}

fun parseTags(tags: String): List<String> {
    return tags.split(",")  // Dividir la cadena por comas
        .map { it.trim() }   // Eliminar espacios en blanco al principio y final
        .filter { it.startsWith("#") }  // Filtrar los que comienzan con #
}

fun getQuizzesByUserId(
    userId: String,
    onResult: (List<Quiz>) -> Unit,
    onError: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val quizCollection = db.collection("Quizzes")

    quizCollection
        .whereEqualTo("userId", userId) // Filtra por userId
        .get()
        .addOnSuccessListener { querySnapshot ->
            val quizzes = querySnapshot.documents.mapNotNull { document ->
                // Aquí convertimos el documento a un objeto Map y luego lo pasamos a la clase Quiz
                val quizData = document.data // Obtiene un mapa con todos los campos del documento
                quizData?.let {
                    // Crear un nuevo objeto Quiz a partir de un mapa
                    Quiz(
                        name = it["quizName"] as? String ?: "",
                        quizId = it["quiz_id"] as? String ?: "",
                        description = it["quizDescription"] as? String ?: "",
                        quizImageUrl = it["quizImage"] as? String ?: "",
                        tags = it["quizTags"] as? List<String> ?: emptyList(),
                        content = it["quizContent"] as? String ?: "",
                        userId = it["userId"] as? String ?: ""
                    )
                }
            }
            onResult(quizzes) // Devuelve la lista de quizzes
        }
        .addOnFailureListener { exception ->
            onError(exception) // Maneja errores
        }
}

fun getQuizById(
    quizId: String,
    onResult: (Quiz) -> Unit,
    onError: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val quizCollection = db.collection("Quizzes")

    quizCollection.whereEqualTo("quiz_id", quizId)
        .get()
        .addOnSuccessListener { querySnapshot ->
            val document = querySnapshot.documents.firstOrNull() // Tomar solo el primer documento
            if (document != null) {
                val quiz = document.toQuizOrNull()
                if (quiz != null) {
                    onResult(quiz)
                } else {
                    onError(Exception("No se pudo mapear el documento a un Quiz"))
                }
            } else {
                onError(Exception("No se encontró un quiz con el ID proporcionado"))
            }
        }
        .addOnFailureListener(onError)
}

private fun DocumentSnapshot.toQuizOrNull(): Quiz? {
    return try {
        Quiz(
            name = getString("quizName") ?: "",
            quizId = getString("quiz_id") ?: "",
            description = getString("quizDescription") ?: "",
            quizImageUrl = getString("quizImage") ?: "",
            tags = get("quizTags") as? List<String> ?: emptyList(),
            content = getString("quizContent") ?: "",
            userId = getString("userId") ?: ""
        )
    } catch (e: Exception) {
        null // Si algo falla, descartamos el documento
    }
}

fun searchQuizzesByTag(tag: String, onResult: (List<Quiz>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    if (tag.isNotEmpty()) {
        db.collection("Quizzes")
            .whereArrayContains("quizTags", tag)
            .get()
            .addOnSuccessListener { documents ->
                val quizzes = documents.mapNotNull { document ->
                    val quizData = document.data
                    quizData?.let {
                        Quiz(
                            name = it["quizName"] as? String ?: "",
                            quizId = it["quiz_id"] as? String ?: "",
                            description = it["quizDescription"] as? String ?: "",
                            quizImageUrl = it["quizImage"] as? String ?: "",
                            tags = it["quizTags"] as? List<String> ?: emptyList(),
                            content = it["quizContent"] as? String ?: "",
                            userId = it["userId"] as? String ?: ""
                        )
                    }
                }
                onResult(quizzes)
            }
            .addOnFailureListener { exception ->
                println("Error al buscar quizzes: ${exception.message}")
            }
    }
}

fun getQuizzesByLastQuizzesUser(
    lastQuizzes: MutableList<String>,
    onResult: (List<Quiz>) -> Unit,
    onError: (Exception) -> Unit
) {

    val quizzes: MutableList<Quiz> = mutableListOf()
    var remainingQuizzes = lastQuizzes.size

    for (quizId in lastQuizzes) {
        getQuizById(quizId, onResult = { result ->
            quizzes.add(result)
            remainingQuizzes--
            if (remainingQuizzes == 0) {
                onResult(quizzes)
            }
        }, onError = { exception ->
            onError(exception)
        })
    }

    if (lastQuizzes.isEmpty()) {
        onResult(emptyList())
    }
}

fun fetchTopTags(onTagsFetched: (List<Pair<String, Int>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("Quizzes")
        .get()
        .addOnSuccessListener { documents ->
            val tagCounts = mutableMapOf<String, Int>()

            for (document in documents) {
                val tags = document.get("quizTags") as? List<String> ?: emptyList()
                for (tag in tags) {
                    tagCounts[tag] = tagCounts.getOrDefault(tag, 0) + 1
                }
            }

            val topTags = tagCounts.entries
                .sortedByDescending { it.value }
                .take(3)
                .map { it.toPair() }

            onTagsFetched(topTags)
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Error fetching quizzes", exception)
        }
}
