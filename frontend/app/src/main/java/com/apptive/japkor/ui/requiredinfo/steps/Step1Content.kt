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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.requiredinfo.RequiredInfoViewModel
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun Step1Content(
    selectedOption: MutableState<String>,
    viewModel: RequiredInfoViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LaunchedEffect(Unit) {
            if (viewModel.gender.value == null) {
                viewModel.setGender(selectedOption.value)
            }
        }
        CustomText(
            text = "성별을 설정해주세요",
            type = CustomTextType.mainRegular,
            size = 32.sp
        )
        CustomText(
            text = "한국인 남성, 일본인 여성 중 선택가능합니다.",
            color = CustomColor.gray400,
            type = CustomTextType.mainRegular,
        )
        Spacer(modifier = Modifier.height(50.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomText(
                text = "저는",
                color = CustomColor.gray400,
                type = CustomTextType.mainRegular,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                listOf("한국 남성", "일본 여성").forEach { option ->
                    val isSelected = selectedOption.value == option

                    Button(
                        onClick = {
                            selectedOption.value = option
                            viewModel.setGender(option)
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
            Spacer(modifier = Modifier.width(10.dp))
            CustomText(
                text = "입니다.",
                color = CustomColor.gray400,
                type = CustomTextType.mainRegular,
            )
        }
        Spacer(modifier = Modifier.height(80.dp))
        val selectedText = if (selectedOption.value == "한국 남성") {
            "'한국 남성'를 선택하셨습니다.\n\n'한국 남성'를 선택하는 경우,\n'일본 여성'에게 프로필이 먼저 전달됩니다."
        } else {
            "'일본 여성'를 선택하셨습니다.\n\n'일본 여성'를 선택하는 경우,\n'한국 남성'에게 프로필이 먼저 전달됩니다."
        }
        CustomText(
            text = selectedText,
            color = CustomColor.gray300,
            type = CustomTextType.body,
            size = 14.sp
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}
