package com.apptive.japkor.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.apptive.japkor.R
import com.apptive.japkor.ui.components.CustomOutlinedTextField
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apptive.japkor.ui.components.LoadingDialog

import com.apptive.japkor.ui.components.ToastType
import com.apptive.japkor.ui.signup.components.EmailWithAuthSection
import com.apptive.japkor.ui.signup.components.PasswordSection
import com.apptive.japkor.ui.components.LocalToastManager
import kotlinx.coroutines.flow.collectLatest


/**
 * 회원가입 화면 (SignUpScreen.kt)
 * EmailWithAuthSection, HalfCustomTextField, AuthCodeField, PasswordSection 컴포저블 포함
 * EmailWithAuthSection: 이메일 입력 + 인증 코드 전송/인증
 * HalfCustomTextField: CustomTextField를 반으로 쪼갠 버전 (이메일 입력에서만 사용)
 * AuthCodeField: 인증 코드 입력 필드
 * PasswordSection: 비밀번호 입력 및 확인
 */
@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = viewModel()) {
    val toastManager = LocalToastManager.current

    var name by remember { mutableStateOf("") }

    var emailLocal by remember { mutableStateOf("") }   // @ 앞
    var emailDomain by remember { mutableStateOf("") }  // @ 뒤
    var authCode by remember { mutableStateOf("") }     // 인증 코드
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    // 버튼 활성화 조건
    val canSendCode = emailLocal.isNotBlank() && emailDomain.isNotBlank()
    val canVerifyCode = authCode.isNotBlank()

    // 비밀번호 불일치 여부
    val isPasswordMismatch =
        passwordConfirm.isNotBlank() && password != passwordConfirm

    val scrollState = rememberScrollState()

    val hasSentCode by viewModel.hasSentCode.collectAsState()
    val isResendEnabled by viewModel.isResendEnabled.collectAsState()
    val codeTimerSeconds by viewModel.codeTimerSeconds.collectAsState()
    val emailVerified by viewModel.emailVerified.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    if (isLoading) {
        LoadingDialog()
    }



    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is SignUpUiEvent.ShowToast -> {
                    when (event.type) {
                        ToastType.INFO -> toastManager.info(event.message)
                        ToastType.SUCCESS -> toastManager.success(event.message)
                        ToastType.ERROR -> toastManager.error(event.message)
                    }
                }
                SignUpUiEvent.NavigateToLogin -> navController.navigate("login")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .imePadding(), // 키보드 올라온 만큼 자동 padding
        horizontalAlignment = Alignment.Start
    ) {
        /* 헤더: 좌측 상단 뒤로가기 아이콘 + 구분선 + 언어 선택 */
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.width(20.dp)
                    )
                }
                CustomText(
                    text = "회원가입",
                    modifier = Modifier.weight(1f),
                    color = CustomColor.black,
                    textAlign = TextAlign.Center,
                    size = 17.sp
                )

                Box( // "회원가입" 텍스트를 중앙으로 맞추는 용도
                    modifier = Modifier
                        .padding(20.dp)
                        .width(48.dp)
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

        /* 본문 영역 */
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomText(
                text = "이름",
                type = CustomTextType.body,
                color = CustomColor.black,
                size = 15.sp
            )
            CustomOutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "이름"
            )

            // 이메일
            Spacer(modifier = Modifier.height(16.dp))
            EmailWithAuthSection(
                emailLocal = emailLocal,
                onEmailLocalChange = { emailLocal = it },
                emailDomain = emailDomain,
                onEmailDomainChange = { emailDomain = it },
                authCode = authCode,
                onAuthCodeChange = { authCode = it },
                canSendCode = canSendCode && !emailVerified,
                onClickSendCode = {
                    val email = "$emailLocal@$emailDomain"
                    viewModel.sendEmailCode(email)
                },
                showResend = hasSentCode,
                isResendEnabled = isResendEnabled && !emailVerified,
                remainingSeconds = if (emailVerified) 0 else codeTimerSeconds,
                onClickResend = {
                    val email = "$emailLocal@$emailDomain"
                    viewModel.sendEmailCode(email)
                },
                canVerifyCode = canVerifyCode && !emailVerified,
                onClickVerify = {
                    val email = "$emailLocal@$emailDomain"
                    viewModel.verifyEmail(email, authCode)
                },
                isEmailVerified = emailVerified
            )

            // 비밀번호
            Spacer(modifier = Modifier.height(16.dp))
            PasswordSection(
                password = password,
                passwordConfirm = passwordConfirm,
                onPasswordChange = { password = it },
                onPasswordConfirmChange = { passwordConfirm = it },
                isPasswordMismatch = isPasswordMismatch
            )

            // 가입하기
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val email = "$emailLocal@$emailDomain"
                    viewModel.signUp(
                        name = name,
                        email = email,
                        password = password
                    )
                },
                enabled = name.isNotBlank()
                        && emailLocal.isNotBlank()
                        && emailDomain.isNotBlank()
                        && authCode.isNotBlank()
                        && password.isNotBlank()
                        && passwordConfirm.isNotBlank()
                        && !isPasswordMismatch,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomColor.primary600,
                    contentColor = CustomColor.white,
                    disabledContainerColor = CustomColor.gray300,
                    disabledContentColor = CustomColor.white
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                CustomText(
                    text = "가입하기",
                    type = CustomTextType.body,
                )
            }
        }
    }
}
