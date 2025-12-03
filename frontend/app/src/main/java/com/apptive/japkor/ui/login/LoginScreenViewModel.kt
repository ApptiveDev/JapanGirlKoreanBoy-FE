package com.apptive.japkor.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel(){

    fun signIn(email:String,password:String,onResult:(Boolean)->Unit){
        viewModelScope.launch {
            Log.d("LoginVM", "signIn 호출: email=$email, password=$password")

            val result = repository.signIn(email, password)
            if (result != null) {
                Log.d("LoginVM", "로그인 성공! token=${result.token}")
                onResult(true)
            } else {
                Log.e("LoginVM", "로그인 실패")
                onResult(false)
            }
        }
    }
}