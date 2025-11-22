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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

private val degreeOptions = listOf("고등학교 졸업", "전문학사(2년제 대학)", "학사(4년제 대학)", "석사", "박사")
private val wealthOptions = listOf(
    "자산 1억 원 미만",
    "자산 1억 ~ 3억 원 사이",
    "자산 3억 ~ 5억 원 사이",
    "자산 5억 ~ 10억 원 사이",
    "자산 10억 원 초과"
)
@Composable
fun Step3Content() {
    var degree by remember { mutableStateOf<String?>(null) }
    var wealth by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomText(
            text = "선택사항",
            type = CustomTextType.mainRegular,
            size = 32.sp
        )
        CustomText(
            text = "클릭하여 각 항목에 정보를 입력해주세요.",
            color = CustomColor.gray400,
            type = CustomTextType.mainRegular,
        )
        Spacer(modifier = Modifier.height(20.dp))

        // 학력 / 재산
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 학력
            Column {
                CustomText(
                    text = "학력(인증)",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 17.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                val highSchool = degreeOptions.first()
                val degreeRows = degreeOptions.drop(1).chunked(2)

                // 칩들만 묶는 Column: 모두 같은 간격 12dp
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    // 1행: 고등학교 졸업만 왼쪽에, 오른쪽은 비움
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OptionChip(
                            text = highSchool,
                            selected = degree == highSchool,
                            onClick = {
                                degree = if (degree == highSchool) null else highSchool
                            },
                            modifier = Modifier.weight(1f)
                        )
                        // 오른쪽 빈 칸
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // 2~3행: 나머지 4개
                    degreeRows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { option ->
                                OptionChip(
                                    text = option,
                                    selected = degree == option,
                                    onClick = {
                                        degree = if (degree == option) null else option
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
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

            Spacer(modifier = Modifier.height(20.dp))

            // 자산
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                CustomText(
                    text = "자산(인증)",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 17.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    wealthOptions.forEach { option ->
                        OptionChip(
                            text = option,
                            selected = wealth == option,
                            onClick = {
                                wealth = if (wealth == option) null else option
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomText(
            text = "'학력' 및 '재산' 정보는 선택 입력 사항입니다.\n입력시, 더 빠르고 정확한 매칭에 도움을 줄 수 있습니다.",
            color = CustomColor.gray300,
            type = CustomTextType.body,
            modifier = Modifier.padding(horizontal = 7.dp),
            size = 14.sp
        )
    }
}