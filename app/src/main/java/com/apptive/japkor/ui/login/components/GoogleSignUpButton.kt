package com.apptive.japkor.ui.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.R
import com.apptive.japkor.ui.theme.CustomColor

/**
 * Google 브랜드 가이드라인에 맞춘 UI-only 버튼.
 * 로그인/토큰 처리는 외부에서 onClick으로 전달하세요.
 */
@Composable
fun GoogleSignUpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(44.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, CustomColor.black),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Sign in with Google",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}