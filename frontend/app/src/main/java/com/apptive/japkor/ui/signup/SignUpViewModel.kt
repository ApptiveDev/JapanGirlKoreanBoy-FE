package com.apptive.japkor.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.model.SignUpDTO
import com.apptive.japkor.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository : AuthRepository = AuthRepository()
) : ViewModel() {
    private val _emailSent = MutableStateFlow(false)
    val emailSent: StateFlow<Boolean> = _emailSent

    private val _emailVerified = MutableStateFlow(false)
    val emailVerified: StateFlow<Boolean> = _emailVerified

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess: StateFlow<Boolean> = _signUpSuccess

    fun sendEmailCode(email: String){
        viewModelScope.launch{
            val result = repository.sendEmailCode(email)
            _emailSent.value = result
        }
    }

    fun verifyEmail(email: String, code: String) {
        viewModelScope.launch {
            val result = repository.verifyEmailCode(email, code)
            _emailVerified.value = result
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
            val result = repository.signUp(request)
            _signUpSuccess.value = result
        }
    }
}