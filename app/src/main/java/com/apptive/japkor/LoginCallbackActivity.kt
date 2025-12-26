package com.apptive.japkor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.apptive.japkor.navigation.Screen
import java.net.URLDecoder

const val EXTRA_START_DESTINATION = "EXTRA_START_DESTINATION"
class LoginCallbackActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent.data
        if (uri == null) {
            toast("딥링크 URI가 없습니다.")
            finish()
            return
        }

        val success = uri.getQueryParameter("success")?.toBoolean() ?: false
        val needsProfileCompletion =
            uri.getQueryParameter("needsProfileCompletion")?.toBoolean() ?: false
        val accessToken = uri.getQueryParameter("accessToken") // 신규 회원일 경우 없을 수도 있다
        val memberId = uri.getQueryParameter("memberId")       // 문자열로 일단 받기
        val dataJson = uri.getQueryParameter("data")
            ?.let { URLDecoder.decode(it, "UTF-8") }

        if (!success) {
            toast("로그인 실패")
            finish()
            return
        }

        // 신규 회원 → 추가 정보 입력 필요
        if (needsProfileCompletion) {
            val i = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(EXTRA_START_DESTINATION, Screen.RequiredInfo.route)
            }
            startActivity(i)
            finish()
            return
        }

//        // 기존 회원 → 바로 메인 화면으로
//        if (accessToken.isNullOrBlank()) {
//            toast("액세스 토큰이 없습니다.")
//            finish()
//            return
//        }
//
//        val i = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        startActivity(i)
//        finish()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
