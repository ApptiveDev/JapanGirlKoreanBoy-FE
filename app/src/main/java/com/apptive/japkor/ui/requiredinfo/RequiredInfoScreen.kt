package com.apptive.japkor.ui.requiredinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.apptive.japkor.R
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.navigation.Screen
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.components.LoadingDialog
import com.apptive.japkor.ui.components.LocalToastManager
import com.apptive.japkor.ui.components.StepIndicator
import com.apptive.japkor.ui.components.ToastType
import com.apptive.japkor.ui.localization.AppLocalizer
import com.apptive.japkor.ui.localization.LocalAppLanguage
import com.apptive.japkor.ui.requiredinfo.steps.Step1Content
import com.apptive.japkor.ui.requiredinfo.steps.Step2Content
import com.apptive.japkor.ui.requiredinfo.steps.Step3Content
import com.apptive.japkor.ui.requiredinfo.steps.Step4Content
import com.apptive.japkor.ui.requiredinfo.steps.Step5Content
import com.apptive.japkor.ui.theme.CustomColor
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RequiredInfoScreen(
    navController: NavController,
    onSubmit: (name: String, email: String) -> Unit = { _, _ -> },
    initialStep: Int = 1
) {
    val context = LocalContext.current

    val requiredInfoViewModel: RequiredInfoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RequiredInfoViewModel(
                    dataStore = DataStoreManager(context.applicationContext)
                ) as T
            }
        }
    )
    val toastManager = LocalToastManager.current
    val appLanguage = LocalAppLanguage.current

    val selectedOption = remember { mutableStateOf("한국 남성") }
    val currentStep = remember { mutableStateOf(initialStep) }

    val step1Valid by requiredInfoViewModel.step1Valid.collectAsState()
    val step2Valid by requiredInfoViewModel.step2Valid.collectAsState()
    val step3Valid by requiredInfoViewModel.step3Valid.collectAsState()
    val step4Valid by requiredInfoViewModel.step4Valid.collectAsState()
    val step5Valid by requiredInfoViewModel.step5Valid.collectAsState()
    val submitState by requiredInfoViewModel.submitState.collectAsState()

    LaunchedEffect(Unit) {
        requiredInfoViewModel.events.collectLatest { event ->
            when (event) {
                is RequiredInfoEvent.ShowToast -> {
                    when (event.type) {
                        ToastType.INFO -> toastManager.info(event.message)
                        ToastType.SUCCESS -> toastManager.success(event.message)
                        ToastType.ERROR -> toastManager.error(event.message)
                    }
                }
                RequiredInfoEvent.NavigateToComplete -> {
                    navController.navigate(Screen.RequiredInfoComplete.route) {
                        popUpTo(Screen.RequiredInfo.route) { inclusive = true }
                    }
                }
            }
        }
    }

    if (submitState is SubmitState.Loading) {
        LoadingDialog()
    }

    val canProceed = when (currentStep.value) {
        1 -> step1Valid
        2 -> step2Valid
        3 -> step3Valid
        4 -> step4Valid
        else -> step5Valid
    } && submitState !is SubmitState.Loading

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        containerColor = Color.White,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 4.dp,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 30.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentStep.value == 1) {
                        Button(
                            onClick = { currentStep.value += 1 },
                            enabled = canProceed,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CustomColor.primary600,
                                disabledContainerColor = CustomColor.gray200
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            CustomText(
                                text = "다음",
                                type = CustomTextType.body,
                                color = Color.White
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { currentStep.value -= 1 },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CustomColor.gray100
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                CustomText(
                                    text = "이전",
                                    type = CustomTextType.body,
                                    color = CustomColor.black
                                )
                            }

                            Button(
                                onClick = {
                                    if (currentStep.value < 5) {
                                        currentStep.value += 1
                                    } else {
                                        requiredInfoViewModel.submitRequiredInfo()
                                    }
                                },
                                enabled = canProceed,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CustomColor.primary600,
                                    disabledContainerColor = CustomColor.primary300
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                CustomText(
                                    text = if (currentStep.value < 5) "다음" else "완료",
                                    type = CustomTextType.body,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        val layoutDirection = LocalLayoutDirection.current
        val contentPadding = PaddingValues(
            start = innerPadding.calculateStartPadding(layoutDirection),
            end = innerPadding.calculateEndPadding(layoutDirection),
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + 24.dp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = contentPadding
        ) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
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
                            contentDescription = AppLocalizer.translate("뒤로가기", appLanguage),
                        )
                    }
                }
            }
            item {
                StepIndicator(currentStep = currentStep.value, totalSteps = 5)
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                when (currentStep.value) {
                    1 -> Step1Content(selectedOption = selectedOption,  viewModel = requiredInfoViewModel)
                    2 -> Step2Content(viewModel = requiredInfoViewModel)
                    3 -> Step3Content(viewModel = requiredInfoViewModel)
                    4 -> Step4Content(viewModel = requiredInfoViewModel)
                    5 -> Step5Content(viewModel = requiredInfoViewModel)
                }
            }
        }
    }
}
