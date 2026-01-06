package com.apptive.japkor.ui.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.apptive.japkor.R
import com.apptive.japkor.data.model.MatchingResponse
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    pagerState: PagerState,
    canSelectMatching: Boolean,
    onShowDetails: (MatchingResponse) -> Unit,
    onNoMatch: () -> Unit,
    onConfirm: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val matchings = uiState.matchings
    val selectedMatching = uiState.selectedMatching

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
                    }
                )
            }
            uiState.isWaiting || matchings.isEmpty() -> {
                WaitingContent(
                    modifier = Modifier.fillMaxSize(),
                    aiSummary = uiState.aiSummary,
                    isAiSummaryLoading = uiState.isAiSummaryLoading,
                    aiSummaryError = uiState.aiSummaryError
                )
            }
            else -> {
                MatchingCarouselContent(
                    matchings = matchings,
                    pagerState = pagerState,
                    onShowDetails = onShowDetails,
                    onNoMatch = onNoMatch
                )
            }
        }
    }
}

@Composable
private fun WaitingContent(
    modifier: Modifier = Modifier,
    aiSummary: String?,
    isAiSummaryLoading: Boolean,
    aiSummaryError: String?
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomText(
            text = "매칭 진행 중입니다..",
            type = CustomTextType.body,
            color = CustomColor.gray400
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (!aiSummary.isNullOrBlank()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CustomColor.gray100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomText(
                        text = "내 AI 요약본",
                        type = CustomTextType.label,
                        color = CustomColor.gray400
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomText(
                        text = aiSummary,
                        type = CustomTextType.body,
                        color = CustomColor.black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (isAiSummaryLoading) {
            CustomText(
                text = "AI 요약본을 불러오는 중입니다.",
                type = CustomTextType.body,
                color = CustomColor.gray400,
                textAlign = TextAlign.Center
            )
        } else if (!aiSummaryError.isNullOrBlank()) {
            CustomText(
                text = aiSummaryError,
                type = CustomTextType.body,
                color = CustomColor.gray400,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun MatchingCarouselContent(
    matchings: List<MatchingResponse>,
    pagerState: PagerState,
    onShowDetails: (MatchingResponse) -> Unit,
    onNoMatch: () -> Unit
) {
    val currentMatching = matchings.getOrNull(pagerState.currentPage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText(
            text = "매칭된 남성",
            type = CustomTextType.title,
            color = CustomColor.black
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
        ) { page ->
            val matching = matchings[page]
            val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            ).absoluteValue
            val scale = lerp(0.92f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

            MatchingCard(
                matching = matching,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        PagerIndicator(
            total = matchings.size,
            current = pagerState.currentPage
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { currentMatching?.let { onShowDetails(it) } },
            enabled = currentMatching != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomColor.primary600,
                disabledContainerColor = CustomColor.primary300
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            CustomText(
                text = "이 분 프로필이 궁금해요",
                type = CustomTextType.body,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onNoMatch,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomColor.gray100
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            CustomText(
                text = "마음에 드는 상대가 없어요",
                type = CustomTextType.body,
                color = CustomColor.black
            )
        }
    }
}

@Composable
private fun MatchingCard(
    matching: MatchingResponse,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CustomColor.gray100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(CustomColor.primary100, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "프로필 이미지",
                    tint = CustomColor.primary600,
                    modifier = Modifier.size(72.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            CustomText(
                text = matching.maleName,
                type = CustomTextType.headline,
                color = CustomColor.black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomText(
                text = "프로필을 확인해보세요",
                type = CustomTextType.body,
                color = CustomColor.gray400
            )
        }
    }
}

@Composable
private fun PagerIndicator(total: Int, current: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(total) { index ->
            val color = if (index == current) CustomColor.primary600 else CustomColor.gray200
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            if (index != total - 1) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
private fun MatchingDetailContent(
    matching: MatchingResponse,
    canSelectMatching: Boolean,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileHeader(matching = matching)
            }
            item {
                DetailCard(matching = matching)
            }
        }
        Button(
            onClick = onConfirm,
            enabled = canSelectMatching,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomColor.primary600,
                disabledContainerColor = CustomColor.primary300
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            CustomText(
                text = "마음에 들어요 매칭해주세요",
                type = CustomTextType.body,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ProfileHeader(matching: MatchingResponse) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(CustomColor.primary100, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_user),
                contentDescription = "프로필 이미지",
                tint = CustomColor.primary600,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            CustomText(
                text = matching.maleName,
                type = CustomTextType.headline,
                color = CustomColor.black
            )
            CustomText(
                text = matching.maleEmail,
                type = CustomTextType.body,
                color = CustomColor.gray400
            )
        }
    }
}

@Composable
private fun DetailCard(matching: MatchingResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CustomColor.gray100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomText(
                text = "상세 정보",
                type = CustomTextType.title,
                color = CustomColor.black
            )
            DetailItem(label = "매칭 ID", value = matching.matchingId.toString())
            DetailItem(label = "남성 회원 ID", value = matching.maleMemberId.toString())
            DetailItem(label = "이름", value = matching.maleName)
            DetailItem(label = "이메일", value = matching.maleEmail)
            DetailItem(label = "키", value = formatHeight(matching.height))
            DetailItem(label = "몸무게", value = formatWeight(matching.weight))
            DetailItem(label = "거주지역", value = formatText(matching.residenceArea))
            DetailItem(label = "매칭 순서", value = matching.matchingOrder.toString())
            DetailItem(label = "상태", value = matching.status)
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomText(
            text = label,
            type = CustomTextType.label,
            color = CustomColor.gray400
        )
        CustomText(
            text = value,
            type = CustomTextType.body,
            color = CustomColor.black
        )
    }
}

private fun formatHeight(value: Int?): String {
    return value?.let { "${it}cm" } ?: "미입력"
}

private fun formatWeight(value: Int?): String {
    return value?.let { "${it}kg" } ?: "미입력"
}

private fun formatText(value: String?): String {
    return value?.takeIf { it.isNotBlank() } ?: "미입력"
}
