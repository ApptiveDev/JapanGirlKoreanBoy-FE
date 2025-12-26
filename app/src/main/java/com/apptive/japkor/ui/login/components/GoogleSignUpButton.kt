package com.apptive.japkor.ui.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.R
import com.apptive.japkor.ui.localization.AppLocalizer
import com.apptive.japkor.ui.localization.LocalAppLanguage

/**
 * Google 브랜드 가이드라인에 맞춘 UI-only 버튼.
 * 로그인/토큰 처리는 외부에서 onClick으로 전달하세요.
 */
@Composable
fun GoogleSignUpButton(
    modifier: Modifier = Modifier,
    text: String = "Google로 계속",
    onClick: () -> Unit = {}
) {
    val appLanguage = LocalAppLanguage.current
    val localizedText = AppLocalizer.translate(text, appLanguage)
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF3C4043)
        ),
        border = BorderStroke(1.dp, Color(0xFFDADCE0)),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = AppLocalizer.translate("Google 로그인", appLanguage),
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(18.dp)
            )
            Text(
                text = localizedText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3C4043)
            )
        }
    }
}
