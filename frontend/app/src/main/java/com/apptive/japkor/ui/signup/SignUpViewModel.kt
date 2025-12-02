package com.apptive.japkor.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.model.SignUpDTO
import com.apptive.japkor.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

sealed class SignUpUiEvent {
    data class ShowToast(val message: String) : SignUpUiEvent()
    object NavigateToLogin : SignUpUiEvent()
}

class SignUpViewModel(
    private val repository : AuthRepository = AuthRepository()
) : ViewModel() {
    private val _emailSent = MutableStateFlow(false)
    val emailSent: StateFlow<Boolean> = _emailSent

    private val _emailVerified = MutableStateFlow(false)
    val emailVerified: StateFlow<Boolean> = _emailVerified

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess: StateFlow<Boolean> = _signUpSuccess

    private val _events = MutableSharedFlow<SignUpUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<SignUpUiEvent> = _events.asSharedFlow()

    private val _hasSentCode = MutableStateFlow(false)
    val hasSentCode: StateFlow<Boolean> = _hasSentCode

    private val _isCountingDown = MutableStateFlow(false)
    val isCountingDown: StateFlow<Boolean> = _isCountingDown

    private val _isResendEnabled = MutableStateFlow(false)
    val isResendEnabled: StateFlow<Boolean> = _isResendEnabled

    private val _codeTimerSeconds = MutableStateFlow(0)
    val codeTimerSeconds: StateFlow<Int> = _codeTimerSeconds

    private var timerJob: Job? = null

    fun sendEmailCode(email: String){
        viewModelScope.launch {
            runCatching {
                repository.sendEmailCode(email)
            }.onSuccess { result ->
                _emailSent.value = result
                Log.d(TAG, "sendEmailCode result=$result email=$email")
                if (result) {
                    _hasSentCode.value = true
                    startResendTimer()
                    _events.emit(SignUpUiEvent.ShowToast("인증코드가 전송되었습니다!"))
                } else {
                    _events.emit(SignUpUiEvent.ShowToast("인증코드 전송에 실패했습니다. 다시 시도해주세요."))
                }
            }.onFailure { throwable ->
                Log.e(TAG, "sendEmailCode exception", throwable)
                _events.emit(SignUpUiEvent.ShowToast("인증코드 전송에 실패했습니다. 다시 시도해주세요."))
            }
        }
    }

    fun verifyEmail(email: String, code: String) {
        viewModelScope.launch {
            runCatching {
                repository.verifyEmailCode(email, code)
            }.onSuccess { result ->
                _emailVerified.value = result
                Log.d(TAG, "verifyEmail result=$result")
                if (result) {
                    _events.emit(SignUpUiEvent.ShowToast("이메일 인증 완료!"))
                } else {
                    _events.emit(SignUpUiEvent.ShowToast("인증 코드가 올바르지 않습니다."))
                }
            }.onFailure { throwable ->
                Log.e(TAG, "verifyEmail exception", throwable)
                _events.emit(SignUpUiEvent.ShowToast("인증에 실패했습니다. 네트워크를 확인해주세요."))
            }
        }
    }

    fun signUp(
        name: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            val request = SignUpDTO(
                name = name,
                email = email,
                password = password
            )
            runCatching {
                repository.signUp(request)
            }.onSuccess { result ->
                _signUpSuccess.value = result
                Log.d(TAG, "signUp result=$result")
                if (result) {
                    _events.emit(SignUpUiEvent.ShowToast("회원가입이 완료되었습니다."))
                    _events.emit(SignUpUiEvent.NavigateToLogin)
                } else {
                    _events.emit(SignUpUiEvent.ShowToast("회원가입에 실패했습니다. 다시 시도해주세요."))
                }
            }.onFailure { throwable ->
                Log.e(TAG, "signUp exception", throwable)
                _events.emit(SignUpUiEvent.ShowToast("회원가입에 실패했습니다. 네트워크를 확인해주세요."))
            }
        }
    }

    private fun startResendTimer() {
        timerJob?.cancel()
        _isCountingDown.value = true
        _isResendEnabled.value = false
        _codeTimerSeconds.value = 300

        timerJob = viewModelScope.launch {
            while (_codeTimerSeconds.value > 0) {
                delay(1000)
                _codeTimerSeconds.value = _codeTimerSeconds.value - 1
            }
            _isCountingDown.value = false
            _isResendEnabled.value = true
        }
    }

    companion object {
        private const val TAG = "SignUpViewModel"
    }
}
