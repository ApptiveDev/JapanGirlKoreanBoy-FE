package com.apptive.japkor.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.local.TokenProvider
import com.apptive.japkor.data.api.ServiceFactory
import com.apptive.japkor.data.model.UserStatus
import com.apptive.japkor.data.repository.AuthRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.awaitResponse
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LoginScreenViewModel(
    application: Application,
): AndroidViewModel(application){

    private val repository = AuthRepository()
    private val dataStore = DataStoreManager(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    val rememberedEmail = dataStore.getRememberedEmail()

    init {
        // 앱 재시작 시 저장된 토큰을 메모리에 올려둔다.
        viewModelScope.launch {
            val savedToken = dataStore.getUserToken().first()
            if (savedToken.isNotBlank()) {
                TokenProvider.setToken(savedToken)
                Log.d("LoginVM", "Restored token from DataStore")
            }
        }
    }

    fun signIn(email: String, password: String, onResult: (Boolean, UserStatus?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            Log.d("LoginVM", "signIn 호출: email=$email, password=$password")

            try {
                val fcmToken = getOrFetchFcmToken()
                if (fcmToken.isBlank()) {
                    Log.e("LoginVM", "FCM token missing; aborting signIn")
                    onResult(false, null)
                    return@launch
                }
                val result = repository.signIn(email, password, fcmToken)
                if (result != null) {
                    Log.d("LoginVM", "로그인 성공! token=${result.token}")
                    TokenProvider.setToken(result.token)

                    dataStore.saveUserInfo(
                        memberId = result.memberId,
                        name = result.name,
                        token = result.token,
                        status = result.status.name // enum → string 저장
                    )

                    refreshMemberInfo()

                    val info = dataStore.getUserInfo().first()
                    Log.d("LoginVM", "저장된 UserInfo: $info")
                    onResult(true, result.status)
                } else {
                    Log.e("LoginVM", "로그인 실패: 결과 null")
                    onResult(false, null)
                }
            } catch (e: Exception) {
                Log.e("LoginVM", "로그인 실패: 예외 발생", e)
                onResult(false, null)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun getOrFetchFcmToken(): String {
        val cachedToken = dataStore.getFcmToken().first()
        if (cachedToken.isNotBlank()) return cachedToken

        return try {
            val token = fetchFcmToken()
            if (token.isNotBlank()) {
                dataStore.saveFcmToken(token)
            }
            token
        } catch (e: Exception) {
            Log.w("LoginVM", "FCM token fetch failed", e)
            ""
        }
    }

    private suspend fun fetchFcmToken(): String = suspendCancellableCoroutine { cont ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!cont.isActive) return@addOnCompleteListener
                if (task.isSuccessful) {
                    cont.resume(task.result.orEmpty())
                } else {
                    cont.resumeWithException(
                        task.exception ?: IllegalStateException("FCM token fetch failed")
                    )
                }
            }
    }

    private suspend fun refreshMemberInfo() {
        runCatching {
            ServiceFactory.memberService.getUserInfo().awaitResponse()
        }.onSuccess { response ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    dataStore.saveMemberInfo(body)
                } else {
                    Log.w("LoginVM", "member info response is empty")
                }
            } else {
                Log.w("LoginVM", "member info fetch failed: code=${response.code()}")
            }
        }.onFailure { e ->
            Log.w("LoginVM", "member info fetch failed", e)
        }
    }

    fun updateRememberedEmail(remember: Boolean, email: String) {
        viewModelScope.launch {
            if (remember && email.isNotBlank()) {
                dataStore.saveRememberedEmail(email)
            } else {
                dataStore.clearRememberedEmail()
            }
        }
    }
}
