package com.apptive.japkor.ui.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.apptive.japkor.R
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.model.UserStatus
import com.apptive.japkor.navigation.Screen
import com.apptive.japkor.ui.common.StatusAdvanceEffect

@Composable
fun PendingConnectingRouteScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context.applicationContext) }
    val name by dataStore.getUserName().collectAsState(initial = "")

    StatusAdvanceEffect(
        navController = navController,
        nextRoute = Screen.Connected.route,
        popUpFromRoute = Screen.PendingConnecting.route,
        shouldAdvance = { status -> status == UserStatus.CONNECTING },
        //pollMs = 3000L,
        debugTag = "ADV_CONNECTING"
    )


    PendingConnectingScreen(
        name = name.ifBlank { "회원" },
        logoRes = R.drawable.ampersand_bg
    )
}
