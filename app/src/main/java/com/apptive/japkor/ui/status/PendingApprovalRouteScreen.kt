package com.apptive.japkor.ui.status

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.apptive.japkor.R
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.model.UserStatus
import com.apptive.japkor.navigation.Screen

@Composable
fun PendingApprovalRouteScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context.applicationContext) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val name by dataStore.getUserName().collectAsState(initial = "")

    // ✅ (중요) 일단 remember 유지하되, 로그로 진입 확인
    val vm = remember { PendingApprovalViewModel(dataStore) }
    val state by vm.uiState.collectAsState()

    // ✅ 0) 화면 진입 로그 (이게 안 찍히면 이 Screen이 아예 실행 안 되는 거)
    LaunchedEffect(Unit) {
        Log.d("PENDING_DEBUG", "PendingApprovalRouteScreen entered")
    }

    // ✅ 1) 일단 진입 시 1회는 무조건 호출 (repeatOnLifecycle 타이밍 이슈 제거)
    LaunchedEffect(Unit) {
        Log.d("PENDING_DEBUG", "refreshStatus() first call")
        vm.refreshStatus()
    }

    // ✅ 2) 그 다음부터는 화면이 다시 STARTED 될 때마다 재조회
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            Log.d("PENDING_DEBUG", "repeatOnLifecycle STARTED -> refreshStatus()")
            vm.refreshStatus()
        }
    }

    // ✅ status 변화 로그 + 이동
    LaunchedEffect(state.status) {
        Log.d("PENDING_DEBUG", "status changed: ${state.status}")

        when (state.status) {
            UserStatus.APPROVED,
            UserStatus.CONNECTING -> {
                Log.d("PENDING_DEBUG", "navigate -> pending_connecting")
                navController.navigate(Screen.PendingConnecting.route) {
                    popUpTo(Screen.PendingApproval.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> Unit
        }
    }

    // ✅ 401이면 로그인으로
    LaunchedEffect(state.unauthorized) {
        if (state.unauthorized) {
            Log.d("PENDING_DEBUG", "unauthorized -> navigate login")
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    PendingApprovalScreen(
        name = name.ifBlank { "회원" },
        logoRes = R.drawable.ampersand_bg
    )
}
