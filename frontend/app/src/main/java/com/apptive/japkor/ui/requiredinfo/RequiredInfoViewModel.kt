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
    fun setGender(label: String) {
        _gender.value = RequiredInfoMapper.gender(label)
    }

    private val _height = MutableStateFlow<Int?>(null)
    val height: StateFlow<Int?> = _height
    fun setHeight(value: Int?) { _height.value = value }

    private val _weight = MutableStateFlow<Int?>(null)
    val weight: StateFlow<Int?> = _weight
    fun setWeight(value: Int?) { _weight.value = value }

    private val _region = MutableStateFlow<String?>(null)
    val region: StateFlow<String?> = _region
    fun setRegion(value: String) { _region.value = value }

    private val _smoking = MutableStateFlow<String?>(null)
    val smoking: StateFlow<String?> = _smoking
    fun setSmoking(label: String) {
        _smoking.value = RequiredInfoMapper.smoking(label)
    }

    private val _drinking = MutableStateFlow<String?>(null)
    val drinking: StateFlow<String?> = _drinking
    fun setDrinking(label: String) {
        _drinking.value = RequiredInfoMapper.drinking(label)
    }

    private val _religion = MutableStateFlow<String?>(null)
    val religion: StateFlow<String?> = _religion
    fun setReligion(label: String) {
        _religion.value = RequiredInfoMapper.religion(label)
    }

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState

    fun submitRequiredInfo() {

        if (_gender.value == null) return setError("성별을 선택해주세요.")
        if (_height.value == null) return setError("키를 입력해주세요.")
        if (_weight.value == null) return setError("몸무게를 입력해주세요.")
        if (_region.value == null) return setError("거주 지역을 선택해주세요.")
        if (_smoking.value == null) return setError("흡연 여부를 선택해주세요.")
        if (_drinking.value == null) return setError("음주 빈도를 선택해주세요.")
        if (_religion.value == null) return setError("종교를 선택해주세요.")

        val dto = RequiredInfoDTO(
            gender = _gender.value,
            height = _height.value,
            weight = _weight.value,
            residenceArea = _region.value,
            smokingStatus = _smoking.value,
            drinkingFrequency = _drinking.value,
            religion = _religion.value,

            education = null,
            asset = null,
            otherInfo = null,
            profileImageUrls = null,
            thumbnailImageUrl = null,
            preferredHeightMin = null,
            preferredHeightMax = null,
            avoidReligions = null,
            preferredEducationLevel = null,
            preferredAppearanceStyle = null,
            parentAssetRequirement = null,
            preferredAssetMin = null,
            preferredAssetMax = null,
            preferredJobs = null,
            avoidedJobs = null,
            mbti1 = null,
            mbti2 = null,
            mbti3 = null,
            mbti4 = null,
            priority1 = null,
            priority2 = null,
            priority3 = null
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
