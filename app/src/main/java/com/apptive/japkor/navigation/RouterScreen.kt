package com.apptive.japkor.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.apptive.japkor.data.api.ServiceFactory
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
        // 토큰 없으면 -> Language
        if (token.isBlank()) {
            navController.navigate(Screen.Language.route) {
                popUpTo(Screen.Router.route) { inclusive = true }
                launchSingleTop = true
            }
            return@LaunchedEffect
        }

        // 토큰 있으면 메모리에 세팅
        TokenProvider.setToken(token)

        // 서버에서 현재 status 확인
        val status = runCatching {
            val res = ServiceFactory.matchingService.getMyStatus()

            dataStore.saveUserName(res.name)
            dataStore.saveUserStatus(res.status.name)
            res.status
        }.getOrElse { e ->
            // 서버 호출 실패 시 fallback: DataStore 값
            Log.e("ROUTER_DEBUG", "server status fetch failed, fallback datastore", e)
            val statusStr = dataStore.getUserStatus().first()
            runCatching { UserStatus.valueOf(statusStr) }.getOrNull()
                ?: UserStatus.PENDING_APPROVAL // fallback 기본값(원하면 Login 등으로)
        }

        // status에 따른 화면 이동
        val targetRoute = when (status) {
            UserStatus.INCOMPLETE_PROFILE -> Screen.RequiredInfo.route
            UserStatus.PENDING_APPROVAL -> Screen.PendingApproval.route

            UserStatus.APPROVED -> Screen.PendingConnecting.route

            UserStatus.CONNECTING,
            UserStatus.CONNECTED -> Screen.Connected.route
            UserStatus.BLACKLISTED -> Screen.Blacklisted.route
        }

        Log.d("ROUTER_DEBUG", "status=$status -> target=$targetRoute")

        navController.navigate(targetRoute) {
            popUpTo(Screen.Router.route) { inclusive = true }
            launchSingleTop = true
        }
    }
}
