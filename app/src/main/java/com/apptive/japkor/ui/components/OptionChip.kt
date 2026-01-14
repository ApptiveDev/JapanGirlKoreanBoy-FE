package com.apptive.japkor.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun OptionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)

    val background = when {
        !enabled -> CustomColor.gray100
        selected -> CustomColor.primary600
        else -> CustomColor.white
    }
    val textColor = when {
        !enabled -> CustomColor.gray300
        selected -> CustomColor.white
        else -> CustomColor.gray300
    }
    val borderColor = when {
        !enabled -> CustomColor.gray200
        selected -> CustomColor.gray300
        else -> CustomColor.gray200
    }

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
                enabled = enabled,
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
