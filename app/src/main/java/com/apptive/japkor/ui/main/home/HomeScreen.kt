package com.apptive.japkor.ui.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.apptive.japkor.data.model.HomeMatching
import com.apptive.japkor.ui.main.home.components.MatchingCarouselContent
import com.apptive.japkor.ui.main.home.components.MatchingDetailContent
import com.apptive.japkor.ui.main.home.components.WaitingContent

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    pagerState: PagerState,
    canSelectMatching: Boolean,
    onShowDetails: (HomeMatching) -> Unit,
    onNoMatch: () -> Unit,
    onConfirm: (Long) -> Unit,
    onAccept: (Long) -> Unit,
    onReject: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val matchings = uiState.matchings
    val selectedMatching = uiState.selectedMatching
    val counterpartLabel = if (canSelectMatching) "남성" else "여성"
    val matchingTitle = "매칭된 $counterpartLabel"

    Box(
        modifier = modifier
    ) {
        when {
            selectedMatching != null -> {
                MatchingDetailContent(
                    matching = selectedMatching,
                    canSelectMatching = canSelectMatching,
                    onConfirm = {
                        if (canSelectMatching) {
                            onConfirm(selectedMatching.matchingId)
                        }
                    },
                    onAccept = {
                        if (!canSelectMatching) {
                            onAccept(selectedMatching.matchingId)
                        }
                    },
                    onReject = {
                        if (!canSelectMatching) {
                            onReject(selectedMatching.matchingId)
                        }
                    }
                )
            }

            uiState.isWaiting || matchings.isEmpty() -> {
                WaitingContent(
                    modifier = Modifier.fillMaxSize(),
                    userName = uiState.userName,
                    isFemaleUser = canSelectMatching,
                    aiSummaryKo = uiState.aiSummaryKo,
                    aiSummaryJa = uiState.aiSummaryJa,
                    isAiSummaryLoading = uiState.isAiSummaryLoading,
                    aiSummaryError = uiState.aiSummaryError
                )
            }
            else -> {
                MatchingCarouselContent(
                    matchings = matchings,
                    pagerState = pagerState,
                    onShowDetails = onShowDetails,
                    onNoMatch = onNoMatch,
                    title = matchingTitle,
                    showNoMatchButton = canSelectMatching
                )
            }
        }
    }
}
