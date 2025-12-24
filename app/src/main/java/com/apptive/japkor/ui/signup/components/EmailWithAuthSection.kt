package com.apptive.japkor.ui.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun EmailWithAuthSection(
    emailLocal: String,
    onEmailLocalChange: (String) -> Unit,
    emailDomain: String,
    onEmailDomainChange: (String) -> Unit,
    authCode: String,
    onAuthCodeChange: (String) -> Unit,
    canSendCode: Boolean,
    onClickSendCode: () -> Unit,
    showResend: Boolean,
    isResendEnabled: Boolean,
    remainingSeconds: Int,
    onClickResend: () -> Unit,
    canVerifyCode: Boolean,
    onClickVerify: () -> Unit,
    isEmailVerified: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 라벨
        CustomText(
            text = "이메일",
            type = CustomTextType.body,
            color = CustomColor.black,
            size = 15.sp
        )

        // 이메일 앞부분 + 도메인 (둘 다 직접 입력)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이메일 앞부분
            HalfCustomTextField(
                value = emailLocal,
                onValueChange = onEmailLocalChange,
                placeholder = "이메일",
                modifier = Modifier.weight(1f),
                enabled = !isEmailVerified,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            CustomText(
                text = " @ ",
                type = CustomTextType.body,
                color = CustomColor.gray300,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            // 도메인 직접 입력
            HalfCustomTextField(
                value = emailDomain,
                onValueChange = onEmailDomainChange,
                placeholder = "직접 입력",
                modifier = Modifier.weight(1f),
                enabled = !isEmailVerified,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }

        // 인증 코드 전송 버튼 (비활성/활성)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onClickSendCode,
                enabled = canSendCode && !showResend && !isEmailVerified,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF45C4A),
                    contentColor = CustomColor.white,
                    disabledContainerColor = CustomColor.gray300,
                    disabledContentColor = CustomColor.white
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                CustomText(
                    text = "인증 코드 전송",
                    type = CustomTextType.body,
                    size = 15.sp
                )
            }

            if (showResend) {
                Button(
                    onClick = onClickResend,
                    enabled = isResendEnabled && !isEmailVerified,
                    modifier = Modifier.height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF45C4A),
                        contentColor = CustomColor.white,
                        disabledContainerColor = CustomColor.gray300,
                        disabledContentColor = CustomColor.white
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    val timerText = if (!isEmailVerified && remainingSeconds > 0) {
                        formatRemainingSeconds(remainingSeconds)
                    } else {
                        null
                    }
                    CustomText(
                        text = timerText?.let { "재전송 ($it)" } ?: "재전송",
                        type = CustomTextType.body,
                        size = 13.sp
                    )
                }
            }
        }

        // 인증 코드 입력 + 필드 안쪽에 인증 버튼
        AuthCodeField(
            authCode = authCode,
            onAuthCodeChange = onAuthCodeChange,
            canVerifyCode = canVerifyCode,
            onClickVerify = onClickVerify,
            enabled = !isEmailVerified
        )
    }
}

private fun formatRemainingSeconds(seconds: Int): String {
    val minutes = seconds / 60
    val sec = seconds % 60
    return "%d:%02d".format(minutes, sec)
}
