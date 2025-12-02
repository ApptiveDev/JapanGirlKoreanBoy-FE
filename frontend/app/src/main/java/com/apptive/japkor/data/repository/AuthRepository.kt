package com.apptive.japkor.data.repository

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
        return response.isSuccessful
    }

    suspend fun verifyEmailCode(email:String, code: String):Boolean{
        val response = api.verifyEmailCode(VerifyEmailCodeRequest(email,code)).awaitResponse()
        return response.isSuccessful
    }

    suspend fun signUp(request: SignUpDTO): Boolean {
        val response = api.signUp(request).awaitResponse()
        return response.isSuccessful
    }
}