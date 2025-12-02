package com.apptive.japkor.ui.signup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun AuthCodeField(
    authCode: String,
    onAuthCodeChange: (String) -> Unit,
    canVerifyCode: Boolean,
    onClickVerify: () -> Unit,
    enabled: Boolean = true
) {
    val outerShape = RoundedCornerShape(16.dp)
    var isFocused by remember { mutableStateOf(false) }
    val borderWidth = if (enabled && isFocused) 2.dp else 1.dp
    val borderColor = when {
        !enabled -> CustomColor.gray200
        isFocused -> CustomColor.gray300
        else -> CustomColor.gray200
    }
    val backgroundColor = if (enabled) Color.White else CustomColor.gray100
    val textColor = if (enabled) CustomColor.black else CustomColor.gray300

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(borderWidth, borderColor, outerShape)
            .background(backgroundColor, outerShape)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 코드 입력 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (authCode.isEmpty()) {
                    CustomText(
                        text = "인증 코드",
                        type = CustomTextType.body,
                        color = CustomColor.gray300,
                        size = 14.sp
                    )
                }
                BasicTextField(
                    value = authCode,
                    onValueChange = onAuthCodeChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = textColor,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = enabled && focusState.isFocused
                        },
                    enabled = enabled
                )
            }

            // 필드 안에 들어가는 작은 인증 버튼
            Button(
                onClick = onClickVerify,
                enabled = canVerifyCode && enabled,
                modifier = Modifier.height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF45C4A),
                    contentColor = Color.White,
                    disabledContainerColor = CustomColor.gray300,
                    disabledContentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                CustomText(
                    text = "인증",
                    type = CustomTextType.body,
                    size = 12.sp
                )
            }
        }
    }
}
