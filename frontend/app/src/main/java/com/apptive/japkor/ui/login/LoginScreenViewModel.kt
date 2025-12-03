package com.apptive.japkor.ui.login

import android.app.Application
import android.util.Log
import androidx.datastore.dataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    application: Application,
): AndroidViewModel(application){

    private val repository = AuthRepository()
    private val dataStore = DataStoreManager(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun signIn(email:String,password:String,onResult:(Boolean)->Unit){
        viewModelScope.launch {
            _isLoading.value = true

            Log.d("LoginVM", "signIn 호출: email=$email, password=$password")

            val result = repository.signIn(email, password)
            if (result != null) {
                Log.d("LoginVM", "로그인 성공! token=${result.token}")

                dataStore.saveUserInfo(
                    memberId = result.memberId,
                    name = result.name,
                    token = result.token,
                    status = result.status.name // enum → string 저장
                )

                val info = dataStore.getUserInfo().first()
                Log.d("LoginVM", "저장된 UserInfo: $info")
                _isLoading.value = false
                onResult(true)


            } else {
                Log.e("LoginVM", "로그인 실패")
                onResult(false)
            }
        }
    }
}