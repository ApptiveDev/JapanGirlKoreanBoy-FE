package com.apptive.japkor.ui.requiredinfo

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.model.PresignedUrlRequest
import com.apptive.japkor.data.model.RequiredInfoDTO
import com.apptive.japkor.data.model.UserStatus
import com.apptive.japkor.data.repository.ApiResult
import com.apptive.japkor.data.repository.RequiredInfoRepository
import com.apptive.japkor.ui.components.ToastType
import com.apptive.japkor.utils.required_info.RequiredInfoMapper
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class UploadStatus { Uploading, Success, Failed }

data class ProfileImageState(
    val id: String = UUID.randomUUID().toString(),
    val uri: Uri,
    val fileName: String,
    val contentType: String,
    val uploadedUrl: String? = null,
    val status: UploadStatus = UploadStatus.Uploading,
    val errorMessage: String? = null
)

sealed class RequiredInfoEvent {
    data class ShowToast(val message: String, val type: ToastType = ToastType.INFO) : RequiredInfoEvent()
    object NavigateToComplete : RequiredInfoEvent()
}

class RequiredInfoViewModel(
    private val dataStore: DataStoreManager,
    private val repository: RequiredInfoRepository = RequiredInfoRepository()
) : ViewModel() {

    private val _gender = MutableStateFlow<String?>(null)
    val gender: StateFlow<String?> = _gender
    fun setGender(label: String) { _gender.value = RequiredInfoMapper.gender(label) }

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    fun setName(value: String) { _name.value = value }

    private val _height = MutableStateFlow<Int?>(null)
    val height: StateFlow<Int?> = _height
    fun setHeight(value: Int?) { _height.value = value }

    private val _weight = MutableStateFlow<Int?>(null)
    val weight: StateFlow<Int?> = _weight
    fun setWeight(value: Int?) { _weight.value = value }

    private val _region = MutableStateFlow("")
    val region: StateFlow<String> = _region
    fun setRegion(value: String) { _region.value = value }

    private val _smoking = MutableStateFlow<String?>(null)
    val smoking: StateFlow<String?> = _smoking
    fun setSmoking(label: String) { _smoking.value = RequiredInfoMapper.smoking(label) }

    private val _drinking = MutableStateFlow<String?>(null)
    val drinking: StateFlow<String?> = _drinking
    fun setDrinking(label: String) { _drinking.value = RequiredInfoMapper.drinking(label) }

    private val _religion = MutableStateFlow<String?>(null)
    val religion: StateFlow<String?> = _religion
    fun setReligion(label: String) { _religion.value = RequiredInfoMapper.religion(label) }

    private val _education = MutableStateFlow<String?>(null)
    val education: StateFlow<String?> = _education
    fun setEducation(label: String) { _education.value = RequiredInfoMapper.education(label) }

    private val _asset = MutableStateFlow<String?>(null)
    val asset: StateFlow<String?> = _asset
    fun setAsset(label: String) { _asset.value = RequiredInfoMapper.asset(label) }

    private val _otherInfo = MutableStateFlow("")
    val otherInfo: StateFlow<String> = _otherInfo
    fun setOtherInfo(value: String) { _otherInfo.value = value }

    private val _preferredHeightMin = MutableStateFlow<Int?>(null)
    val preferredHeightMin: StateFlow<Int?> = _preferredHeightMin
    fun setPreferredHeightMin(value: Int) { _preferredHeightMin.value = value }

    private val _preferredHeightMax = MutableStateFlow<Int?>(null)
    val preferredHeightMax: StateFlow<Int?> = _preferredHeightMax
    fun setPreferredHeightMax(value: Int) { _preferredHeightMax.value = value }

    private val _avoidReligions = MutableStateFlow<Set<String>>(emptySet())
    val avoidReligions: StateFlow<Set<String>> = _avoidReligions
    fun toggleAvoidReligion(label: String) {
        val mapped = RequiredInfoMapper.religion(label) ?: return
        val updated = if (mapped == "NONE") {
            setOf("NONE")
        } else {
            (_avoidReligions.value - "NONE").let { current ->
                if (current.contains(mapped)) current - mapped else current + mapped
            }
        }
        _avoidReligions.value = updated
    }

    private val _preferredEducationLevel = MutableStateFlow<String?>(null)
    val preferredEducationLevel: StateFlow<String?> = _preferredEducationLevel
    fun setPreferredEducationLevel(label: String) {
        _preferredEducationLevel.value = RequiredInfoMapper.preferredEducation(label)
    }

    private val _preferredAppearanceStyle = MutableStateFlow<String?>(null)
    val preferredAppearanceStyle: StateFlow<String?> = _preferredAppearanceStyle
    fun setPreferredAppearanceStyle(label: String) {
        _preferredAppearanceStyle.value = RequiredInfoMapper.appearanceStyle(label)
    }

    private val _parentAssetRequirement = MutableStateFlow<String?>(null)
    val parentAssetRequirement: StateFlow<String?> = _parentAssetRequirement
    fun setParentAssetRequirement(label: String) {
        _parentAssetRequirement.value = RequiredInfoMapper.parentAssetRequirement(label)
    }

    private val _preferredAssetMin = MutableStateFlow<Long?>(null)
    val preferredAssetMin: StateFlow<Long?> = _preferredAssetMin
    fun setPreferredAssetMin(value: Long?) { _preferredAssetMin.value = value }

    private val _preferredAssetMax = MutableStateFlow<Long?>(null)
    val preferredAssetMax: StateFlow<Long?> = _preferredAssetMax
    fun setPreferredAssetMax(value: Long?) { _preferredAssetMax.value = value }

    private val _preferredJobs = MutableStateFlow<Set<String>>(emptySet())
    val preferredJobs: StateFlow<Set<String>> = _preferredJobs
    fun togglePreferredJob(label: String) {
        val mapped = RequiredInfoMapper.job(label) ?: return
        _preferredJobs.value = if (_preferredJobs.value.contains(mapped)) {
            _preferredJobs.value - mapped
        } else {
            if (_preferredJobs.value.size >= 3) {
                setError("선호 직업은 최대 3개까지 선택 가능합니다.")
                _preferredJobs.value
            } else {
                _preferredJobs.value + mapped
            }
        }
    }

    private val _avoidedJobs = MutableStateFlow<Set<String>>(emptySet())
    val avoidedJobs: StateFlow<Set<String>> = _avoidedJobs
    fun toggleAvoidedJob(label: String) {
        val mapped = RequiredInfoMapper.job(label) ?: return
        _avoidedJobs.value = if (_avoidedJobs.value.contains(mapped)) {
            _avoidedJobs.value - mapped
        } else {
            if (_avoidedJobs.value.size >= 3) {
                setError("비선호 직업은 최대 3개까지 선택 가능합니다.")
                _avoidedJobs.value
            } else {
                _avoidedJobs.value + mapped
            }
        }
    }

    private val _mbti1 = MutableStateFlow<String?>(null)
    val mbti1: StateFlow<String?> = _mbti1
    fun setMbti1(value: String) { _mbti1.value = value.uppercase() }

    private val _mbti2 = MutableStateFlow<String?>(null)
    val mbti2: StateFlow<String?> = _mbti2
    fun setMbti2(value: String) { _mbti2.value = value.uppercase() }

    private val _mbti3 = MutableStateFlow<String?>(null)
    val mbti3: StateFlow<String?> = _mbti3
    fun setMbti3(value: String) { _mbti3.value = value.uppercase() }

    private val _mbti4 = MutableStateFlow<String?>(null)
    val mbti4: StateFlow<String?> = _mbti4
    fun setMbti4(value: String) { _mbti4.value = value.uppercase() }

    private val _priority1 = MutableStateFlow<String?>(null)
    val priority1: StateFlow<String?> = _priority1
    fun setPriority1(label: String) { _priority1.value = RequiredInfoMapper.priority(label) }

    private val _priority2 = MutableStateFlow<String?>(null)
    val priority2: StateFlow<String?> = _priority2
    fun setPriority2(label: String) { _priority2.value = RequiredInfoMapper.priority(label) }

    private val _priority3 = MutableStateFlow<String?>(null)
    val priority3: StateFlow<String?> = _priority3
    fun setPriority3(label: String) { _priority3.value = RequiredInfoMapper.priority(label) }

    private val _profileImages = MutableStateFlow<List<ProfileImageState>>(emptyList())
    val profileImages: StateFlow<List<ProfileImageState>> = _profileImages.asStateFlow()

    private val _events = MutableSharedFlow<RequiredInfoEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<RequiredInfoEvent> = _events.asSharedFlow()

    val step1Valid: StateFlow<Boolean> = gender
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    @Suppress("UNCHECKED_CAST")
    val step2Valid: StateFlow<Boolean> = combine(
        name, height, weight, region, smoking, drinking, religion
    ) { values ->
        val n = values[0] as String
        val h = values[1] as Int?
        val w = values[2] as Int?
        val r = values[3] as String
        val s = values[4] as String?
        val d = values[5] as String?
        val rel = values[6] as String?
        n.isNotBlank() && h != null && w != null && r.isNotBlank() && s != null && d != null && rel != null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val step3Valid: StateFlow<Boolean> = combine(
        education, asset, otherInfo
    ) { edu, assets, intro ->
        edu != null && assets != null && intro.trim().isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val step4Valid: StateFlow<Boolean> = profileImages
        .map { images ->
            val completed = images.filter { it.status == UploadStatus.Success && it.uploadedUrl != null }
            completed.size >= 2 && images.none { it.status == UploadStatus.Uploading }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val heightRangeValid = combine(preferredHeightMin, preferredHeightMax) { minH, maxH ->
        minH != null && maxH != null &&
                minH in 130..230 &&
                maxH in 130..230 &&
                minH <= maxH
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val assetRangeValid = combine(preferredAssetMin, preferredAssetMax) { assetMin, assetMax ->
        assetMin != null && assetMax != null && assetMin <= assetMax
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val mbtiValid = combine(mbti1, mbti2, mbti3, mbti4) { m1, m2, m3, m4 ->
        (m1 in setOf("E", "I", "X")) &&
                (m2 in setOf("N", "S", "X")) &&
                (m3 in setOf("T", "F", "X")) &&
                (m4 in setOf("J", "P", "X"))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val prioritiesValid = combine(priority1, priority2, priority3) { p1, p2, p3 ->
        listOfNotNull(p1, p2, p3).size == 3 && setOf(p1, p2, p3).size == 3
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val jobsValid = combine(preferredJobs, avoidedJobs) { preferJob, avoidJob ->
        preferJob.isNotEmpty() && preferJob.size <= 3 &&
                avoidJob.isNotEmpty() && avoidJob.size <= 3
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    @Suppress("UNCHECKED_CAST")
    val step5Valid: StateFlow<Boolean> = combine(
        heightRangeValid,
        avoidReligions,
        preferredEducationLevel,
        preferredAppearanceStyle,
        parentAssetRequirement,
        assetRangeValid,
        jobsValid,
        mbtiValid,
        prioritiesValid
    ) { values ->
        val heightValid = values[0] as Boolean
        val avoid = values[1] as Set<String>
        val edu = values[2] as String?
        val appearance = values[3] as String?
        val parentAsset = values[4] as String?
        val assetValid = values[5] as Boolean
        val jobValid = values[6] as Boolean
        val mbtiOk = values[7] as Boolean
        val priorityOk = values[8] as Boolean

        heightValid &&
                avoid.isNotEmpty() &&
                edu != null &&
                appearance != null &&
                parentAsset != null &&
                assetValid &&
                jobValid &&
                mbtiOk &&
                priorityOk
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState

    fun submitRequiredInfo() {
        val uploadedImages = _profileImages.value.filter { it.status == UploadStatus.Success && it.uploadedUrl != null }
        if (uploadedImages.isEmpty()) {
            return setError("프로필 사진을 최소 1장 이상 업로드해주세요.")
        }
        val profileImageUrls = uploadedImages.mapNotNull { it.uploadedUrl }
        val thumbnailImageUrl = profileImageUrls.firstOrNull()

        val name = _name.value.trim()
        if (name.isEmpty()) return setError("이름을 입력해주세요.")
        val gender = _gender.value ?: return setError("성별을 선택해주세요.")
        val height = _height.value ?: return setError("키를 입력해주세요.")
        val weight = _weight.value ?: return setError("몸무게를 입력해주세요.")
        val region = _region.value.takeIf { it.isNotBlank() }
            ?: return setError("거주 지역을 입력해주세요.")
        val smoking = _smoking.value ?: return setError("흡연 유무를 입력해주세요.")
        val drinking = _drinking.value ?: return setError("음주 빈도를 선택해주세요.")
        val religion = _religion.value ?: return setError("종교를 선택해주세요.")
        val education = _education.value ?: return setError("학력을 선택해주세요.")
        val asset = _asset.value ?: return setError("자산을 선택해주세요.")

        val otherInfo = _otherInfo.value.trim()
        if (otherInfo.isEmpty()) return setError("기타 정보를 입력해주세요.")
        if (otherInfo.length > 300) return setError("기타 정보는 최대 300자까지 입력 가능합니다.")

        val preferredHeightMin = _preferredHeightMin.value
            ?: return setError("선호 키 최소값을 입력해주세요.")
        val preferredHeightMax = _preferredHeightMax.value
            ?: return setError("선호 키 최대값을 입력해주세요.")
        if (preferredHeightMin < 130) return setError("선호 키 최소값은 130 이상이어야 합니다.")
        if (preferredHeightMax > 230) return setError("선호 키 최대값은 230 이하여야 합니다.")
        if (preferredHeightMin > preferredHeightMax) return setError("선호 키 범위를 확인해주세요.")

        val avoidReligions = _avoidReligions.value
        if (avoidReligions.isEmpty()) return setError("기피 종교를 선택해주세요.")

        val preferredEducationLevel = _preferredEducationLevel.value
            ?: return setError("선호 학벌을 선택해주세요.")
        val preferredAppearanceStyle = _preferredAppearanceStyle.value
            ?: return setError("선호 외모 스타일을 선택해주세요.")
        val parentAssetRequirement = _parentAssetRequirement.value
            ?: return setError("부모님 자산 요구사항을 선택해주세요.")

        val preferredAssetMin = _preferredAssetMin.value
            ?: return setError("선호 자산 최소값을 입력해주세요.")
        val preferredAssetMax = _preferredAssetMax.value
            ?: return setError("선호 자산 최대값을 입력해주세요.")
        if (preferredAssetMin > preferredAssetMax) return setError("선호 자산 최소/최대값을 확인해주세요.")

        val preferredJobs = _preferredJobs.value
        if (preferredJobs.isEmpty()) return setError("선호 직업을 선택해주세요.")
        if (preferredJobs.size > 3) return setError("선호 직업은 최대 3개까지 선택 가능합니다.")

        val avoidedJobs = _avoidedJobs.value
        if (avoidedJobs.isEmpty()) return setError("비선호 직업을 선택해주세요.")
        if (avoidedJobs.size > 3) return setError("비선호 직업은 최대 3개까지 선택 가능합니다.")

        val mbti1 = _mbti1.value
        val mbti2 = _mbti2.value
        val mbti3 = _mbti3.value
        val mbti4 = _mbti4.value
        val mbti1Valid = mbti1 != null && setOf("E", "I", "X").contains(mbti1)
        val mbti2Valid = mbti2 != null && setOf("N", "S", "X").contains(mbti2)
        val mbti3Valid = mbti3 != null && setOf("T", "F", "X").contains(mbti3)
        val mbti4Valid = mbti4 != null && setOf("J", "P", "X").contains(mbti4)
        if (!(mbti1Valid && mbti2Valid && mbti3Valid && mbti4Valid)) {
            return setError("MBTI E/I는 E, I 중 하나여야 합니다.")
        }

        val priority1 = _priority1.value ?: return setError("1순위를 선택해주세요.")
        val priority2 = _priority2.value ?: return setError("2순위를 선택해주세요.")
        val priority3 = _priority3.value ?: return setError("3순위를 선택해주세요.")
        val priorities = listOf(priority1, priority2, priority3)
        if (priorities.toSet().size != 3) return setError("우선순위는 중복될 수 없습니다.")

        val dto = RequiredInfoDTO(
            name = name,
            gender = gender,
            height = height,
            weight = weight,
            residenceArea = region,
            smokingStatus = smoking,
            drinkingFrequency = drinking,
            religion = religion,
            education = education,
            asset = asset,
            otherInfo = otherInfo,
            profileImageUrls = profileImageUrls,
            thumbnailImageUrl = thumbnailImageUrl,
            preferredHeightMin = preferredHeightMin,
            preferredHeightMax = preferredHeightMax,
            avoidReligions = avoidReligions.toList(),
            preferredEducationLevel = preferredEducationLevel,
            preferredAppearanceStyle = preferredAppearanceStyle,
            parentAssetRequirement = parentAssetRequirement,
            preferredAssetMin = preferredAssetMin,
            preferredAssetMax = preferredAssetMax,
            preferredJobs = preferredJobs.toList(),
            avoidedJobs = avoidedJobs.toList(),
            mbti1 = mbti1,
            mbti2 = mbti2,
            mbti3 = mbti3,
            mbti4 = mbti4,
            priority1 = priority1,
            priority2 = priority2,
            priority3 = priority3
        )
        Log.d(TAG, "submitRequiredInfo dto=$dto")

        viewModelScope.launch {
            _submitState.value = SubmitState.Loading

            val result: ApiResult<Unit> = repository.postRequiredInfo(dto)
            Log.d(TAG, "postRequiredInfo result success=${result.success} code=${result.code}")

            if (result.success && result.code in 200..299) {
                _submitState.value = SubmitState.Success
                dataStore.saveUserName(name)
                dataStore.saveUserStatus(UserStatus.PENDING_APPROVAL.name)
                _events.emit(RequiredInfoEvent.NavigateToComplete)
            } else {
                val message = result.errorMessage ?: "알 수 없는 오류가 발생했습니다."
                _submitState.value = SubmitState.Error(message)
                _events.emit(RequiredInfoEvent.ShowToast(message, ToastType.ERROR))
            }
        }
    }

    fun uploadProfileImage(context: Context, uri: Uri, targetIndex: Int? = null) {
        val index = targetIndex ?: _profileImages.value.size
        if (index >= MAX_IMAGES) {
            viewModelScope.launch {
                _events.emit(
                    RequiredInfoEvent.ShowToast(
                        "이미지는 최대 ${MAX_IMAGES}장까지 업로드할 수 있습니다.",
                        ToastType.ERROR
                    )
                )
            }
            return
        }

        val fileName = resolveFileName(context, uri)
        val contentType = resolveContentType(context, uri)
        val newState = ProfileImageState(
            uri = uri,
            fileName = fileName,
            contentType = contentType,
            status = UploadStatus.Uploading
        )
        val imageId = newState.id

        placeImageState(index, newState)

        viewModelScope.launch {
            val presignedResult = repository.getPresignedUrls(
                PresignedUrlRequest(fileName, contentType)
            )
            val presigned = presignedResult.data
            if (!presignedResult.success || presigned == null) {
                markUploadFailure(imageId, presignedResult.errorMessage ?: "Presigned URL 발급에 실패했습니다.")
                return@launch
            }
            Log.d(TAG, "Presigned URL issued for $fileName: ${presigned.presignedUrl}")

            val bytes = withContext(Dispatchers.IO) {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            }
            if (bytes == null) {
                markUploadFailure(imageId, "이미지 파일을 불러올 수 없습니다.")
                return@launch
            }

            val uploadSuccess = withContext(Dispatchers.IO) {
                repository.uploadImageToPresignedUrl(
                    presigned.presignedUrl,
                    bytes,
                    presigned.contentType
                )
            }

            if (!uploadSuccess) {
                markUploadFailure(imageId, "이미지 업로드에 실패했습니다.")
                return@launch
            }

            val remoteUrl = presigned.presignedUrl.substringBefore("?")
            Log.d(TAG, "Image upload completed. Remote URL=$remoteUrl")
            updateImageState(imageId) {
                it.copy(
                    uploadedUrl = remoteUrl,
                    status = UploadStatus.Success,
                    errorMessage = null
                )
            }
        }
    }

    private fun placeImageState(index: Int, state: ProfileImageState) {
        _profileImages.update { current ->
            val mutable = current.toMutableList()
            if (index < mutable.size) {
                mutable[index] = state
            } else {
                mutable.add(state)
            }
            if (mutable.size > MAX_IMAGES) {
                mutable.subList(MAX_IMAGES, mutable.size).clear()
            }
            mutable
        }
    }

    private fun updateImageState(imageId: String, transform: (ProfileImageState) -> ProfileImageState) {
        _profileImages.update { images ->
            images.map { if (it.id == imageId) transform(it) else it }
        }
    }

    private suspend fun markUploadFailure(imageId: String, message: String) {
        updateImageState(imageId) {
            it.copy(status = UploadStatus.Failed, errorMessage = message)
        }
        _events.emit(RequiredInfoEvent.ShowToast(message, ToastType.ERROR))
    }

    private fun resolveFileName(context: Context, uri: Uri): String {
        val nameFromCursor = runCatching {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex >= 0) {
                    cursor.getString(nameIndex)
                } else {
                    null
                }
            }
        }.getOrNull()

        return nameFromCursor?.takeIf { it.isNotBlank() }
            ?: "image_${System.currentTimeMillis()}.jpg"
    }

    private fun resolveContentType(context: Context, uri: Uri): String {
        return context.contentResolver.getType(uri) ?: "image/jpeg"
    }

    private fun setError(msg: String) {
        _submitState.value = SubmitState.Error(msg)
        viewModelScope.launch {
            _events.emit(RequiredInfoEvent.ShowToast(msg, ToastType.ERROR))
        }
    }

    companion object {
        private const val MAX_IMAGES = 6
        private const val TAG = "RequiredInfoViewModel"
    }
}

sealed class SubmitState {
    object Idle : SubmitState()
    object Loading : SubmitState()
    object Success : SubmitState()
    data class Error(val message: String) : SubmitState()
}
