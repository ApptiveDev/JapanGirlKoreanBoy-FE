package com.apptive.japkor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.apptive.japkor.ui.main.MainRouteScreen
import com.apptive.japkor.ui.language.LanguageScreen
import com.apptive.japkor.ui.login.LoginScreen
import com.apptive.japkor.ui.requiredinfo.RequiredInfoCompleteScreen
import com.apptive.japkor.ui.requiredinfo.RequiredInfoScreen
import com.apptive.japkor.ui.signup.SignUpScreen
import com.apptive.japkor.ui.status.PendingApprovalRouteScreen
import com.apptive.japkor.ui.status.PendingConnectingRouteScreen

sealed class Screen(val route: String) {
    object Router : Screen("router")

    object Login : Screen("login")
    object Language : Screen("language")
    object SignUp : Screen("signup")

    object Home : Screen("home")

    object RequiredInfo : Screen("requiredinfo")
    object RequiredInfoComplete : Screen("requiredinfo_complete")

    // placeholder 기능
    object PendingApproval : Screen("pending_approval")
    object PendingConnecting : Screen("pending_connecting")
    object Connected : Screen("connected")
    object Blacklisted : Screen("blacklisted")
}

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Router.route
    ) {
        composable(Screen.Router.route) {
            RouterScreen(navController)
        }

        composable(Screen.Language.route) { LanguageScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.SignUp.route) { SignUpScreen(navController) }

        composable(Screen.RequiredInfo.route) { RequiredInfoScreen(navController) }
        composable(Screen.RequiredInfoComplete.route) { RequiredInfoCompleteScreen(navController) }

        composable(Screen.PendingApproval.route) { PendingApprovalRouteScreen() }
        composable(Screen.PendingConnecting.route) { PendingConnectingRouteScreen() }

        //composable(Screen.Connected.route) { ConnectedPlaceholderScreen(navController) }
        //composable(Screen.Blacklisted.route) { BlacklistedPlaceholderScreen(navController) }
    }
}
