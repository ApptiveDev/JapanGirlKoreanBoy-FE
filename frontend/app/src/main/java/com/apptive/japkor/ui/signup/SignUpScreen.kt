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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.apptive.japkor.R
import com.apptive.japkor.ui.components.CustomOutlinedTextField
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.imePadding


/**
 * 회원가입 화면 (SignUpScreen.kt)
 * EmailWithAuthSection, HalfCustomTextField, AuthCodeField, PasswordSection 컴포저블 포함
 * EmailWithAuthSection: 이메일 입력 + 인증 코드 전송/인증
 * HalfCustomTextField: CustomTextField를 반으로 쪼갠 버전 (이메일 입력에서만 사용)
 * AuthCodeField: 인증 코드 입력 필드
 * PasswordSection: 비밀번호 입력 및 확인
 */
@Composable
fun SignUpScreen(navController: NavController) {
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
                canSendCode = canSendCode,
                onClickSendCode = {
                    // TODO: 인증 코드 전송 API
                },
                canVerifyCode = canVerifyCode,
                onClickVerify = {
                    // TODO: 인증 코드 검증 API
                }
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
                onClick = { /* TODO: 회원가입 API */ },
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
                    containerColor = Color(0xFFF45C4A),
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

/**
 * 이메일 입력 + 인증 코드 전송/인증
 */
@Composable
private fun EmailWithAuthSection(
    emailLocal: String,
    onEmailLocalChange: (String) -> Unit,
    emailDomain: String,
    onEmailDomainChange: (String) -> Unit,
    authCode: String,
    onAuthCodeChange: (String) -> Unit,
    canSendCode: Boolean,
    onClickSendCode: () -> Unit,
    canVerifyCode: Boolean,
    onClickVerify: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 라벨
        CustomText(
            text = "이메일",
            type = CustomTextType.body,
            color = CustomColor.black,
            size = 15.sp
        )

        // 이메일 앞부분 + 도메인 (둘 다 직접 입력)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이메일 앞부분
            HalfCustomTextField(
                value = emailLocal,
                onValueChange = onEmailLocalChange,
                placeholder = "이메일",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            CustomText(
                text = " @ ",
                type = CustomTextType.body,
                color = CustomColor.gray300,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            // 도메인 직접 입력
            HalfCustomTextField(
                value = emailDomain,
                onValueChange = onEmailDomainChange,
                placeholder = "직접 입력",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        }

        // 인증 코드 전송 버튼 (비활성/활성)
        Button(
            onClick = onClickSendCode,
            enabled = canSendCode,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF45C4A),
                contentColor = CustomColor.white,
                disabledContainerColor = CustomColor.gray300,
                disabledContentColor = CustomColor.white
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            CustomText(
                text = "인증 코드 전송",
                type = CustomTextType.body,
                size = 15.sp
            )
        }

        // 인증 코드 입력 + 필드 안쪽에 인증 버튼
        AuthCodeField(
            authCode = authCode,
            onAuthCodeChange = onAuthCodeChange,
            canVerifyCode = canVerifyCode,
            onClickVerify = onClickVerify
        )
    }
}



/**
 * CustomTextField 반으로 쪼갠 버전 (이메일 입력에서만 사용)
 */
@Composable
private fun HalfCustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val shape = RoundedCornerShape(16.dp)
    var isFocused by remember { mutableStateOf(false) }
    val borderWidth = if (isFocused) 2.dp else 1.dp
    val borderColor = if (isFocused) CustomColor.gray300 else CustomColor.gray200

    Box(
        modifier = modifier
            .height(50.dp)
            .border(borderWidth, borderColor, shape)
            .background(CustomColor.white, shape)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            CustomText(
                text = placeholder,
                type = CustomTextType.body,
                color = CustomColor.gray300,
                size = 14.sp
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = CustomColor.black,
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused // 포커스 상태 업데이트 (never read라고 뜨지만 필요함)
                },
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation
        )
    }
}

/**
 * 인증 코드 입력 필드
 */
@Composable
private fun AuthCodeField(
    authCode: String,
    onAuthCodeChange: (String) -> Unit,
    canVerifyCode: Boolean,
    onClickVerify: () -> Unit
) {
    val outerShape = RoundedCornerShape(16.dp)
    var isFocused by remember { mutableStateOf(false) }
    val borderWidth = if (isFocused) 2.dp else 1.dp
    val borderColor = if (isFocused) CustomColor.gray300 else CustomColor.gray200

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(borderWidth, borderColor, outerShape)
            .background(Color.White, outerShape)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 코드 입력 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (authCode.isEmpty()) {
                    CustomText(
                        text = "인증 코드",
                        type = CustomTextType.body,
                        color = CustomColor.gray300,
                        size = 14.sp
                    )
                }
                BasicTextField(
                    value = authCode,
                    onValueChange = onAuthCodeChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = CustomColor.black,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        }
                )
            }

            // 필드 안에 들어가는 작은 인증 버튼
            Button(
                onClick = onClickVerify,
                enabled = canVerifyCode,
                modifier = Modifier.height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF45C4A),
                    contentColor = Color.White,
                    disabledContainerColor = CustomColor.gray300,
                    disabledContentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                CustomText(
                    text = "인증",
                    type = CustomTextType.body,
                    size = 12.sp
                )
            }
        }
    }
}

/**
 * 비밀번호 입력 및 확인
 */
@Composable
private fun PasswordSection(
    password: String,
    passwordConfirm: String,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    isPasswordMismatch: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomText(
            text = "비밀번호",
            type = CustomTextType.body,
            color = CustomColor.black,
            size = 15.sp
        )
        CustomOutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "비밀번호",
            isPassword = true
        )
        CustomOutlinedTextField(
            value = passwordConfirm,
            onValueChange = onPasswordConfirmChange,
            placeholder = "비밀번호 확인",
            isPassword = true
        )

        // 비밀번호 불일치 에러 메시지
        if (isPasswordMismatch) {
            CustomText(
                text = "비밀번호가 일치하지 않습니다.",
                color = Color(0xFFF45C4A),
                type = CustomTextType.body,
                modifier = Modifier.padding(horizontal = 4.dp),
                size = 13.sp
            )
        }

        CustomText(
            text = "8 ~ 16자 이내 영문, 특수 문자 조합",
            color = CustomColor.gray300,
            type = CustomTextType.body,
            modifier = Modifier.padding(horizontal = 4.dp),
            size = 14.sp
        )
    }
}
