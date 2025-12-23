package com.apptive.japkor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.apptive.japkor.ui.language.LanguageScreen
import com.apptive.japkor.ui.login.LoginScreen
import com.apptive.japkor.ui.requiredinfo.RequiredInfoCompleteScreen
import com.apptive.japkor.ui.requiredinfo.RequiredInfoScreen
import com.apptive.japkor.ui.signup.SignUpScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")

    object Language : Screen("language")

    object RequiredInfo : Screen("requiredinfo")

    object RequiredInfoComplete : Screen("requiredinfo_complete")

    object SignUp : Screen("signup")
}

@Composable
fun AppNavHost(navController: NavHostController, isSignedIn: Boolean) {
    NavHost(
        navController = navController,
        startDestination = Screen.Language.route
    ) {
        composable(Screen.Language.route) {
            LanguageScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.RequiredInfo.route) {
            RequiredInfoScreen(navController)
        }
        composable(Screen.RequiredInfoComplete.route) {
            RequiredInfoCompleteScreen(navController)
        }
        composable(
            route = Screen.SignUp.route,
            content = {
                SignUpScreen(navController)
            }
        )
    }
}
