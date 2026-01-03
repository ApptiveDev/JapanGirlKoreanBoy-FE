package com.apptive.japkor.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.api.MatchingService
import com.apptive.japkor.data.api.ServiceFactory
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
    val matchings: List<MatchingResponse> = emptyList(),
    val selectedMatching: MatchingResponse? = null,
    val isWaiting: Boolean = false
)

class HomeViewModel(
    private val matchingService: MatchingService = ServiceFactory.matchingService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<HomeUiEvent> = _events.asSharedFlow()

    init {
        fetchFemaleMatchings()
    }

    fun fetchFemaleMatchings() {
        if (_uiState.value.isWaiting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                matchingService.getFemaleMatchings().awaitResponse()
            }.onSuccess { response ->
                Log.d(TAG, "getFemaleMatchings success=${response.isSuccessful} code=${response.code()}")
                if (response.isSuccessful) {
                    val data = response.body().orEmpty()
                    if (data.isEmpty()) {
                        _uiState.value = HomeUiState(isWaiting = true)
                    } else {
                        _uiState.value = HomeUiState(matchings = data)
                    }
                } else {
                    _uiState.value = HomeUiState(isWaiting = true)
                    _events.tryEmit(HomeUiEvent.ShowToast("매칭 목록을 불러오지 못했습니다."))
                }
            }.onFailure { throwable ->
                Log.e(TAG, "getFemaleMatchings failed", throwable)
                _uiState.value = HomeUiState(isWaiting = true)
                _events.tryEmit(HomeUiEvent.ShowToast("네트워크 오류로 매칭을 불러올 수 없습니다."))
            }
        }
    }

    fun showDetails(matching: MatchingResponse) {
        _uiState.update { it.copy(selectedMatching = matching) }
    }

    fun hideDetails() {
        _uiState.update { it.copy(selectedMatching = null) }
    }

    fun selectMatching(matchingId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                matchingService.selectMatching(matchingId).awaitResponse()
            }.onSuccess { response ->
                Log.d(TAG, "selectMatching success=${response.isSuccessful} code=${response.code()}")
                if (response.isSuccessful) {
                    _uiState.value = HomeUiState(isWaiting = true)
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.tryEmit(HomeUiEvent.ShowToast("매칭 선택에 실패했습니다."))
                }
            }.onFailure { throwable ->
                Log.e(TAG, "selectMatching failed", throwable)
                _uiState.update { it.copy(isLoading = false) }
                _events.tryEmit(HomeUiEvent.ShowToast("네트워크 오류로 매칭을 선택할 수 없습니다."))
            }
        }
    }

    fun noMatchSelected() {
        _uiState.value = HomeUiState(isWaiting = true)
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
