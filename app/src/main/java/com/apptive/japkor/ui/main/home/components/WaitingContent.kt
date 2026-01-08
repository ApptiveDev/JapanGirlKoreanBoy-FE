package com.apptive.japkor.ui.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
internal fun WaitingContent(
    modifier: Modifier = Modifier,
    userName: String?,
    isFemaleUser: Boolean,
    aiSummaryKo: String?,
    aiSummaryJa: String?,
    isAiSummaryLoading: Boolean,
    aiSummaryError: String?
) {
    val displayName = userName?.takeIf { it.isNotBlank() } ?: "회원"
    val summaryKo = aiSummaryKo?.takeIf { it.isNotBlank() }
    val summaryJa = aiSummaryJa?.takeIf { it.isNotBlank() }
    val displaySummaryKo = if (isFemaleUser) null else summaryKo
    val displaySummaryJa = if (isFemaleUser) summaryJa else null

    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            CustomText(
                text = displayName,
                type = CustomTextType.title,
                size = 24.sp
            )
            Spacer(Modifier.width(8.dp))
            CustomText(
                text = "님,",
                type = CustomTextType.label,
                size = 24.sp
            )
        }

        CustomText(
            text = "다른 매칭상대를 찾고있어요!",
            type = CustomTextType.label,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            size = 24.sp
        )

        Spacer(modifier = Modifier.height(44.dp))

        // ---- AI 요약 영역 (스샷처럼 스택 카드 느낌) ----
        when {
            displaySummaryKo != null || displaySummaryJa != null -> {
                AiSummaryStack(
                    modifier = Modifier.fillMaxWidth(),
                    summaryKo = displaySummaryKo,
                    summaryJa = displaySummaryJa
                )
            }

            isAiSummaryLoading -> {
                CustomText(
                    text = "AI 요약본을 불러오는 중입니다.",
                    type = CustomTextType.body,
                    color = CustomColor.gray400,
                    textAlign = TextAlign.Center
                )
            }

            !aiSummaryError.isNullOrBlank() -> {
                CustomText(
                    text = aiSummaryError,
                    type = CustomTextType.body,
                    color = CustomColor.gray400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun AiSummaryStack(
    modifier: Modifier = Modifier,
    summaryKo: String?,
    summaryJa: String?
) {
    val content = listOfNotNull(summaryKo, summaryJa).joinToString("\n\n")

    // 스샷처럼 뒤 카드가 살짝 삐져나오도록 2장의 카드로 표현
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = CustomColor.primary100),
                modifier = Modifier
                    .size(width = 250.dp, height = 300.dp)
                    .offset(x = 14.dp, y = 10.dp)
                    .border(
                        width = 1.dp,
                        color = CustomColor.primary200,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {}

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = CustomColor.primary100),

                modifier = Modifier
                    .size(width = 250.dp, height = 300.dp)
                    .border(
                        width = 1.dp,
                        color = CustomColor.primary200,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    // 본문 카드 영역
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CustomColor.white)
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomText(
                            text = content,
                            type = CustomTextType.mainRegular,
                            color = CustomColor.gray400,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 42.dp),
                            size= 18.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        CustomText(
            text = "평균 2~ 3일의 시간이 소요될 수 있습니다.\n" +
                    "조금만 기다려 주세요!",
            type = CustomTextType.body,
            color = CustomColor.gray300,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 42.dp),
            size= 16.sp
        )
    }
}
