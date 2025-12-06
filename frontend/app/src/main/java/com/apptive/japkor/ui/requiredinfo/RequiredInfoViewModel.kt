package com.apptive.japkor.ui.requiredinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.model.RequiredInfoDTO
import com.apptive.japkor.data.repository.RequiredInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RequiredInfoViewModel(
    private val repository: RequiredInfoRepository = RequiredInfoRepository()
) : ViewModel() {
    private val _gender = MutableStateFlow<String?>(null)
    val gender: StateFlow<String?> = _gender

    fun setGender(value: String){
        _gender.value = value
    }

    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState

    fun submitRequiredInfo() {
        if (_gender.value == null){
            _submitState.value = SubmitState.Error("성별을 선택해주세요.")
            return
        }

        val dto = RequiredInfoDTO(
            gender = _gender.value,
            height = null,
            weight = null,
            residenceArea = null,
            smokingStatus = null,
            drinkingFrequency = null,
            religion = null,
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

            if (success) {
                _submitState.value = SubmitState.Success
            } else {
                _submitState.value = SubmitState.Error(errorMessage ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }
}


sealed class SubmitState {
    object Idle : SubmitState()
    object Loading : SubmitState()
    object Success : SubmitState()
    data class Error(val message: String) : SubmitState()
}