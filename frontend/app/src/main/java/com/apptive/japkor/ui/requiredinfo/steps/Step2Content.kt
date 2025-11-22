package com.apptive.japkor.ui.requiredinfo.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomOutlinedTextField
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun Step2Content() {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }

    val smokingOptions = listOf("O", "X")
    var smoking by remember { mutableStateOf("X") }

    val drinkOptions = listOf("주 1회 미만", "주 1회", "주 2회", "주 3회 이상")
    var drink by remember { mutableStateOf("주 1회") }

    val religionOptions = listOf("무교", "불교", "기독교", "천주교", "기타")
    var religion by remember { mutableStateOf("무교") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
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

        // 키 / 몸무게 / 거주 지역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomOutlinedTextField(
                value = height,
                onValueChange = { height = it },
                placeholder = "키"
            )
            CustomOutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                placeholder = "몸무게"
            )
            CustomOutlinedTextField(
                value = region,
                onValueChange = { region = it },
                placeholder = "거주 지역"
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // 흡연 / 음주 / 종교 선택
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 흡연
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
                            selected = smoking == option,
                            onClick = { smoking = option },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

            }

            // 음주 빈도 (2x2)
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
                                    selected = drink == option,
                                    onClick = { drink = option },
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

            // 종교: 앞의 4개는 2x2, 마지막 '기타'는 전체 너비
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomText(
                    text = "종교",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )

                val firstFour = religionOptions.take(4)
                val last = religionOptions.drop(4)

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val rows = firstFour.chunked(2)
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { option ->
                                OptionChip(
                                    text = option,
                                    selected = religion == option,
                                    onClick = { religion = option },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    // 기타: 전체 너비
                    if (last.isNotEmpty()) {
                        //Spacer(modifier = Modifier.height(8.dp))
                        OptionChip(
                            text = last[0],
                            selected = religion == last[0],
                            onClick = { religion = last[0] },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        CustomText(
            text = "거짓 정보 입력 시 서비스 이용이 제한될 수 있습니다.",
            color = CustomColor.gray300,
            type = CustomTextType.body,
            modifier = Modifier.padding(horizontal = 7.dp),
            size = 14.sp
        )
    }
}

@Composable
fun OptionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)

    val background = if (selected) CustomColor.gray400 else CustomColor.white
    val textColor = if (selected) CustomColor.white else CustomColor.gray300
    val borderColor = if (selected) CustomColor.gray300 else CustomColor.gray200

    Surface(
        modifier = modifier
            .height(44.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clip(shape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        color = background,
        shape = shape,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            CustomText(
                text = text,
                type = CustomTextType.body,
                size = 14.sp,
                color = textColor
            )
        }
    }
}
