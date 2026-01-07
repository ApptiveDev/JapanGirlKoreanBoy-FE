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
fun PendingApprovalRouteScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context.applicationContext) }
    val name by dataStore.getUserName().collectAsState(initial = "")

    StatusAdvanceEffect(
        navController = navController,
        nextRoute = Screen.PendingConnecting.route,
        popUpFromRoute = Screen.PendingApproval.route,
        shouldAdvance = { status ->
            status == UserStatus.APPROVED ||
                    status == UserStatus.CONNECTING ||
                    status == UserStatus.CONNECTED
        },
        pollMs = 5000L,
        debugTag = "ADV_APPROVAL"
    )

    PendingApprovalScreen(
        name = name.ifBlank { "회원" },
        logoRes = R.drawable.ampersand_bg
    )
}
