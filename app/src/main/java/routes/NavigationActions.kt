package routes

import android.os.Bundle
import androidx.navigation.NavController

class NavigationActions (private val navController: NavController){

    fun navigateToHome() {
        navController.navigate(Routes.HOME){
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToLogin() {
        navController.navigate(Routes.LOGIN)
    }

    fun navigateToRegister() {
        navController.navigate(Routes.SING_IN)
    }
    fun navigateToQuizCraft() {
        navController.navigate(Routes.CRAFT)
    }

    fun navigateToUserInfo() {
        navController.navigate(Routes.USERINFO)
    }

    fun navigateToEditUser() {
        navController.navigate(Routes.EDITUSER)
    }

    fun navigateToEditQuiz(quizId: String) {
        val route = Routes.EDITQUIZ.replace("{quizId}", quizId)
        navController.navigate(route)
    }

    fun navigateToInfoQuiz(quizId: String) {
        val route = Routes.INFOQUIZ.replace("{quizId}", quizId)
        navController.navigate(route)
    }

    fun navigateToGame() {
        navController.navigate(Routes.GAME)
    }

    fun navigateToResult(correctQuestion: Int, wrongQuestion: Int) {
        val route = Routes.RESULT.replace("{correctQuestion}", correctQuestion.toString())
            .replace("{wrongQuestion}", wrongQuestion.toString())
        navController.navigate(route)
    }

}
