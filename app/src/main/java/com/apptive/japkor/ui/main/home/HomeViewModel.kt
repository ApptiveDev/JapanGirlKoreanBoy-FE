package com.apptive.japkor.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.api.MatchingService
import com.apptive.japkor.data.api.ServiceFactory
import com.apptive.japkor.data.model.MalePendingMatchingResponse
import com.apptive.japkor.data.model.MatchingResponse
import com.apptive.japkor.ui.components.ToastType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

sealed class HomeUiEvent {
    data class ShowToast(val message: String, val type: ToastType = ToastType.ERROR) : HomeUiEvent()
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val matchings: List<HomeMatching> = emptyList(),
    val selectedMatching: HomeMatching? = null,
    val isWaiting: Boolean = false,
    val aiSummaryKo: String? = null,
    val aiSummaryJa: String? = null,
    val isAiSummaryLoading: Boolean = false,
    val aiSummaryError: String? = null
)

class HomeViewModel(
    private val matchingService: MatchingService = ServiceFactory.matchingService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<HomeUiEvent> = _events.asSharedFlow()

    init {
        fetchAiSummary()
    }

    fun fetchMatchingsForGender(gender: String) {
        when {
            gender.isBlank() -> return
            gender == FEMALE_GENDER -> fetchFemaleMatchings()
            gender == MALE_GENDER -> fetchMalePendingMatchings()
        }
    }

    private fun fetchFemaleMatchings() {
        if (_uiState.value.isWaiting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                matchingService.getFemaleMatchings().awaitResponse()
            }.onSuccess { response ->
                Log.d(TAG, "getFemaleMatchings success=${response.isSuccessful} code=${response.code()}")
                if (response.isSuccessful) {
                    val data = response.body().orEmpty().map { it.toHomeMatching() }
                    if (data.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                matchings = emptyList(),
                                selectedMatching = null,
                                isWaiting = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                matchings = data,
                                selectedMatching = null,
                                isWaiting = false
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            matchings = emptyList(),
                            selectedMatching = null,
                            isWaiting = true
                        )
                    }
                }
            }.onFailure { throwable ->
                Log.e(TAG, "getFemaleMatchings failed", throwable)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchings = emptyList(),
                        selectedMatching = null,
                        isWaiting = true
                    )
                }
            }
        }
    }

    private fun fetchMalePendingMatchings() {
        if (_uiState.value.isWaiting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                matchingService.getMalePendingMatchings().awaitResponse()
            }.onSuccess { response ->
                Log.d(
                    TAG,
                    "getMalePendingMatchings success=${response.isSuccessful} code=${response.code()}"
                )
                if (response.isSuccessful) {
                    val body = response.body().orEmpty()
                    Log.d(TAG, "getMalePendingMatchings body=$body")
                    val data = body.map { it.toHomeMatching() }
                    if (data.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                matchings = emptyList(),
                                selectedMatching = null,
                                isWaiting = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                matchings = data,
                                selectedMatching = null,
                                isWaiting = false
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            matchings = emptyList(),
                            selectedMatching = null,
                            isWaiting = true
                        )
                    }
                }
            }.onFailure { throwable ->
                Log.e(TAG, "getMalePendingMatchings failed", throwable)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        matchings = emptyList(),
                        selectedMatching = null,
                        isWaiting = true
                    )
                }
            }
        }
    }

    fun showDetails(matching: HomeMatching) {
        _uiState.update { it.copy(selectedMatching = matching) }
    }

    fun hideDetails() {
        _uiState.update { it.copy(selectedMatching = null) }
    }

    fun selectMatching(matchingId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                matchingService.femaleSelectMatching(matchingId).awaitResponse()
            }.onSuccess { response ->
                Log.d(TAG, "femaleSelectMatching success=${response.isSuccessful} code=${response.code()}")
                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            matchings = emptyList(),
                            selectedMatching = null,
                            isWaiting = true
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.tryEmit(HomeUiEvent.ShowToast("매칭 선택에 실패했습니다."))
                }
            }.onFailure { throwable ->
                Log.e(TAG, "femaleSelectMatching failed", throwable)
                _uiState.update { it.copy(isLoading = false) }
                _events.tryEmit(HomeUiEvent.ShowToast("네트워크 오류로 매칭을 선택할 수 없습니다."))
            }
        }
    }

    fun noMatchSelected() {
        _uiState.update {
            it.copy(
                isLoading = false,
                matchings = emptyList(),
                selectedMatching = null,
                isWaiting = true
            )
        }
    }

    private fun fetchAiSummary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiSummaryLoading = true, aiSummaryError = null) }
            runCatching {
                matchingService.getMyAiSummary().awaitResponse()
            }.onSuccess { response ->
                Log.d(TAG, "getMyAiSummary success=${response.isSuccessful} code=${response.code()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d(
                        TAG,
                        "getMyAiSummary body memberId=${body?.memberId} name=${body?.name} " +
                            "aiSummaryKo=${body?.aiSummaryKo} aiSummaryJa=${body?.aiSummaryJa}"
                    )
                    val summaryKo = body?.aiSummaryKo?.trim()?.takeIf { it.isNotBlank() }
                    val summaryJa = body?.aiSummaryJa?.trim()?.takeIf { it.isNotBlank() }
                    if (summaryKo != null || summaryJa != null) {
                        _uiState.update {
                            it.copy(
                                aiSummaryKo = summaryKo,
                                aiSummaryJa = summaryJa,
                                isAiSummaryLoading = false,
                                aiSummaryError = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                aiSummaryKo = null,
                                aiSummaryJa = null,
                                isAiSummaryLoading = false,
                                aiSummaryError = "AI 요약본을 불러오지 못했습니다."
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            aiSummaryKo = null,
                            aiSummaryJa = null,
                            isAiSummaryLoading = false,
                            aiSummaryError = "AI 요약본을 불러오지 못했습니다."
                        )
                    }
                }
            }.onFailure { throwable ->
                Log.e(TAG, "getMyAiSummary failed", throwable)
                _uiState.update {
                    it.copy(
                        aiSummaryKo = null,
                        aiSummaryJa = null,
                        isAiSummaryLoading = false,
                        aiSummaryError = "AI 요약본을 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
        private const val FEMALE_GENDER = "JAPANESE_FEMALE"
        private const val MALE_GENDER = "KOREAN_MALE"
    }
}

private fun MatchingResponse.toHomeMatching(): HomeMatching {
    return HomeMatching(
        matchingId = matchingId,
        memberId = maleMemberId,
        name = maleName,
        email = maleEmail,
        height = height,
        weight = weight,
        residenceArea = residenceArea,
        matchingOrder = matchingOrder,
        status = status
    )
}

private fun MalePendingMatchingResponse.toHomeMatching(): HomeMatching {
    return HomeMatching(
        matchingId = matchingId,
        memberId = femaleMemberId,
        name = femaleName,
        email = femaleEmail,
        height = height,
        weight = weight,
        residenceArea = residenceArea,
        matchingOrder = null,
        status = status
    )
}
