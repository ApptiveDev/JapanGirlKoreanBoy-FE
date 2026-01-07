package com.apptive.japkor.ui.status

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.model.UserStatus
import com.apptive.japkor.data.repository.StatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PendingApprovalUiState(
    val isLoading: Boolean = false,
    val status: UserStatus? = null,
    val unauthorized: Boolean = false,
    val error: String? = null
)

class PendingApprovalViewModel(
    private val dataStore: DataStoreManager,
    private val repo: StatusRepository = StatusRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PendingApprovalUiState())
    val uiState: StateFlow<PendingApprovalUiState> = _uiState


    fun refreshStatus() {
        Log.d("STATUS_DEBUG", "refreshStatus called")

        viewModelScope.launch {
            try {
                val res = repo.getMyStatus()
                Log.d("STATUS_DEBUG", "status api success: ${res.status}")

                dataStore.saveUserName(res.name)
                dataStore.saveUserStatus(res.status.name)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    status = res.status
                )
            } catch (e: Exception) {
                Log.e("STATUS_DEBUG", "status api failed", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

}
