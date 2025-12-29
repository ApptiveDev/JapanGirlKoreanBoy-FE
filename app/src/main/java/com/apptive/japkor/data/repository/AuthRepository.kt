package com.apptive.japkor.data.repository

import android.app.Service
import android.util.Log
import com.apptive.japkor.data.api.ApiClient
import com.apptive.japkor.data.api.AuthApiService
import com.apptive.japkor.data.api.ServiceFactory
import com.apptive.japkor.data.model.SendEmailCodeRequest
import com.apptive.japkor.data.model.SignInDTO
import com.apptive.japkor.data.model.SignInResponse
import com.apptive.japkor.data.model.SignUpDTO
import com.apptive.japkor.data.model.VerifyEmailCodeRequest
import retrofit2.awaitResponse

class AuthRepository(
    private val api: AuthApiService = ServiceFactory.authApiService
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

    suspend fun signIn(email: String, password: String, fcmToken: String) : SignInResponse?{
        val response = api.signIn(SignInDTO(email, password, fcmToken)).awaitResponse()
        Log.d("AuthRepository", "signIn success=${response.isSuccessful} code=${response.code()}")

        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e("AuthRepository", "signIn failed: ${response.errorBody()?.string().orEmpty()}")
            null
        }
    }
}
