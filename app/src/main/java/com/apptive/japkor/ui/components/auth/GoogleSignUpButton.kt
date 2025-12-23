package com.apptive.japkor.ui.components.auth

import android.accounts.AccountManager
import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.apptive.japkor.R
import com.apptive.japkor.ui.components.LocalToastManager
import com.apptive.japkor.ui.theme.CustomColor
import com.apptive.japkor.utils.auth.GoogleCredentialHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun GoogleSignUpButton(onSignedIn: () -> Unit) {
    val context = LocalContext.current
    val toastManager = LocalToastManager.current
    val activity = context as? ComponentActivity ?: (context as? Activity)
    if (activity == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Activity 컨텍스트를 찾을 수 없습니다.")
        }
        return
    }

    val credentialManager = remember { CredentialManager.create(context) }
    val coroutineScope = rememberCoroutineScope()

    Column {
        Button(
            modifier = Modifier
                .size(30.dp)
                .border(
                    width = 1.dp,
                    color = CustomColor.gray200,
                    shape = CircleShape
                ),
            shape = CircleShape,
            onClick = {

                coroutineScope.launch {
                    try {
                        // 1) Credential에서 ID 토큰 가져오기
                        val idToken = try {
                            GoogleCredentialHelper.fetchGoogleIdToken(
                                activity = activity,
                                context = context,
                                credentialManager = credentialManager
                            )
                        } catch (e: Exception) {
                            // No credentials -> 계정 추가 유도
                            if (e.message?.contains("No credentials available") == true) {
                                val accountManager = AccountManager.get(activity)
                                accountManager.addAccount(
                                    "com.google",
                                    null,
                                    null,
                                    null,
                                    activity,
                                    { future ->
                                        activity.runOnUiThread {
                                            toastManager.success("계정 추가 화면이 열렸습니다.")
                                        }
                                    },
                                    null
                                )
                            }
                            throw e
                        }

                        // 2) Firebase 인증 & UserData 획득
                        // val userData: UserData = AuthRepository.firebaseSignIn(idToken)

                        // 3) 서버로 전송 (optional) — 오류 있어도 흐름 유지 가능
//                        try {
//                            val resp = AuthRepository.signUp(userData)
//                            if (resp.isSuccessful) {
//                                withContext(kotlinx.coroutines.Dispatchers.Main) {
//                                    Toast.makeText(
//                                        activity,
//                                        "회원가입 성공 (${resp.code()})",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            } else {
//                                val err = resp.errorBody()?.string()
//                                Log.e("GoogleSignInButton", "서버 응답 실패: ${resp.code()} / $err")
//                            }
//                        } catch (e: Exception) {
//                            Log.e("GoogleSignInButton", "서버 전송 예외 ${e.message}", e)
//                        }

                        // 4) Firestore 저장 ->  로그인 할때마다 저장하므로 수정 필요
                        try {
                            // UserRepository.saveUserToFirestore(userData)
                        } catch (e: Exception) {
                            Log.e("GoogleSignInButton", "Firestore 저장 실패 ${e.message}", e)
                        }

                        // 성공 콜백
                        onSignedIn()

                    } catch (e: CancellationException) {
                        // 취소 무시
                        Log.w("GoogleSignInButton", "작업 취소됨 ${e.message}")
                        toastManager.info("Google 로그인 취소")
                    } catch (e: Exception) {
                        Log.e("GoogleSignInButton", "로그인 실패: ${e.message}")
                        toastManager.error("Google 로그인에 실패했습니다.")

                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            contentPadding = PaddingValues(1.dp) // 내부 패딩 제거
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Logo",
                tint = CustomColor.gray400,
                modifier = Modifier.size(20.dp) // 아이콘을 버튼에 맞게 크게
            )
        }
    }
}
