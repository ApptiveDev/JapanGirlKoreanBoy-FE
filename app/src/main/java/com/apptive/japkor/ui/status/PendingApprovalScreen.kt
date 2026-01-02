package com.apptive.japkor.ui.status

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable

@Composable
fun PendingApprovalScreen(
    name: String,
    @DrawableRes logoRes: Int
) {
    StatusWaitingTemplate(
        name = name,
        title = "프로필을 심사 중이에요!",
        subtitle = "조금만 더 기다려주세요",
        logoRes = logoRes
    )
}
