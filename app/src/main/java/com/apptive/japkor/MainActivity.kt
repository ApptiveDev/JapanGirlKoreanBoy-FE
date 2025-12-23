package com.apptive.japkor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.apptive.japkor.navigation.AppNavHost
import com.apptive.japkor.ui.theme.JapKorTheme
import com.apptive.japkor.ui.components.CustomToastContainer
import com.apptive.japkor.ui.components.ToastManager
import com.apptive.japkor.ui.components.ToastProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            JapKorTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var signedIn by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val toastManager = remember { ToastManager() }

    ToastProvider(manager = toastManager) {
        Box(modifier = Modifier.fillMaxSize()) {
            AppNavHost(navController = navController, isSignedIn = signedIn)
            CustomToastContainer(manager = toastManager)
        }
    }
}
