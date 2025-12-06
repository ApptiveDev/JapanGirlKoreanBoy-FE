package com.apptive.japkor.data.repository


import android.util.Log
import com.apptive.japkor.data.api.RequiredInfoApiService
import com.apptive.japkor.data.api.ServiceFactory
import com.apptive.japkor.data.model.ErrorResponse
import com.apptive.japkor.data.model.RequiredInfoDTO
import com.google.gson.Gson
import retrofit2.awaitResponse

class RequiredInfoRepository(
    private val api: RequiredInfoApiService = ServiceFactory.requiredInfoApiService
) {

    suspend fun postRequiredInfo(body: RequiredInfoDTO): Pair<Boolean, String?> {
        val response = api.postRequiredInfo(body).awaitResponse()

        Log.d("RequiredInfoRepository",
            "postRequiredInfo success=${response.isSuccessful} code=${response.code()}")

        if (response.isSuccessful) {
            return Pair(true, null)
        }

        // ❗ 실패 시 서버의 message 파싱
        val errorJson = response.errorBody()?.string()
        val errorMessage = try {
            Gson().fromJson(errorJson, ErrorResponse::class.java)?.message
        } catch (e: Exception) {
            null
        }

        Log.e("RequiredInfoRepository",
            "postRequiredInfo failed: $errorMessage")

        return Pair(false, errorMessage)
    }
}
