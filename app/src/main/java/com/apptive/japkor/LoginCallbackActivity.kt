package com.apptive.japkor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.apptive.japkor.data.local.TokenProvider
import com.apptive.japkor.navigation.Screen
import java.net.URLDecoder

const val EXTRA_START_DESTINATION = "EXTRA_START_DESTINATION"

class LoginCallbackActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent.data ?: run {
            toast("딥링크 URI가 없습니다.")
            finish()
            return
        }

        val success = uri.getQueryParameter("success")?.toBoolean() ?: false
        if (!success) {
            toast("로그인 실패")
            finish()
            return
        }

        val needsProfileCompletion =
            uri.getQueryParameter("needsProfileCompletion")?.toBoolean() ?: false

        // 서버가 토큰을 query로 주는 경우 인코딩 이슈 대비 (안전하게 decode)
        val accessToken = uri.getQueryParameter("accessToken")?.let {
            URLDecoder.decode(it, "UTF-8")
        }

        val memberId = uri.getQueryParameter("memberId")
        val dataJson = uri.getQueryParameter("data")?.let {
            URLDecoder.decode(it, "UTF-8")
        }

        // ✅ 신규/기존 상관없이 success면 토큰 저장이 최우선
        if (accessToken.isNullOrBlank()) {
            toast("액세스 토큰이 없습니다.")
            finish()
            return
        }
        TokenProvider.setToken(accessToken)

        // (선택) 디버깅용: 필요하면 잠깐 켜두기
        // toast("token saved: ${accessToken.take(10)}...")

        val startRoute = if (needsProfileCompletion) {
            Screen.RequiredInfo.route
        } else {
            Screen.RequiredInfoComplete.route
        }

        val i = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_START_DESTINATION, startRoute)

            // 필요하면 전달 (안 써도 됨)
            putExtra("memberId", memberId)
            putExtra("dataJson", dataJson)
        }

        startActivity(i)
        finish()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
