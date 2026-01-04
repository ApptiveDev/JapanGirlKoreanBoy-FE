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
        val KEY_REMEMBERED_EMAIL = stringPreferencesKey("remembered_email")
        val KEY_LANGUAGE = stringPreferencesKey("app_language")
        val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
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

    fun getUserName() = context.dataStore.data.map { it[KEY_NAME] ?: "" }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = name
        }
    }

    suspend fun saveUserStatus(status: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_STATUS] = status
        }
    }

    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_FCM_TOKEN] = token
        }
    }

    fun getFcmToken() = context.dataStore.data.map { it[KEY_FCM_TOKEN] ?: "" }

    suspend fun saveRememberedEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_REMEMBERED_EMAIL] = email
        }
    }

    fun getRememberedEmail() = context.dataStore.data.map { it[KEY_REMEMBERED_EMAIL] ?: "" }

    suspend fun clearRememberedEmail() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_REMEMBERED_EMAIL)
        }
    }

    fun getLanguage() = context.dataStore.data.map { it[KEY_LANGUAGE] ?: "ko" }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = languageCode
        }
    }

    suspend fun clearUserInfo() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_MEMBER_ID)
            prefs.remove(KEY_NAME)
            prefs.remove(KEY_TOKEN)
            prefs.remove(KEY_STATUS)
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
