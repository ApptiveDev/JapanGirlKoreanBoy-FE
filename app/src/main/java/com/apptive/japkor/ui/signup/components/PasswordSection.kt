package com.apptive.japkor.ui.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomOutlinedTextField
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun PasswordSection(
    password: String,
    passwordConfirm: String,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    isPasswordMismatch: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomText(
            text = "비밀번호",
            type = CustomTextType.body,
            color = CustomColor.black,
            size = 15.sp
        )
        CustomOutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "비밀번호",
            isPassword = true
        )
        CustomOutlinedTextField(
            value = passwordConfirm,
            onValueChange = onPasswordConfirmChange,
            placeholder = "비밀번호 확인",
            isPassword = true
        )

        // 비밀번호 불일치 에러 메시지
        if (isPasswordMismatch) {
            CustomText(
                text = "비밀번호가 일치하지 않습니다.",
                color = Color(0xFFF45C4A),
                type = CustomTextType.body,
                modifier = Modifier.padding(horizontal = 4.dp),
                size = 13.sp
            )
        }

        CustomText(
            text = "8 ~ 16자 이내 영문, 특수 문자 조합",
            color = CustomColor.gray300,
            type = CustomTextType.body,
            modifier = Modifier.padding(horizontal = 4.dp),
            size = 14.sp
        )
    }
}
