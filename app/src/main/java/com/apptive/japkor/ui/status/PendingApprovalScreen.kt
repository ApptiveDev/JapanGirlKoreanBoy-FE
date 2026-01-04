package com.apptive.japkor.ui.status

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun PendingApprovalScreen(
    name: String,
    @DrawableRes logoRes: Int
) {
    StatusWaitingTemplate(
        name = name,
        title = "프로필을 심사 중이에요!",
        subtitle = "조금만 더 기다려주세요",
        logoRes = logoRes,
        logoOffsetX = (-150).dp   // 왼쪽으로 밀기 (여기 값만 튜닝)
    )
}
