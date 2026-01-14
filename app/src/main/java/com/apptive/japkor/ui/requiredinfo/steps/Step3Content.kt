package com.apptive.japkor.ui.requiredinfo.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.components.OptionChip
import com.apptive.japkor.ui.requiredinfo.RequiredInfoViewModel
import com.apptive.japkor.ui.theme.CustomColor
import com.apptive.japkor.utils.required_info.RequiredInfoMapper

private val degreeOptions = listOf(
    "고등학교 졸업",
    "전문학사 (2년제 대학)",
    "학사 (4년제 대학)",
    "석사",
    "박사"
)

private val wealthOptions = listOf(
    "자산 1억 원 미만",
    "자산 1억 ~ 3억 원 사이",
    "자산 3억 ~ 5억 원 사이",
    "자산 5억 ~ 10억 원 사이",
    "자산 10억 원 초과"
)

@Composable
fun Step3Content(
    viewModel: RequiredInfoViewModel
) {
    val degree by viewModel.education.collectAsState()
    val wealth by viewModel.asset.collectAsState()
    val otherInfo by viewModel.otherInfo.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomText(
            text = "필수정보입력",
            type = CustomTextType.mainRegular,
            size = 32.sp
        )
        CustomText(
            text = "클릭하여 각 항목에 정보를 입력해주세요.",
            color = CustomColor.gray400,
            type = CustomTextType.mainRegular,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp))
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomText(
                    text = "학력",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )

                degreeOptions.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { option ->
                            val mapped = RequiredInfoMapper.education(option)
                            OptionChip(
                                text = option,
                                selected = degree == mapped,
                                onClick = { viewModel.setEducation(option) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                CustomText(
                    text = "최종 학력을 선택해 주세요.",
                    color = CustomColor.gray300,
                    type = CustomTextType.body,
                    modifier = Modifier.padding(horizontal = 7.dp),
                    size = 14.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomText(
                    text = "자산",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    wealthOptions.forEach { option ->
                        val mapped = RequiredInfoMapper.asset(option)
                        OptionChip(
                            text = option,
                            selected = wealth == mapped,
                            onClick = { viewModel.setAsset(option) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp)),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomText(
                text = "기타 자기 소개",
                type = CustomTextType.body,
                color = CustomColor.gray300,
                size = 12.sp
            )

            OutlinedTextField(
                value = otherInfo,
                onValueChange = {
                    if (it.length <= 300) {
                        viewModel.setOtherInfo(it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    CustomText(
                        text = "300자 이내로 자기소개를 작성해 주세요.",
                        type = CustomTextType.body,
                        color = CustomColor.gray300
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = CustomColor.gray200,
                    focusedBorderColor = CustomColor.gray300
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp
                ),
                maxLines = 6
            )

            CustomText(
                text = "${otherInfo.length}/300",
                type = CustomTextType.body,
                color = CustomColor.gray300,
                size = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
