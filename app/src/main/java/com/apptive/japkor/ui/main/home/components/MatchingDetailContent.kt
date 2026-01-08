package com.apptive.japkor.ui.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.apptive.japkor.R
import com.apptive.japkor.data.model.HomeMatching
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
internal fun MatchingDetailContent(
    matching: HomeMatching,
    canSelectMatching: Boolean,
    onConfirm: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileHeader(matching = matching)
            }
            item {
                DetailCard(
                    matching = matching
                )
            }
        }
        if (canSelectMatching) {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomColor.primary600,
                    disabledContainerColor = CustomColor.primary300
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                CustomText(
                    text = "마음에 들어요 매칭해주세요",
                    type = CustomTextType.body,
                    color = Color.White
                )
            }
        } else {
            Button(
                onClick = onAccept,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomColor.primary600,
                    disabledContainerColor = CustomColor.primary300
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                CustomText(
                    text = "수락할래요",
                    type = CustomTextType.body,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onReject,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomColor.gray100
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                CustomText(
                    text = "거절할래요",
                    type = CustomTextType.body,
                    color = CustomColor.black
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(matching: HomeMatching) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(CustomColor.primary100, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_user),
                contentDescription = "프로필 이미지",
                tint = CustomColor.primary600,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            CustomText(
                text = matching.name,
                type = CustomTextType.headline,
                color = CustomColor.black
            )
            CustomText(
                text = matching.email,
                type = CustomTextType.body,
                color = CustomColor.gray400
            )
        }
    }
}

@Composable
private fun DetailCard(
    matching: HomeMatching
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CustomColor.gray100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomText(
                text = "상세 정보",
                type = CustomTextType.title,
                color = CustomColor.black
            )
            DetailItem(label = "이름", value = matching.name)
            DetailItem(label = "이메일", value = matching.email)
            DetailItem(label = "키", value = formatHeight(matching.height))
            DetailItem(label = "몸무게", value = formatWeight(matching.weight))
            DetailItem(label = "거주지역", value = formatText(matching.residenceArea))
            DetailImageItem(label = "thumbnailImageUrl", imageUrl = matching.thumbnailImageUrl)
            DetailImageItem(label = "profileImageUrl", imageUrl = matching.profileImageUrl)
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomText(
            text = label,
            type = CustomTextType.label,
            color = CustomColor.gray400
        )
        CustomText(
            text = value,
            type = CustomTextType.body,
            color = CustomColor.black
        )
    }
}

@Composable
private fun DetailImageItem(label: String, imageUrl: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomText(
            text = label,
            type = CustomTextType.label,
            color = CustomColor.gray400
        )
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CustomColor.gray100),
                contentScale = ContentScale.Crop
            )
        } else {
            CustomText(
                text = "미입력",
                type = CustomTextType.body,
                color = CustomColor.gray400
            )
        }
    }
}

private fun formatHeight(value: Int?): String {
    return value?.let { "${it}cm" } ?: "미입력"
}

private fun formatWeight(value: Int?): String {
    return value?.let { "${it}kg" } ?: "미입력"
}

private fun formatText(value: String?): String {
    return value?.takeIf { it.isNotBlank() } ?: "미입력"
}
