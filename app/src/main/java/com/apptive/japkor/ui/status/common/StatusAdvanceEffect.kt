package com.apptive.japkor.ui.common

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.apptive.japkor.data.api.ServiceFactory
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.model.UserStatus
import kotlinx.coroutines.delay
import retrofit2.awaitResponse

/**
 * 단방향 전이(이전 단계로 돌아가지 않음) 앱용:
 * - 화면이 STARTED 될 때 서버 status 재조회
 * - "다음 단계 조건"이 되면 단 한 번 navigate하고 이전 화면은 popUpTo로 제거
 * - 절대 이전 상태로 보내는 navigate는 하지 않는다
 */
@Composable
fun StatusAdvanceEffect(
    navController: NavHostController,
    nextRoute: String,
    popUpFromRoute: String,
    shouldAdvance: (UserStatus) -> Boolean,
    pollMs: Long = 5000L, // 0이면 포커싱 시 1회만, 아니면 n ms마다 폴링
    debugTag: String = "ADVANCE"
) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context.applicationContext) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (true) {
                runCatching {
                    val response = ServiceFactory.memberService.getUserInfo().awaitResponse()
                    if (!response.isSuccessful) {
                        throw IllegalStateException("member info fetch failed: code=${response.code()}")
                    }

                    val body = response.body() ?: throw IllegalStateException("member info is empty")
                    dataStore.saveMemberInfo(body)

                    Log.d(debugTag, "status=${body.status}")

                    if (shouldAdvance(body.status)) {
                        Log.d(debugTag, "advance -> $nextRoute (popUpFrom=$popUpFromRoute)")
                        navController.navigate(nextRoute) {
                            popUpTo(popUpFromRoute) { inclusive = true } // ✅ 이전 단계 제거
                            launchSingleTop = true
                        }
                        return@repeatOnLifecycle
                    }
                }.onFailure { e ->
                    Log.e(debugTag, "status check failed", e)
                }

                if (pollMs <= 0L) break
                delay(pollMs)
            }
        }
    }
}
