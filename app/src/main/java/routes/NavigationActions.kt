package routes

import androidx.navigation.NavController

class NavigationActions (private val navController: NavController){

    fun navigateToHome() {
        navController.navigate(Routes.HOME)
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

}
