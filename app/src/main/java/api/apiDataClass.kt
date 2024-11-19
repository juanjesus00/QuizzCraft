package api

data class NebiusRequest(
    val messages: List<Message>, // Actualiza este campo según la documentación de la API.
    val model: String = "meta-llama/Meta-Llama-3.1-70B-Instruct" // Especifica el modelo si es requerido.
)

data class NebiusResponse(
    val results: List<Result> // Cambiar estructura de acuerdo a la respuesta de Nebius
)
data class Message(
    val role: String, // Por ejemplo, "user", "assistant", etc.
    val content: String // El texto del mensaje.
)

data class Result(
    val generated_text: String // Campo de respuesta que contiene el texto generado
)

