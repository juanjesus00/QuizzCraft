package model

import com.google.firebase.firestore.PropertyName

data class Quiz(
    @PropertyName("quizName") val name: String,
    @PropertyName("quiz_id") val quizId: String,
    @PropertyName("quizDescription") val description: String,
    @PropertyName("quizImage") val quizImageUrl: String,
    @PropertyName("quizTags") val tags: List<String>,
    @PropertyName("quizContent") val content: String,
    val userId: String
) {

    constructor() : this("","","","",emptyList(),"","")

    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "quiz_id" to this.quizId,
            "quizName" to this.name,
            "quizImage" to this.quizImageUrl,
            "quizDescription" to this.description,
            "quizTags" to this.tags,
            "quizContent" to this.content,
            "userId" to this.userId
        )
    }


}
