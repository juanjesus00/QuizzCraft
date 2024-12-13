package model

data class User(
    val email: String,
    val userId: String,
    val userName: String,
    val profileImageUrl: String,
    val createdQuiz: Int,
    val passQuiz: Int,
    val lastQuizzes: MutableList<String>,
    val password: String
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "userName" to this.userName,
            "PerfilImage" to this.profileImageUrl,
            "email" to this.email,
            "createdQuiz" to this.createdQuiz,
            "passQuiz" to this.passQuiz,
            "lastQuizzes" to this.lastQuizzes,
            "password" to this.password

        )
    }
}