package com.apptive.japkor.ui.requiredinfo.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.requiredinfo.RequiredInfoViewModel
import com.apptive.japkor.ui.theme.CustomColor
import com.apptive.japkor.utils.required_info.GenderMapper

@Composable
fun Step1Content(viewModel: RequiredInfoViewModel = viewModel()) {

    val selectedGender by viewModel.gender.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomText(
            text = "성별을 설정해주세요",
            type = CustomTextType.mainRegular,
            size = 32.sp
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {
                listOf("한국 남성", "일본 여성").forEach { option ->
                    val isSelected = selectedGender == option

                    Button(
                        onClick = {
                            val serverValue = GenderMapper.toServerValue(option)
                            if (serverValue != null) viewModel.setGender(serverValue)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) CustomColor.gray300 else CustomColor.gray100
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        CustomText(
                            text = option,
                            color = if (isSelected) Color.White else CustomColor.black,
                            type = CustomTextType.mainRegular,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        if (selectedGender != null) {
            val koreanLabel = when (selectedGender) {
                "KOREAN_MALE" -> "한국 남성"
                "JAPANESE_FEMALE" -> "일본 여성"
                else -> ""
            }

            CustomText(
                text = "'$koreanLabel'를 선택하셨습니다.\n\n프로필이 반대 성별에게 먼저 전달됩니다.",
                color = CustomColor.gray300,
                type = CustomTextType.body,
                size = 14.sp
            )
        }
    }
}
