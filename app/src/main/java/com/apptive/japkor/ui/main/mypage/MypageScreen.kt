package com.apptive.japkor.ui.main.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.local.DataStoreManager.MyProfileUiModel
import com.apptive.japkor.ui.components.CommonHeader
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun MypageScreen(
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
    onBack: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)

    val profile by dataStoreManager.getMyProfileUiModel()
        .collectAsState(initial = null)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CommonHeader(
            title = "마이페이지",
            showBackButton = showBackButton,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            MyProfileCard(profile = profile)

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MyProfileCard(profile: MyProfileUiModel?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dashedBorder(
                color = Color(0xFFD7D7D7),
                strokeWidth = 1.dp,
                cornerRadius = 14.dp
            )
            .padding(16.dp)
    ) {
        // My Profile pill
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color.White,
                shadowElevation = 0.dp,
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(999.dp))
                        .drawBehind { }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    CustomText(
                        text = "My Profile",
                        type = CustomTextType.body,
                        color = CustomColor.gray400
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            //ProfileImageWithBadge(imageUrl = profile?.thumbnailImageUrl) // 돋보기 있는 버전
            ProfileImage(imageUrl = profile?.thumbnailImageUrl)

            Spacer(Modifier.width(12.dp))

            Column {
                CustomText(
                    text = profile?.name?.ifBlank { "—" } ?: "—",
                    type = CustomTextType.headline,
                    color = CustomColor.black
                )
            }
        }

        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = CustomColor.gray100)
        Spacer(Modifier.height(10.dp))

        InfoRow(label = "지역", value = profile?.residenceArea ?: "—")
        InfoRow(label = "흡연", value = profile?.smokingStatus ?: "—")
        InfoRow(label = "음주", value = profile?.drinkingFrequency ?: "—")
        InfoRow(label = "학력", value = profile?.education ?: "—")
        InfoRow(
            label = "종교",
            value = profile?.religion ?: "—"
        )

        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 13.dp,
                    vertical = 12.dp
                )
        ) {
            CustomText(
                text = "이미 심사 통과한 프로필은 수정이 어려워요.",
                type = CustomTextType.body,
                color = CustomColor.gray300
            )
        }
    }
}

@Composable
private fun ProfileImage(imageUrl: String?) {
    val painter = rememberAsyncImagePainter(model = imageUrl)

    Image(
        painter = painter,
        contentDescription = "profile",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)                 // ✅ 원형
            .background(Color(0xFFEDEDED))     // 로딩/빈값 배경
    )
}

//@Composable
//private fun ProfileImageWithBadge(imageUrl: String?) {
//    Box(modifier = Modifier.size(70.dp)) {
//        val painter = rememberAsyncImagePainter(model = imageUrl)
//
//        Image(
//            painter = painter,
//            contentDescription = "profile",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(64.dp)
//                .background(Color(0xFFEDEDED), CircleShape)
//        )
//
//        Box(
//            modifier = Modifier
//                .size(24.dp)
//                .align(Alignment.BottomEnd)
//                .background(Color(0xFF7C8055), CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "search",
//                tint = Color.White,
//                modifier = Modifier.size(16.dp)
//            )
//        }
//    }
//}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 13.dp,
                vertical = 17.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomText(
            text = label,
            type = CustomTextType.body,
            color = CustomColor.gray300,
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomText(
            text = value,
            type = CustomTextType.title,
            color = CustomColor.black
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(
                horizontal = 13.dp,
                vertical = 0.dp
            ),
        color = CustomColor.gray100,
        thickness = 1.dp
    )
}

/**
 * ✅ 점선 테두리: "여기서만 쓸 거라" 파일 내부에 private로 둠
 */
private fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp,
    cornerRadius: Dp,
    on: Dp = 6.dp,
    off: Dp = 6.dp,
) = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(on.toPx(), off.toPx()),
            0f
        )
    )
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}
