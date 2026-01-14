package com.apptive.japkor.data.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.apptive.japkor.data.model.UserInfoResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import com.apptive.japkor.utils.required_info.RequiredInfoReverseMapper

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {
    private val gson = Gson()

    companion object {
        val KEY_MEMBER_ID = intPreferencesKey("member_id")      // Int → intPreferencesKey
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_TOKEN = stringPreferencesKey("token")
        val KEY_STATUS = stringPreferencesKey("status")
        val KEY_GENDER = stringPreferencesKey("gender")
        val KEY_PROVIDER = stringPreferencesKey("provider")
        val KEY_PROVIDER_ID = stringPreferencesKey("provider_id")
        val KEY_EMAIL = stringPreferencesKey("email")
        val KEY_HEIGHT = intPreferencesKey("height")
        val KEY_WEIGHT = intPreferencesKey("weight")
        val KEY_RESIDENCE_AREA = stringPreferencesKey("residence_area")
        val KEY_SMOKING_STATUS = stringPreferencesKey("smoking_status")
        val KEY_DRINKING_FREQUENCY = stringPreferencesKey("drinking_frequency")
        val KEY_RELIGION = stringPreferencesKey("religion")
        val KEY_EDUCATION = stringPreferencesKey("education")
        val KEY_ASSET = stringPreferencesKey("asset")
        val KEY_OTHER_INFO = stringPreferencesKey("other_info")
        val KEY_THUMBNAIL_IMAGE_URL = stringPreferencesKey("thumbnail_image_url")
        val KEY_PROFILE_IMAGE_URLS = stringPreferencesKey("profile_image_urls")
        val KEY_AI_SUMMARY = stringPreferencesKey("ai_summary")
        val KEY_AI_SUMMARY_JP = stringPreferencesKey("ai_summary_jp")
        val KEY_CREATED_AT = stringPreferencesKey("created_at")
        val KEY_UPDATED_AT = stringPreferencesKey("updated_at")
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

    suspend fun saveMemberInfo(info: UserInfoResponse) {
        context.dataStore.edit { prefs ->
            prefs[KEY_MEMBER_ID] = info.memberId.toInt()
            prefs[KEY_PROVIDER] = info.provider
            prefs[KEY_EMAIL] = info.email
            prefs[KEY_STATUS] = info.status.name
            prefs[KEY_GENDER] = info.gender
            prefs[KEY_CREATED_AT] = info.createdAt
            prefs[KEY_UPDATED_AT] = info.updatedAt

            setOptionalString(prefs, KEY_PROVIDER_ID, info.providerId)
            setOptionalString(prefs, KEY_NAME, info.name)
            setOptionalInt(prefs, KEY_HEIGHT, info.height)
            setOptionalInt(prefs, KEY_WEIGHT, info.weight)
            setOptionalString(prefs, KEY_RESIDENCE_AREA, info.residenceArea)
            setOptionalString(prefs, KEY_SMOKING_STATUS, info.smokingStatus)
            setOptionalString(prefs, KEY_DRINKING_FREQUENCY, info.drinkingFrequency)
            setOptionalString(prefs, KEY_RELIGION, info.religion)
            setOptionalString(prefs, KEY_EDUCATION, info.education)
            setOptionalString(prefs, KEY_ASSET, info.asset)
            setOptionalString(prefs, KEY_OTHER_INFO, info.otherInfo)
            setOptionalString(prefs, KEY_THUMBNAIL_IMAGE_URL, info.thumbnailImageUrl)
            setOptionalString(prefs, KEY_AI_SUMMARY, info.aiSummary)
            setOptionalString(prefs, KEY_AI_SUMMARY_JP, info.aiSummaryJp)

            val profileUrlsJson = info.profileImageUrls
                ?.takeIf { it.isNotEmpty() }
                ?.let { gson.toJson(it) }
            setOptionalString(prefs, KEY_PROFILE_IMAGE_URLS, profileUrlsJson)
        }
    }

    fun getUserToken() = context.dataStore.data.map { it[KEY_TOKEN] ?: "" }

    fun getUserStatus() = context.dataStore.data.map { it[KEY_STATUS] ?: "" }

    fun getUserInfo() = context.dataStore.data.map {
        mapOf(
            "memberId" to (it[KEY_MEMBER_ID] ?: -1),
            "name" to (it[KEY_NAME] ?: ""),
            "token" to (it[KEY_TOKEN] ?: ""),
            "status" to (it[KEY_STATUS] ?: ""),
            "gender" to (it[KEY_GENDER] ?: ""),
            "provider" to (it[KEY_PROVIDER] ?: ""),
            "providerId" to (it[KEY_PROVIDER_ID] ?: ""),
            "email" to (it[KEY_EMAIL] ?: ""),
            "height" to (it[KEY_HEIGHT] ?: -1),
            "weight" to (it[KEY_WEIGHT] ?: -1),
            "residenceArea" to (it[KEY_RESIDENCE_AREA] ?: ""),
            "smokingStatus" to (it[KEY_SMOKING_STATUS] ?: ""),
            "drinkingFrequency" to (it[KEY_DRINKING_FREQUENCY] ?: ""),
            "religion" to (it[KEY_RELIGION] ?: ""),
            "education" to (it[KEY_EDUCATION] ?: ""),
            "asset" to (it[KEY_ASSET] ?: ""),
            "otherInfo" to (it[KEY_OTHER_INFO] ?: ""),
            "thumbnailImageUrl" to (it[KEY_THUMBNAIL_IMAGE_URL] ?: ""),
            "profileImageUrls" to (it[KEY_PROFILE_IMAGE_URLS] ?: ""),
            "aiSummary" to (it[KEY_AI_SUMMARY] ?: ""),
            "aiSummaryJp" to (it[KEY_AI_SUMMARY_JP] ?: ""),
            "createdAt" to (it[KEY_CREATED_AT] ?: ""),
            "updatedAt" to (it[KEY_UPDATED_AT] ?: "")
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

    suspend fun saveUserGender(gender: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_GENDER] = gender
        }
    }

    fun getUserGender() = context.dataStore.data.map { it[KEY_GENDER] ?: "" }

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
            prefs.remove(KEY_GENDER)
            prefs.remove(KEY_PROVIDER)
            prefs.remove(KEY_PROVIDER_ID)
            prefs.remove(KEY_EMAIL)
            prefs.remove(KEY_HEIGHT)
            prefs.remove(KEY_WEIGHT)
            prefs.remove(KEY_RESIDENCE_AREA)
            prefs.remove(KEY_SMOKING_STATUS)
            prefs.remove(KEY_DRINKING_FREQUENCY)
            prefs.remove(KEY_RELIGION)
            prefs.remove(KEY_EDUCATION)
            prefs.remove(KEY_ASSET)
            prefs.remove(KEY_OTHER_INFO)
            prefs.remove(KEY_THUMBNAIL_IMAGE_URL)
            prefs.remove(KEY_PROFILE_IMAGE_URLS)
            prefs.remove(KEY_AI_SUMMARY)
            prefs.remove(KEY_AI_SUMMARY_JP)
            prefs.remove(KEY_CREATED_AT)
            prefs.remove(KEY_UPDATED_AT)
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    private fun setOptionalString(
        prefs: MutablePreferences,
        key: Preferences.Key<String>,
        value: String?
    ) {
        if (value == null) {
            prefs.remove(key)
        } else {
            prefs[key] = value
        }
    }

    private fun setOptionalInt(
        prefs: MutablePreferences,
        key: Preferences.Key<Int>,
        value: Int?
    ) {
        if (value == null) {
            prefs.remove(key)
        } else {
            prefs[key] = value
        }
    }

    // My Profile UI Model and Flow
    data class MyProfileUiModel(
        val name: String,
        val residenceArea: String,
        val smokingStatus: String,
        val drinkingFrequency: String,
        val education: String,
        val religion: String,
        val thumbnailImageUrl: String,
    )

    fun getMyProfileUiModel(): Flow<MyProfileUiModel> {
        val nameFlow = getUserName()
        val residenceFlow = context.dataStore.data.map { it[KEY_RESIDENCE_AREA] ?: "" }
        val smokingFlow = context.dataStore.data.map { it[KEY_SMOKING_STATUS] ?: "" }
        val drinkingFlow = context.dataStore.data.map { it[KEY_DRINKING_FREQUENCY] ?: "" }
        val educationFlow = context.dataStore.data.map { it[KEY_EDUCATION] ?: "" }
        val religionFlow = context.dataStore.data.map { it[KEY_RELIGION] ?: "" }
        val thumbFlow = context.dataStore.data.map { it[KEY_THUMBNAIL_IMAGE_URL] ?: "" }

        return combine(
            nameFlow, residenceFlow, smokingFlow, drinkingFlow, educationFlow, religionFlow, thumbFlow
        ) { values: Array<String> ->
            val name = values[0]
            val residence = values[1]
            val smokingCode = values[2]
            val drinkingCode = values[3]
            val educationCode = values[4]
            val religionCode = values[5]
            val thumb = values[6]

            MyProfileUiModel(
                name = name,
                residenceArea = residence,
                smokingStatus = RequiredInfoReverseMapper.smoking(smokingCode),
                drinkingFrequency = RequiredInfoReverseMapper.drinking(drinkingCode),
                education = RequiredInfoReverseMapper.education(educationCode),
                religion = RequiredInfoReverseMapper.religion(religionCode),
                thumbnailImageUrl = thumb
            )
        }
    }
}
