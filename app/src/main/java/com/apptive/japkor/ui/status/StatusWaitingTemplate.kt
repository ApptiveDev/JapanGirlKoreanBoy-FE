package com.apptive.japkor.ui.status

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun StatusWaitingTemplate(
    name: String,
    title: String,
    subtitle: String,
    @DrawableRes logoRes: Int,
    modifier: Modifier = Modifier,
    logoSize: Dp = 520.dp,
    logoOffsetX: Dp = 70.dp,
    logoOffsetY: Dp = 100.dp,
    logoAlpha: Float = 0.45f
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CustomColor.primary100)
    ) {
        Image(
            painter = painterResource(id = logoRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(logoSize)
                .align(Alignment.Center)
                .offset(x = logoOffsetX, y = logoOffsetY)
                .alpha(logoAlpha)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomText(
                /* TODO: FontWeight.Bold 추가해야 할까...... */
                text = "$name 님,",
                type = CustomTextType.title,
                size = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            CustomText(
                text = title,
                type = CustomTextType.title,
                size = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(100.dp))
            /* TODO: ". . ." 애니메이션 필요 */
            CustomText(
                text = subtitle,
                type = CustomTextType.body,
                color = CustomColor.gray400,
                textAlign = TextAlign.Center
            )
        }
    }
}