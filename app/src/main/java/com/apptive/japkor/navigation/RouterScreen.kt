package com.apptive.japkor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.local.TokenProvider
import com.apptive.japkor.data.model.UserStatus
import kotlinx.coroutines.flow.first

@Composable
fun RouterScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val dataStore = remember {
        DataStoreManager(context.applicationContext)
    }

    LaunchedEffect(Unit) {
        val token = dataStore.getUserToken().first()

        // 1️⃣ 토큰 없으면 → Language
        if (token.isBlank()) {
            navController.navigate(Screen.Language.route) {
                popUpTo(Screen.Router.route) { inclusive = true }
            }
            return@LaunchedEffect
        }

        // 2️⃣ 토큰 있으면 메모리에 세팅
        TokenProvider.setToken(token)

        // 3️⃣ status 읽기
        val statusStr = dataStore.getUserStatus().first()
        val status = runCatching {
            UserStatus.valueOf(statusStr)
        }.getOrNull()

        // 4️⃣ status → 목적지 결정
        val targetRoute = when (status) {
            UserStatus.INCOMPLETE_PROFILE -> Screen.RequiredInfo.route
            UserStatus.PENDING_APPROVAL -> Screen.PendingApproval.route
            UserStatus.APPROVED -> Screen.PendingConnecting.route
            UserStatus.CONNECTING,
            UserStatus.CONNECTED -> Screen.Connected.route
            UserStatus.BLACKLISTED -> Screen.Blacklisted.route
            null -> Screen.Login.route
        }

        // 5️⃣ Router 제거 + 이동
        navController.navigate(targetRoute) {
            popUpTo(Screen.Router.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    // 👀 사용자에게 보이는 UI (간단한 로딩/스플래시)
    //SplashLoadingScreen()
}
