package com.apptive.japkor.data.local

object TokenProvider {
    @Volatile
    private var token: String? = null

    fun setToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String? = token
}
