package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("sign-in")
    fun signIn(@Body body: SignInDTO): Call<SignInResponse>
    @POST("sign-up")
    fun signUp(@Body body: SignUpDTO): Call<Void>

    @POST("email-code/send")
    fun sendEmailCode(@Body body: SendEmailCodeRequest): Call<Void>

    @POST("email-code/verify")
    fun verifyEmailCode(@Body body: VerifyEmailCodeRequest): Call<Void>
}
