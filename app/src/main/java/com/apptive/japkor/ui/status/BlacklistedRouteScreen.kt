package com.apptive.japkor.ui.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.apptive.japkor.R
import com.apptive.japkor.data.local.DataStoreManager

@Composable
fun BlacklistedRouteScreen() {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context.applicationContext) }
    val name by dataStore.getUserName().collectAsState(initial = "")

    StatusWaitingTemplate(
        name = name.ifBlank { "회원" },
        title = "서비스 이용이 제한되었습니다",
        subtitle = "문의가 필요하시면 고객센터로 연락해주세요",
        logoRes = R.drawable.ampersand_bg
    )
}
