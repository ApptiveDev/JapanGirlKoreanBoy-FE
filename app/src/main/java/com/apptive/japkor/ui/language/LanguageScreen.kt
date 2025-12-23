package com.apptive.japkor.ui.language

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun LanguageScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = com.apptive.japkor.R.drawable.lang_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.black.copy(alpha = 0.5f)) // 0.3 ~ 0.6 사이에서 조절
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(horizontal = 50.dp)
            .padding(vertical = 100.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = com.apptive.japkor.R.drawable.ic_en_logo),
            contentDescription = "영어 로고",
            modifier = Modifier
                .height(66.dp)
                .width(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            CustomText(
                text = "en",
                type = CustomTextType.mainRegular,
                color = CustomColor.white,
                size = 35.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            CustomText(
                text = "당신의 인연을 잇는 소개팅 어플 ‘앤’ 입니다\n" +
                        "언어를 선택하여 시작해주세요",
                type = CustomTextType.mainRegular,
                color = CustomColor.white,
                size = 16.sp
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.5f)
                .padding(end = 50.dp, bottom = 150.dp),
            horizontalAlignment = Alignment.End
        ) {
            CustomText(
                text = "縁",
                type = CustomTextType.STFangSong,
                color = CustomColor.white,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp)
                    .clickable { navController.navigate("login") },

                textAlign = TextAlign.Center,
                size = 40.sp,
                underline = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomText(
                text = "인연",
                type = CustomTextType.mainRegular,
                color = CustomColor.white,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp)
                    .clickable { navController.navigate("login") },

                textAlign = TextAlign.Center,
                size = 30.sp,
                underline = true
            )
        }
    }
}
