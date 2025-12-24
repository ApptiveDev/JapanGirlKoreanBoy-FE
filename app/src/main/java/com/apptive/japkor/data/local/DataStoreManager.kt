package com.apptive.japkor.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val KEY_MEMBER_ID = intPreferencesKey("member_id")      // Int → intPreferencesKey
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_TOKEN = stringPreferencesKey("token")
        val KEY_STATUS = stringPreferencesKey("status")
    }

    suspend fun saveUserInfo(memberId: Int, name: String, token: String, status: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_MEMBER_ID] = memberId
            prefs[KEY_NAME] = name
            prefs[KEY_TOKEN] = token
            prefs[KEY_STATUS] = status
        }
    }

    fun getUserToken() = context.dataStore.data.map { it[KEY_TOKEN] ?: "" }

    fun getUserStatus() = context.dataStore.data.map { it[KEY_STATUS] ?: "" }

    fun getUserInfo() = context.dataStore.data.map {
        mapOf(
            "memberId" to (it[KEY_MEMBER_ID] ?: -1),
            "name" to (it[KEY_NAME] ?: ""),
            "token" to (it[KEY_TOKEN] ?: ""),
            "status" to (it[KEY_STATUS] ?: "")
        )
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
