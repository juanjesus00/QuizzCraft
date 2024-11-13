package model

data class User(
    val id: String?,
    val userId: String,
    val userName: String,
    val profileImageUrl: String
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "userName" to this.userName,
            "PerfilImage" to this.profileImageUrl
        )
    }
}