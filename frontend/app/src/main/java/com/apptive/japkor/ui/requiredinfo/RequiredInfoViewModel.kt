package com.apptive.japkor.ui.requiredinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.model.RequiredInfoDTO
import com.apptive.japkor.data.repository.RequiredInfoRepository
import com.apptive.japkor.utils.required_info.RequiredInfoMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RequiredInfoViewModel(
    private val repository: RequiredInfoRepository = RequiredInfoRepository()
) : ViewModel() {

    private val _gender = MutableStateFlow<String?>(null)
    val gender: StateFlow<String?> = _gender
    fun setGender(label: String) { _gender.value = RequiredInfoMapper.gender(label) }

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

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState

    fun submitRequiredInfo() {

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
            profileImageUrls = null,
            thumbnailImageUrl = null,
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
            mbti1 = mbti1!!,
            mbti2 = mbti2!!,
            mbti3 = mbti3!!,
            mbti4 = mbti4!!,
            priority1 = priority1,
            priority2 = priority2,
            priority3 = priority3
        )

        viewModelScope.launch {
            _submitState.value = SubmitState.Loading

            val (success, errorMessage) = repository.postRequiredInfo(dto)

            _submitState.value =
                if (success) SubmitState.Success
                else SubmitState.Error(errorMessage ?: "알 수 없는 오류가 발생했습니다.")
        }
    }

    private fun setError(msg: String) {
        _submitState.value = SubmitState.Error(msg)
    }
}

sealed class SubmitState {
    object Idle : SubmitState()
    object Loading : SubmitState()
    object Success : SubmitState()
    data class Error(val message: String) : SubmitState()
}
