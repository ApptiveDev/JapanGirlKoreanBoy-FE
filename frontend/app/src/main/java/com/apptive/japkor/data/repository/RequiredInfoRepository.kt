package com.apptive.japkor.data.repository


import android.util.Log
import com.apptive.japkor.data.api.RequiredInfoApiService
import com.apptive.japkor.data.api.ServiceFactory
import com.apptive.japkor.data.model.ErrorResponse
import com.apptive.japkor.data.model.PresignedUrlRequest
import com.apptive.japkor.data.model.PresignedUrlResponse
import com.apptive.japkor.data.model.RequiredInfoDTO
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse

data class ApiResult<T>(
    val success: Boolean,
    val code: Int,
    val data: T? = null,
    val errorMessage: String? = null
)

class RequiredInfoRepository(
    private val api: RequiredInfoApiService = ServiceFactory.requiredInfoApiService
) {

    private val okHttpClient = OkHttpClient()

    suspend fun postRequiredInfo(body: RequiredInfoDTO): ApiResult<Unit> {
        val response = api.postRequiredInfo(body).awaitResponse()

        Log.d("RequiredInfoRepository",
            "postRequiredInfo success=${response.isSuccessful} code=${response.code()}")

        if (response.isSuccessful) {
            return ApiResult(
                success = true,
                code = response.code()
            )
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

        return ApiResult(
            success = false,
            code = response.code(),
            errorMessage = errorMessage
        )
    }

    suspend fun getPresignedUrls(request: PresignedUrlRequest): ApiResult<PresignedUrlResponse> {
        val response = api.getPresignedUrl(request).awaitResponse()

        if (response.isSuccessful){
            return ApiResult(
                success = true,
                code = response.code(),
                data = response.body()
            )
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = try {
            Gson().fromJson(errorJson, ErrorResponse::class.java)?.message
        } catch (e: Exception) {
            null
        }
        return ApiResult(
            success = false,
            code = response.code(),
            errorMessage = errorMessage
        )
    }

    suspend fun getPresignedUrlList(requests: List<PresignedUrlRequest>): ApiResult<List<PresignedUrlResponse>> {
        val response = api.getPresignedUrlList(requests).awaitResponse()

        if (response.isSuccessful){
            return ApiResult(
                success = true,
                code = response.code(),
                data = response.body()
            )
        }

        val errorJson = response.errorBody()?.string()
        val errorMessage = try {
            Gson().fromJson(errorJson, ErrorResponse::class.java)?.message
        } catch (e: Exception) {
            null
        }
        return ApiResult(
            success = false,
            code = response.code(),
            errorMessage = errorMessage
        )
    }

    suspend fun uploadImageToPresignedUrl(
        presignedUrl: String,
        bytes: ByteArray,
        contentType: String
    ): Boolean {
        val request = Request.Builder()
            .url(presignedUrl)
            .put(bytes.toRequestBody(contentType.toMediaTypeOrNull()))
            .build()

        val response = okHttpClient.newCall(request).execute()
        response.use {
            return it.isSuccessful
        }
    }
}
