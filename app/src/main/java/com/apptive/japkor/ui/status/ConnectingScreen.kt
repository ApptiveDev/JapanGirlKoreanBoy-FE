package com.apptive.japkor.ui.status

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ConnectingScreen(
    name: String,
    @DrawableRes logoRes: Int
) {
    StatusWaitingTemplate(
        name = name,
        title = "매칭 상대를 찾고 있어요!",
        subtitle = "조금만 더 기다려주세요",
        logoRes = logoRes,
        logoOffsetX = 150.dp,     // 오른쪽으로 밀기 (여기 값만 튜닝)
    )
}
