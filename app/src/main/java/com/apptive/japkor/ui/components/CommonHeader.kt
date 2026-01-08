package com.apptive.japkor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.apptive.japkor.ui.theme.CustomColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.apptive.japkor.R

@Composable
fun CommonHeader(
    title: String,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
    onBack: (() -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            // 왼쪽 영역: 버튼이 없어도 자리(48dp) 유지해서 제목이 안 흔들림
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                if (showBackButton && onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "back",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = CustomColor.gray300,
            thickness = 1.dp
        )

    }
}
