package uiGame

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

@Composable
fun ParseAndGetJson(jsonString: String): QuestionData {
    val gson = Gson()

    val questionData = remember {
        gson.fromJson(jsonString, QuestionData::class.java)
    }
    return questionData
}
