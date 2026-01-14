package com.apptive.japkor.data.local

import android.content.Context

object TokenProvider {
    private const val PREFS_NAME = "auth"
    private const val KEY_ACCESS_TOKEN = "accessToken"

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun setToken(newToken: String) {
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ACCESS_TOKEN, newToken)
            .apply()
    }

    fun getToken(): String? {
        return appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ACCESS_TOKEN, null)
    }

    fun clearToken() {
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_ACCESS_TOKEN)
            .apply()
    }
}
