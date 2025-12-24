package com.apptive.japkor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun LoadingDialog() {
    val dialogShape = RoundedCornerShape(24.dp)

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .shadow(
                        elevation = 16.dp,
                        shape = dialogShape,
                        clip = false
                    )
                    .clip(dialogShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFFBF8),
                                Color(0xFFF5E9E0)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE7D6C9),
                        shape = dialogShape
                    )
                    .padding(horizontal = 24.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                LoveThreadIndicator()
                CustomText(
                    text = "인연의 실을\n 곱게 잇는 중이에요",
                    type = CustomTextType.mainBold,
                    color = Color(0xFF4C3B30)
                )
                CustomText(
                    text = "두 마음이 예쁘게 묶이도록\n 조금만 기다려주세요",
                    type = CustomTextType.body,
                    color = CustomColor.gray400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LoveThreadIndicator() {
    val blush = Color(0xFFE7B7C5)
    val blushTrack = Color(0xFFF4E7DE)

    Box(
        modifier = Modifier.size(96.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(72.dp),
            strokeWidth = 6.dp,
            color = blush,
            trackColor = blushTrack
        )
    }
}
