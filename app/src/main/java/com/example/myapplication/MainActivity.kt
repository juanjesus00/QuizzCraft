package com.example.myapplication

import SplashVideo.SplashViewModel
import SplashVideo.VideoSplashScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import carga.LoadingScreen
import kotlinx.coroutines.delay
import routes.NavigationActions
import routes.Routes
import uiLogin.LoginScreen
import uiLogin.loginbacked
import uiRegister.RegisterScreen
import uiPrincipal.MyComposeApp
import uiResult.ResultScreen

class MainActivity : ComponentActivity() {
    private lateinit var loginBackend: loginbacked
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navigationActions = NavigationActions(navController)
            val splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]

            NavHost(navController = navController, startDestination = Routes.SPLASH){
                composable(Routes.SPLASH) {
                    VideoSplashScreen (viewModel = splashViewModel){

                        navController.navigate(Routes.HOME){
                            launchSingleTop = true
                            popUpTo(Routes.SPLASH){inclusive = true}
                        }
                    }
                }
                composable(Routes.HOME){ MyComposeApp(navigationActions, navController) }
                composable(Routes.LOGIN) { LoginScreen(navigationActions) }
                composable(Routes.SING_IN) { RegisterScreen(navigationActions) }
                composable(Routes.CRAFT) { MyComposeApp(navigationActions, navController) }
                composable(Routes.USERINFO) { MyComposeApp(navigationActions, navController) }
                composable(Routes.EDITUSER) { MyComposeApp(navigationActions, navController) }
                composable(Routes.EDITQUIZ) { MyComposeApp(navigationActions, navController) }
                composable(Routes.INFOQUIZ) { MyComposeApp(navigationActions, navController) }
                composable(Routes.GAME) { MyComposeApp(navigationActions, navController) }
                composable(Routes.RESULT) { ResultScreen(navigationActions, navController) }
                composable(Routes.CARGA){ LoadingScreen() }
                composable(Routes.ABOUT) { MyComposeApp(navigationActions, navController) }
            }

        }

    }
}
