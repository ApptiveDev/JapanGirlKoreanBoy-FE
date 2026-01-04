package com.apptive.japkor.ui.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.apptive.japkor.R
import com.apptive.japkor.data.local.DataStoreManager

@Composable
fun PendingApprovalRouteScreen() {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context.applicationContext) }

    val name by dataStore.getUserName().collectAsState(initial = "")

    PendingApprovalScreen(
        name = name.ifBlank { "회원" },
        logoRes = R.drawable.ampersand_bg
    )
}
