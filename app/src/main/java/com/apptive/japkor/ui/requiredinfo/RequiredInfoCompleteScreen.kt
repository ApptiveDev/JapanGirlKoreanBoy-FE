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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.model.UserStatus
import com.apptive.japkor.navigation.Screen
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.localization.AppLocalizer
import com.apptive.japkor.ui.localization.LocalAppLanguage
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun RequiredInfoCompleteScreen(navController: NavController) {
    val context = LocalContext.current
    val appLanguage = LocalAppLanguage.current
    val dataStore = remember { DataStoreManager(context) }
    val statusValue by dataStore.getUserStatus().collectAsState(initial = "")
    val userStatus = runCatching { UserStatus.valueOf(statusValue) }.getOrNull()
    val statusMessage = when (userStatus) {
        UserStatus.APPROVED,
        UserStatus.CONNECTING,
        UserStatus.CONNECTED,
        UserStatus.BLACKLISTED -> userStatus.displayLabel
        UserStatus.INCOMPLETE_PROFILE -> "필수정보 입력이 필요합니다"
        UserStatus.PENDING_APPROVAL,
        null -> "심사 중입니다"
    }

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
                        .size(70.dp)
                        .background(color = CustomColor.primary600, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = AppLocalizer.translate("완료", appLanguage),
                        tint = CustomColor.white,
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
                    text = statusMessage,
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
                    colors = ButtonDefaults.buttonColors(containerColor = CustomColor.primary600),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    CustomText(
                        text = "확인했어요",
                        type = CustomTextType.body,
                        color = Color.White
                    )
                }

            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
