package com.apptive.japkor.ui.requiredinfo.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.components.OptionChip
import com.apptive.japkor.ui.requiredinfo.RequiredInfoViewModel
import com.apptive.japkor.ui.theme.CustomColor
import com.apptive.japkor.utils.required_info.RequiredInfoMapper
import kotlin.math.abs
import kotlin.math.roundToInt

private val avoidReligionOptions = listOf("없음", "불교", "기독교", "천주교", "신토", "기타")
private val preferredEducationOptions = listOf(
    "상위권",
    "명문",
    "중상위권 (국공립 사립대)",
    "실무형 (예술, 전문대)"
)
private val appearanceOptions = listOf(
    "연예인 느낌의 비주얼",
    "자연스럽고 호감 가는 인상",
    "개성이 뚜렷하고 자기 스타일이 있는 사람"
)
private val parentAssetOptions = listOf(
    "상속받을 자산이 1억 이상 있어야 해요",
    "노후관리만 되어 있으면 돼요",
    "상관없어요"
)
private val jobOptions = listOf("의사", "교사", "엔지니어", "프리랜서", "무직")
private val priorityOptions = listOf("직업", "학력", "키", "외모", "부모 자산", "본인 자산", "종교", "성격")

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Step5Content(
    viewModel: RequiredInfoViewModel
) {
    val preferredHeightMin by viewModel.preferredHeightMin.collectAsState()
    val preferredHeightMax by viewModel.preferredHeightMax.collectAsState()
    val avoidReligions by viewModel.avoidReligions.collectAsState()
    val preferredEducationLevel by viewModel.preferredEducationLevel.collectAsState()
    val preferredAppearanceStyle by viewModel.preferredAppearanceStyle.collectAsState()
    val parentAssetRequirement by viewModel.parentAssetRequirement.collectAsState()
    val preferredAssetMin by viewModel.preferredAssetMin.collectAsState()
    val preferredAssetMax by viewModel.preferredAssetMax.collectAsState()
    val preferredJobs by viewModel.preferredJobs.collectAsState()
    val avoidedJobs by viewModel.avoidedJobs.collectAsState()
    val mbti1 by viewModel.mbti1.collectAsState()
    val mbti2 by viewModel.mbti2.collectAsState()
    val mbti3 by viewModel.mbti3.collectAsState()
    val mbti4 by viewModel.mbti4.collectAsState()
    val priority1 by viewModel.priority1.collectAsState()
    val priority2 by viewModel.priority2.collectAsState()
    val priority3 by viewModel.priority3.collectAsState()

    LaunchedEffect(Unit) {
        if (viewModel.preferredHeightMin.value == null) viewModel.setPreferredHeightMin(160)
        if (viewModel.preferredHeightMax.value == null) viewModel.setPreferredHeightMax(180)
        if (viewModel.mbti1.value == null) viewModel.setMbti1("X")
        if (viewModel.mbti2.value == null) viewModel.setMbti2("X")
        if (viewModel.mbti3.value == null) viewModel.setMbti3("X")
        if (viewModel.mbti4.value == null) viewModel.setMbti4("X")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomText(
            text = "선호조건입력",
            type = CustomTextType.mainRegular,
            size = 32.sp
        )
        CustomText(
            text = "선호 키, 조건, 직업, 우선순위를 모두 입력해주세요.",
            color = CustomColor.gray400,
            type = CustomTextType.mainRegular,
        )

        // 선호 키
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp))
                .padding( vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = "선호 키",
                    type = CustomTextType.mainBold,
                    size = 16.sp
                )
                CustomText(
                    text = "${preferredHeightMin ?: 160}cm ~ ${preferredHeightMax ?: 180}cm",
                    type = CustomTextType.mainBold,
                    size = 14.sp,
                    color = CustomColor.gray300
                )
            }

            val sliderRange = (preferredHeightMin?.toFloat() ?: 160f)..(preferredHeightMax?.toFloat() ?: 180f)
            RangeSlider(
                value = sliderRange,
                onValueChange = { range ->
                    val start = range.start.roundToInt().coerceIn(130, 230)
                    val end = range.endInclusive.roundToInt().coerceIn(130, 230)
                    viewModel.setPreferredHeightMin(start)
                    viewModel.setPreferredHeightMax(end)
                },
                valueRange = 130f..230f,
                steps = 99,
                colors = SliderDefaults.colors(
                    thumbColor = CustomColor.primary600,
                    activeTrackColor = CustomColor.primary500,
                    activeTickColor= CustomColor.primary500,
                    inactiveTrackColor = CustomColor.primary300,
                    inactiveTickColor = CustomColor.primary600

                )
            )

//            thumbColor: Color = Color.Unspecified,
//            activeTrackColor: Color = Color.Unspecified,
//            activeTickColor: Color = Color.Unspecified,
//            inactiveTrackColor: Color = Color.Unspecified,
//            inactiveTickColor: Color = Color.Unspecified,
//            disabledThumbColor: Color = Color.Unspecified,
//            disabledActiveTrackColor: Color = Color.Unspecified,
//            disabledActiveTickColor: Color = Color.Unspecified,
//            disabledInactiveTrackColor: Color = Color.Unspecified,
//            disabledInactiveTickColor: Color = Color.Unspecified

            CustomText(
                text = "130~230cm 범위에서 설정해주세요.",
                type = CustomTextType.body,
                color = CustomColor.gray300,
                size = 12.sp
            )
        }

        // 종교/학벌/외모/부모님 자산
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp))
                .padding( vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomText(
                    text = "기피 종교",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    avoidReligionOptions.forEach { option ->
                        val mapped = RequiredInfoMapper.religion(option)
                        OptionChip(
                            text = option,
                            selected = avoidReligions.contains(mapped),
                            onClick = { viewModel.toggleAvoidReligion(option) }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomText(
                    text = "선호 학벌",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    preferredEducationOptions.forEach { option ->
                        val mapped = RequiredInfoMapper.preferredEducation(option)
                        OptionChip(
                            text = option,
                            selected = preferredEducationLevel == mapped,
                            onClick = { viewModel.setPreferredEducationLevel(option) }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomText(
                    text = "선호 외모 스타일",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    appearanceOptions.forEach { option ->
                        val mapped = RequiredInfoMapper.appearanceStyle(option)
                        OptionChip(
                            text = option,
                            selected = preferredAppearanceStyle == mapped,
                            onClick = { viewModel.setPreferredAppearanceStyle(option) }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomText(
                    text = "부모님 자산 요구사항",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    parentAssetOptions.forEach { option ->
                        val mapped = RequiredInfoMapper.parentAssetRequirement(option)
                        OptionChip(
                            text = option,
                            selected = parentAssetRequirement == mapped,
                            onClick = { viewModel.setParentAssetRequirement(option) }
                        )
                    }
                }
            }
        }

        // 선호 자산
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp))
                .padding( vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomText(
                text = "선호 자산 (원 단위)",
                type = CustomTextType.body,
                color = CustomColor.gray300,
                size = 12.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = preferredAssetMin?.toString() ?: "",
                    onValueChange = { value ->
                        viewModel.setPreferredAssetMin(value.filter { it.isDigit() }.toLongOrNull())
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    placeholder = {
                        CustomText(
                            text = "최소 자산",
                            type = CustomTextType.body,
                            color = CustomColor.gray300
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = CustomColor.gray200,
                        focusedBorderColor = CustomColor.gray300
                    ),
                    textStyle = TextStyle(fontSize = 14.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = preferredAssetMax?.toString() ?: "",
                    onValueChange = { value ->
                        viewModel.setPreferredAssetMax(value.filter { it.isDigit() }.toLongOrNull())
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    placeholder = {
                        CustomText(
                            text = "최대 자산",
                            type = CustomTextType.body,
                            color = CustomColor.gray300
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = CustomColor.gray200,
                        focusedBorderColor = CustomColor.gray300
                    ),
                    textStyle = TextStyle(fontSize = 14.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        // 직업
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp))
                .padding( vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomText(
                        text = "선호 직업",
                        type = CustomTextType.mainBold,
                        size = 16.sp
                    )
                    CustomText(
                        text = "(최대 3개)",
                        type = CustomTextType.body,
                        size = 12.sp,
                        color = CustomColor.gray300
                    )
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    jobOptions.forEach { option ->
                        val mapped = RequiredInfoMapper.job(option)
                        OptionChip(
                            text = option,
                            selected = preferredJobs.contains(mapped),
                            onClick = { viewModel.togglePreferredJob(option) }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomText(
                        text = "비선호 직업",
                        type = CustomTextType.mainBold,
                        size = 16.sp
                    )
                    CustomText(
                        text = "(최대 3개)",
                        type = CustomTextType.body,
                        size = 12.sp,
                        color = CustomColor.gray300
                    )
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    jobOptions.forEach { option ->
                        val mapped = RequiredInfoMapper.job(option)
                        OptionChip(
                            text = option,
                            selected = avoidedJobs.contains(mapped),
                            onClick = { viewModel.toggleAvoidedJob(option) }
                        )
                    }
                }
            }
        }

        // MBTI
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp))
                .padding( vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomText(
                text = "선호 성격 (MBTI)",
                type = CustomTextType.mainBold,
                size = 16.sp
            )

            MbtiRow(
                left = MbtiChoice(value = "I", label = "I(내향성)"),
                right = MbtiChoice(value = "E", label = "E(외향성)"),
                selected = mbti1,
                onSelect = { viewModel.setMbti1(it) }
            )
            MbtiRow(
                left = MbtiChoice(value = "N", label = "직관성(N)"),
                right = MbtiChoice(value = "S", label = "감각형(S)"),
                selected = mbti2,
                onSelect = { viewModel.setMbti2(it) }
            )
            MbtiRow(
                left = MbtiChoice(value = "T", label = "사고형(T)"),
                right = MbtiChoice(value = "F", label = "감정형(F)"),
                selected = mbti3,
                onSelect = { viewModel.setMbti3(it) }
            )
            MbtiRow(
                left = MbtiChoice(value = "J", label = "판단형(J)"),
                right = MbtiChoice(value = "P", label = "인식형(P)"),
                selected = mbti4,
                onSelect = { viewModel.setMbti4(it) }
            )
        }

        // 우선순위
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.white, shape = RoundedCornerShape(16.dp))
                .padding( vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomText(
                text = "우선순위",
                type = CustomTextType.mainBold,
                size = 16.sp
            )

            PriorityRow(
                title = "1순위",
                selected = priority1,
                onSelect = { viewModel.setPriority1(it) }
            )
            PriorityRow(
                title = "2순위",
                selected = priority2,
                onSelect = { viewModel.setPriority2(it) }
            )
            PriorityRow(
                title = "3순위",
                selected = priority3,
                onSelect = { viewModel.setPriority3(it) }
            )
        }
    }
}

private data class MbtiChoice(
    val value: String,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MbtiRow(
    left: MbtiChoice,
    right: MbtiChoice,
    selected: String?,
    onSelect: (String) -> Unit
) {
    val centerValue = 50f
    val snapThreshold = 6f
    val normalized = selected?.uppercase()
    val targetValue = when (normalized) {
        left.value -> 0f
        right.value -> 100f
        "X" -> centerValue
        else -> centerValue
    }
    var sliderValue by remember { mutableStateOf(targetValue) }
    var userChanged by remember { mutableStateOf(false) }

    LaunchedEffect(normalized) {
        if (!userChanged) {
            sliderValue = targetValue
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomText(
            text = left.label,
            type = CustomTextType.body,
            color = CustomColor.gray300,
            size = 12.sp
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(CustomColor.gray300)
                )
                Slider(
                    value = sliderValue,
                    onValueChange = { value ->
                        userChanged = true
                        val clamped = value.coerceIn(0f, 100f)
                        val snapped = if (abs(clamped - centerValue) <= snapThreshold) {
                            centerValue
                        } else {
                            clamped
                        }
                        sliderValue = snapped
                        val mapped = when {
                            snapped == centerValue -> "X"
                            snapped < centerValue -> left.value
                            else -> right.value
                        }
                        if (mapped != normalized) onSelect(mapped)
                    },
                    valueRange = 0f..100f,
                    steps = 0,
                    colors = SliderDefaults.colors(
                        thumbColor = CustomColor.primary600,
                        activeTrackColor = Color.Transparent,
                        inactiveTrackColor = Color.Transparent,
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent
                    ),
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(CustomColor.primary600, CircleShape)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CustomText(
                    text = "X",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    size = 12.sp
                )
            }
        }
        CustomText(
            text = right.label,
            type = CustomTextType.body,
            color = CustomColor.gray300,
            size = 12.sp,
            textAlign = TextAlign.End
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PriorityRow(
    title: String,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CustomText(
            text = title,
            type = CustomTextType.body,
            color = CustomColor.gray300,
            size = 12.sp
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            priorityOptions.forEach { option ->
                val mapped = RequiredInfoMapper.priority(option)
                OptionChip(
                    text = option,
                    selected = selected == mapped,
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}
