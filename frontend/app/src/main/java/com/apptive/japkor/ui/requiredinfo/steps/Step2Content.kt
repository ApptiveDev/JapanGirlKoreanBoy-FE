package com.apptive.japkor.ui.requiredinfo.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomOutlinedTextField
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor
import com.apptive.japkor.ui.components.OptionChip
import com.apptive.japkor.ui.requiredinfo.RequiredInfoViewModel
import com.apptive.japkor.utils.required_info.RequiredInfoMapper

@Composable
fun Step2Content(
    viewModel: RequiredInfoViewModel
) {
    val height by viewModel.height.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val region by viewModel.region.collectAsState()
    val smoking by viewModel.smoking.collectAsState()
    val drink by viewModel.drinking.collectAsState()
    val religion by viewModel.religion.collectAsState()

    val smokingOptions = listOf("흡연", "비흡연")
    val drinkOptions = listOf("주 1회 미만", "주 1회", "주 2회", "주 3회 이상")
    val religionOptions = listOf("무교", "불교", "기독교", "천주교", "신토", "기타")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .imePadding(),
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

        Spacer(modifier = Modifier.height(5.dp))

        // ============================
        //  키 / 몸무게 / 거주 지역 입력
        // ============================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 키 입력 (Int?)
            CustomOutlinedTextField(
                value = height?.toString() ?: "",
                onValueChange = {
                    val num = it.toIntOrNull()
                    viewModel.setHeight(num)
                },
                placeholder = "키(cm)",
                isNumberOnly = true
            )

            // 몸무게 입력 (Int?)
            CustomOutlinedTextField(
                value = weight?.toString() ?: "",
                onValueChange = {
                    val num = it.toIntOrNull()
                    viewModel.setWeight(num)
                },
                placeholder = "몸무게(kg)",
                isNumberOnly = true
            )

            // 지역 입력 (String)
            CustomOutlinedTextField(
                value = region,
                onValueChange = { viewModel.setRegion(it) },
                placeholder = "거주 지역"
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // ============================
        //  흡연 / 음주 / 종교 선택
        // ============================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ---- 흡연 여부 ----
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomText(
                    text = "흡연",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    smokingOptions.forEach { option ->
                        OptionChip(
                            text = option,
                            selected = smoking == RequiredInfoMapper.smoking(option),
                            onClick = { viewModel.setSmoking(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // ---- 음주 빈도 ----
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomText(
                    text = "음주 빈도",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )

                val drinkRows = drinkOptions.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    drinkRows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { option ->
                                OptionChip(
                                    text = option,
                                    selected = drink == RequiredInfoMapper.drinking(option),
                                    onClick = { viewModel.setDrinking(option) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // ---- 종교 ----
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomText(
                    text = "종교",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    religionOptions.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { option ->
                                OptionChip(
                                    text = option,
                                    selected = religion == RequiredInfoMapper.religion(option),
                                    onClick = { viewModel.setReligion(option) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // 경고 문구
        CustomText(
            text = "거짓 정보 입력 시 서비스 이용이 제한될 수 있습니다.",
            color = CustomColor.gray300,
            type = CustomTextType.body,
            modifier = Modifier.padding(horizontal = 7.dp),
            size = 14.sp
        )
    }
}
