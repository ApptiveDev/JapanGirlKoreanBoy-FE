package com.apptive.japkor.data.repository

import android.util.Log
import com.apptive.japkor.data.api.ApiClient
import com.apptive.japkor.data.api.AuthApiService
import com.apptive.japkor.data.model.SendEmailCodeRequest
import com.apptive.japkor.data.model.SignUpDTO
import com.apptive.japkor.data.model.VerifyEmailCodeRequest
import retrofit2.awaitResponse

class AuthRepository(
    private val api: AuthApiService = ApiClient.apiService
){
    suspend fun sendEmailCode(email: String) : Boolean{
        val response = api.sendEmailCode(SendEmailCodeRequest(email)).awaitResponse()
        Log.d("AuthRepository", "sendEmailCode email=$email success=${response.isSuccessful} code=${response.code()}")
        if (!response.isSuccessful) {
            Log.e("AuthRepository", "sendEmailCode failed: ${response.errorBody()?.string().orEmpty()}")
        }
        return response.isSuccessful
    }

    suspend fun verifyEmailCode(email:String, code: String):Boolean{
        val response = api.verifyEmailCode(VerifyEmailCodeRequest(email,code)).awaitResponse()
        Log.d("AuthRepository", "verifyEmailCode success=${response.isSuccessful} code=${response.code()}")
        if (!response.isSuccessful) {
            Log.e("AuthRepository", "verifyEmailCode failed: ${response.errorBody()?.string().orEmpty()}")
        }
        return response.isSuccessful
    }

    suspend fun signUp(request: SignUpDTO): Boolean {
        val response = api.signUp(request).awaitResponse()
        Log.d("AuthRepository", "signUp success=${response.isSuccessful} code=${response.code()}")
        if (!response.isSuccessful) {
            Log.e("AuthRepository", "signUp failed: ${response.errorBody()?.string().orEmpty()}")
        }
        return response.isSuccessful
    }
}
