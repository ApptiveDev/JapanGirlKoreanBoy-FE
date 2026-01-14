package com.apptive.japkor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.apptive.japkor.data.api.ServiceFactory
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.local.TokenProvider
import com.apptive.japkor.data.model.UserStatus
import com.apptive.japkor.navigation.Screen
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.net.URLDecoder
import org.json.JSONObject

const val EXTRA_START_DESTINATION = "EXTRA_START_DESTINATION"
private const val TAG = "LoginCallback"

class LoginCallbackActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent.data ?: run {
            toast("딥링크 URI가 없습니다.")
            finish()
            return
        }

        val rawParams = uri.queryParameterNames
            .associateWith { key -> uri.getQueryParameter(key).orEmpty() }
        val decodedDataJson = uri.getQueryParameter("data")?.let {
            URLDecoder.decode(it, "UTF-8")
        }
        val dataObject = decodedDataJson?.let { json ->
            runCatching { JSONObject(json) }.getOrNull()
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "OAuth callback rawUri=$uri")
            Log.d(TAG, "OAuth callback rawParams=$rawParams")
            Log.d(TAG, "OAuth callback decodedData=$decodedDataJson")
        }

        val success = uri.getQueryParameter("success")?.toBoolean() ?: false
        if (!success) {
            toast("로그인 실패")
            finish()
            return
        }

        val needsProfileCompletion = uri.getQueryParameter("needsProfileCompletion")?.toBoolean()
        val statusParam = uri.getQueryParameter("status")?.takeIf { it.isNotBlank() }
        val statusFromData = dataObject?.optString("status")?.takeIf { it.isNotBlank() }
        val userStatus = (statusParam ?: statusFromData)?.let { status ->
            runCatching { UserStatus.valueOf(status) }.getOrNull()
        }

        // 서버가 토큰을 query로 주는 경우 인코딩 이슈 대비 (안전하게 decode)
        val accessToken = uri.getQueryParameter("accessToken")?.let {
            URLDecoder.decode(it, "UTF-8")
        } ?: dataObject?.optString("accessToken")?.takeIf { it.isNotBlank() }

        val memberIdParam = uri.getQueryParameter("memberId")
        val memberIdValue = memberIdParam?.toIntOrNull()
            ?: dataObject?.optInt("memberId", -1)?.takeIf { it > 0 }
        val nameValue = dataObject?.optString("name")?.takeIf { it.isNotBlank() }.orEmpty()
        val dataJson = decodedDataJson

        // ✅ 신규/기존 상관없이 success면 토큰 저장이 최우선
        if (accessToken.isNullOrBlank()) {
            toast("액세스 토큰이 없습니다.")
            finish()
            return
        }
        TokenProvider.setToken(accessToken)

        // (선택) 디버깅용: 필요하면 잠깐 켜두기
        // toast("token saved: ${accessToken.take(10)}...")

        val startRoute = when (userStatus) {
            UserStatus.INCOMPLETE_PROFILE -> Screen.RequiredInfo.route
            UserStatus.CONNECTING -> Screen.Home.route
            UserStatus.PENDING_APPROVAL,
            UserStatus.APPROVED,
            UserStatus.CONNECTED,
            UserStatus.BLACKLISTED -> Screen.RequiredInfoComplete.route
            null -> when (needsProfileCompletion) {
                true -> Screen.RequiredInfo.route
                false -> Screen.RequiredInfoComplete.route
                null -> Screen.RequiredInfo.route
            }
        }

        val statusValueToSave = userStatus?.name
            ?: if (needsProfileCompletion == true) UserStatus.INCOMPLETE_PROFILE.name else ""

        val i = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_START_DESTINATION, startRoute)

            // 필요하면 전달 (안 써도 됨)
            putExtra("memberId", memberIdParam)
            putExtra("dataJson", dataJson)
        }

        val dataStore = DataStoreManager(this)
        lifecycleScope.launch {
            dataStore.saveUserInfo(
                memberId = memberIdValue ?: -1,
                name = nameValue,
                token = accessToken,
                status = statusValueToSave
            )
            startActivity(i)
            refreshMemberInfo(dataStore)
            finish()
        }
    }

    private suspend fun refreshMemberInfo(dataStore: DataStoreManager) {
        runCatching {
            ServiceFactory.memberService.getUserInfo().awaitResponse()
        }.onSuccess { response ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    dataStore.saveMemberInfo(body)
                } else {
                    Log.w(TAG, "member info response is empty")
                }
            } else {
                Log.w(TAG, "member info fetch failed: code=${response.code()}")
            }
        }.onFailure { e ->
            Log.w(TAG, "member info fetch failed", e)
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
