package com.apptive.japkor.ui.main.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.apptive.japkor.R
import com.apptive.japkor.data.model.HomeMatching
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor
import kotlin.math.absoluteValue

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun MatchingCarouselContent(
    matchings: List<HomeMatching>,
    pagerState: PagerState,
    onShowDetails: (HomeMatching) -> Unit,
    onNoMatch: () -> Unit,
    title: String,
    showNoMatchButton: Boolean
) {
    val currentMatching = matchings.getOrNull(pagerState.currentPage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText(
            text = title,
            type = CustomTextType.title,
            color = CustomColor.black
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
        ) { page ->
            val matching = matchings[page]
            val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            ).absoluteValue
            val scale = lerp(0.92f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

            MatchingCard(
                matching = matching,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        PagerIndicator(
            total = matchings.size,
            current = pagerState.currentPage
        )
        Spacer(modifier = Modifier.height(12.dp))
        CustomText(
            text = "매칭 제안이 도착했어요!\n마음에 드는 상대를 선택해주세요.",
            type = CustomTextType.body,
            color = CustomColor.gray300,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomText(
            text = "마음에 드는 경우, 남성의 프로필도 확인해보세요!",
            type = CustomTextType.body,
            color = CustomColor.gray300,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = { currentMatching?.let { onShowDetails(it) } },
            enabled = currentMatching != null,
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
                text = "이 분 프로필이 궁금해요",
                type = CustomTextType.body,
                color = Color.White
            )
        }
        if (showNoMatchButton) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onNoMatch,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomColor.gray100
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                CustomText(
                    text = "마음에 드는 상대가 없어요",
                    type = CustomTextType.body,
                    color = CustomColor.black
                )
            }
        }
    }
}

@Composable
private fun MatchingCard(
    matching: HomeMatching,
    modifier: Modifier = Modifier
) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (matching.name.isNotBlank()) {
                NameBubble(
                    name = matching.name,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 10.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CustomColor.primary100, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                val thumbnailUrl = matching.thumbnailImageUrl
                if (!thumbnailUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = "프로필 이미지",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_user),
                        contentDescription = "프로필 이미지",
                        tint = CustomColor.primary600,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }

        }
}

@Composable
private fun NameBubble(
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(CustomColor.primary100, RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)

        ) {
            CustomText(
                text = name,
                type = CustomTextType.label,
                color = CustomColor.black,
                size = 14.sp
            )
        }
        Box(
            modifier = Modifier
                .size(10.dp)
                .offset(y = (-5).dp)
                .rotate(45f)
                .background(CustomColor.primary100, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
private fun PagerIndicator(total: Int, current: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(total) { index ->
            val color = if (index == current) CustomColor.primary600 else CustomColor.gray200
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(6.dp)
                    .background(color, RoundedCornerShape(3.dp))
            )
            if (index != total - 1) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}
