package uiGame

data class QuestionData(
    val questions: List<Question>
)


data class Question(
    val question: String,
    val answer: Answers
)


data class Answers(
    val answers: List<Option>
)


data class Option(
    val respuesta: String,
    val correct: Boolean
)