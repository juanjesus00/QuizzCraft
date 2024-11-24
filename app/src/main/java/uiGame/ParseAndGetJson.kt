package uiGame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.gson.Gson

@Composable
fun ParseAndGetJson(jsonString: String): QuestionData {
    val gson = Gson()

    val questionData = remember {
        gson.fromJson(jsonString, QuestionData::class.java)
    }
    return questionData
}
