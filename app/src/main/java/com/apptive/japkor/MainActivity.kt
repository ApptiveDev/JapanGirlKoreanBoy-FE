package com.apptive.japkor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.navigation.AppNavHost
import com.apptive.japkor.navigation.Screen
import com.apptive.japkor.ui.theme.JapKorTheme
import com.apptive.japkor.ui.components.CustomToastContainer
import com.apptive.japkor.ui.components.ToastManager
import com.apptive.japkor.ui.components.ToastProvider
import com.apptive.japkor.ui.localization.AppLanguage
import com.apptive.japkor.ui.localization.LocalAppLanguage
import com.apptive.japkor.push.ensureDefaultNotificationChannel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            Log.d(TAG, "Notification permission granted=$granted")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        ensureDefaultNotificationChannel(this)

        val dataStoreManager = DataStoreManager(this)
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                Log.d(TAG, "FCM token: $token")
                lifecycleScope.launch {
                    dataStoreManager.saveFcmToken(token)
                }
            }

        val startDestinationFromIntent =
            intent.getStringExtra(EXTRA_START_DESTINATION)
        val startDestination =
            startDestinationFromIntent ?: Screen.Language.route

        setContent {
            JapKorTheme {
                MainScreen(
                    onRequestNotificationPermission = ::requestNotificationPermission
                )
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            return
        }

        notificationPermissionLauncher.launch(permission)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@Composable
fun MainScreen(
    onRequestNotificationPermission: () -> Unit
) {
    val navController = rememberNavController()
    val toastManager = remember { ToastManager() }
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }

    val languageCode by dataStore
        .getLanguage()
        .collectAsState(initial = AppLanguage.Korean.code)

    val appLanguage = AppLanguage.fromCode(languageCode)

    LaunchedEffect(Unit) {
        onRequestNotificationPermission()
    }

    ToastProvider(manager = toastManager) {
        CompositionLocalProvider(LocalAppLanguage provides appLanguage) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppNavHost(
                    navController = navController
                )
                CustomToastContainer(manager = toastManager)
            }
        }
    }
}

