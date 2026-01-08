package com.apptive.japkor.ui.main.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
internal fun WaitingContent(
    modifier: Modifier = Modifier,
    aiSummaryKo: String?,
    aiSummaryJa: String?,
    isAiSummaryLoading: Boolean,
    aiSummaryError: String?
) {
    val summaryKo = aiSummaryKo?.takeIf { it.isNotBlank() }
    val summaryJa = aiSummaryJa?.takeIf { it.isNotBlank() }
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
        if (summaryKo != null || summaryJa != null) {
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
                    if (summaryKo != null) {
                        CustomText(
                            text = "한국어",
                            type = CustomTextType.label,
                            color = CustomColor.gray400
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CustomText(
                            text = summaryKo,
                            type = CustomTextType.body,
                            color = CustomColor.black,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (summaryKo != null && summaryJa != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    if (summaryJa != null) {
                        CustomText(
                            text = "일본어",
                            type = CustomTextType.label,
                            color = CustomColor.gray400
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CustomText(
                            text = summaryJa,
                            type = CustomTextType.body,
                            color = CustomColor.black,
                            textAlign = TextAlign.Center
                        )
                    }
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
