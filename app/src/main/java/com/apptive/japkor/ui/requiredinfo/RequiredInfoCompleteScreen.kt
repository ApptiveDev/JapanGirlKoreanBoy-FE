package com.apptive.japkor.ui.requiredinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.apptive.japkor.navigation.Screen
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun RequiredInfoCompleteScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(color = CustomColor.gray100, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "완료",
                        tint = CustomColor.black,
                        modifier = Modifier.size(40.dp)
                    )
                }
                CustomText(
                    text = "신청이 완료되었습니다",
                    type = CustomTextType.title,
                    size = 24.sp,
                    textAlign = TextAlign.Center
                )
                CustomText(
                    text = ". . .",
                    type = CustomTextType.title,
                    color = CustomColor.gray400,
                    textAlign = TextAlign.Center
                )
                CustomText(
                    text = "심사 중입니다",
                    type = CustomTextType.body,
                    color = CustomColor.gray400,
                    textAlign = TextAlign.Center
                )
            }



            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, bottom = 8.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    CustomText(
                        text = "심사는 약 일주일 정도 걸립니다.\n심사가 완료되면 매칭이 시작됩니다!",
                        type = CustomTextType.body,
                        color = CustomColor.gray400,
                        textAlign = TextAlign.Start
                    )
                }

                Button(
                    onClick = {
                        navController.navigate(Screen.Language.route) {
                            popUpTo(Screen.Language.route) { inclusive = false }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CustomColor.gray300),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    CustomText(
                        text = "확인했어요",
                        type = CustomTextType.body,
                        color = Color.Black
                    )
                }

            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
