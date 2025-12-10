package com.apptive.japkor.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.apptive.japkor.R
import com.apptive.japkor.navigation.Screen
import com.apptive.japkor.ui.components.CustomOutlinedTextField
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.components.LoadingDialog
import com.apptive.japkor.ui.components.LocalToastManager
import com.apptive.japkor.ui.components.auth.GoogleSignUpButton
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun LoginScreen(navController: NavController,viewModel: LoginScreenViewModel = viewModel()) {
    val toastManager = LocalToastManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // 키보드 높이 감지
    LocalDensity.current
    WindowInsets.ime
    WindowInsets.navigationBars

    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingDialog()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(WindowInsets.safeDrawing.asPaddingValues()),
            horizontalAlignment = Alignment.Start
        ) {
            // 헤더: 좌측 상단 뒤로가기 아이콘 + 구분선 + 언어 선택
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.width(20.dp)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    thickness = 1.dp,
                    color = CustomColor.gray200
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    CustomText(
                        text = "한국어",
                        type = CustomTextType.body,
                        color = CustomColor.gray300,
                        underline = true
                    )
                }
            }

            // 본문 영역
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomText(
                    text = "어서오세요\nen 입니다",
                    type = CustomTextType.mainRegular,
                    size = 32.sp
                )
                CustomText(
                    text = "당신의 인연을 잇는 소개팅 어플 '앤' 입니다\n",
                    color = CustomColor.gray400,
                    type = CustomTextType.mainRegular,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomOutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "이메일을 입력하세요"
                    )

                    CustomOutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "비밀번호를 입력하세요",
                        isPassword = true
                    )

                    Button(
                        onClick = {
                            navController.navigate("requiredInfo")

//                            viewModel.signIn(email,password) {success ->
//                                if (success) {
//                                    toastManager.success("로그인 성공! 환영합니다.")
//                                    navController.navigate("requiredInfo")
//                                }
//                                else{
//                                    toastManager.error("로그인 실패! 이메일과 비밀번호를 확인해주세요.")
//                                }
//                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomColor.gray300
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        CustomText(
                            text = "로그인",
                            type = CustomTextType.body,
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = "아이디 찾기",
                            type = CustomTextType.body,
                            color = CustomColor.black,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomText(
                            text = " | ",
                            type = CustomTextType.body,
                            color = CustomColor.gray300,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomText(
                            text = "비밀번호 찾기",
                            type = CustomTextType.body,
                            color = CustomColor.black,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomText(
                            text = " | ",
                            type = CustomTextType.body,
                            color = CustomColor.gray300,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CustomText(
                            text = "회원가입",
                            type = CustomTextType.body,
                            color = CustomColor.black,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.SignUp.route)
                            }
                        )
                    }
                }
            }

            // 키보드가 열릴 때 충분한 공간 확보
            Spacer(modifier = Modifier.height(200.dp))
        }

        // 하단 고정 버튼 영역
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 24.dp)
                .padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding() + 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    color = CustomColor.gray300,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                CustomText(
                    text = "SNS 계정으로 로그인",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(10.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    color = CustomColor.gray300,
                    thickness = 1.dp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,

                ) {
                GoogleSignUpButton(
                    onSignedIn = {
                        Log.d("LoginScreen", "onSignedIn 콜백 호출됨")
                        toastManager.success("로그인 성공! 환영합니다.")
                        navController.navigate("requiredinfo")
                    },
                )

            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = "이용약관",
                    type = CustomTextType.body,
                    color = CustomColor.black,
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomText(
                    text = " | ",
                    type = CustomTextType.body,
                    color = CustomColor.gray300,
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomText(
                    text = "개인정보 보호정책",
                    type = CustomTextType.body,
                    color = CustomColor.black,
                )
            }


        }
    }
}
