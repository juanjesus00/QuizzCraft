package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import quizcraft.uiQuizCraft
import routes.NavigationActions
import routes.Routes
import uiLogin.LoginScreen
import uiRegister.RegisterScreen
import uiPrincipal.MyComposeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val navigationActions = NavigationActions(navController)
            NavHost(navController = navController, startDestination = Routes.HOME){
                composable(Routes.HOME){ MyComposeApp(navigationActions, navController) }
                composable(Routes.LOGIN) { LoginScreen(navigationActions) }
                composable(Routes.SING_IN) { RegisterScreen(navigationActions) }
                composable(Routes.CRAFT) { MyComposeApp(navigationActions, navController) }
                composable(Routes.USERINFO) { MyComposeApp(navigationActions, navController) }
                composable(Routes.EDITUSER) { MyComposeApp(navigationActions, navController) }
                }

        }

    }
}
