package com.apptive.japkor.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apptive.japkor.R
import com.apptive.japkor.data.local.DataStoreManager
import com.apptive.japkor.data.local.TokenProvider
import com.apptive.japkor.navigation.Screen
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.components.LoadingDialog
import com.apptive.japkor.ui.components.LocalToastManager
import com.apptive.japkor.ui.components.ToastType
import com.apptive.japkor.ui.main.chat.ChattingScreen
import com.apptive.japkor.ui.main.home.HomeScreen
import com.apptive.japkor.ui.main.home.HomeUiEvent
import com.apptive.japkor.ui.main.home.HomeViewModel
import com.apptive.japkor.ui.main.mypage.MypageScreen
import com.apptive.japkor.ui.main.setting.SettingScreen
import com.apptive.japkor.ui.theme.CustomColor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private data class HomeTab(
    val route: String,
    val label: String,
    val iconResId: Int
)

private object HomeRoute {
    const val Chat = "home_chat"
    const val Main = "home_main"
    const val MyPage = "home_mypage"
    const val Setting = "home_setting"
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun MainRouteScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val homeNavController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: HomeRoute.Main
    val isMainScreen = currentRoute == HomeRoute.Main
    val isSettingScreen = currentRoute == HomeRoute.Setting
    val showBottomBar = currentRoute in setOf(HomeRoute.Chat, HomeRoute.Main, HomeRoute.MyPage)
    val tabs = listOf(
        HomeTab(route = HomeRoute.Chat, label = "채팅", iconResId = R.drawable.ic_chat),
        HomeTab(route = HomeRoute.Main, label = "홈", iconResId = R.drawable.ic_n),
        HomeTab(route = HomeRoute.MyPage, label = "내정보", iconResId = R.drawable.ic_user)
    )

    var showLogoutDialog by remember { mutableStateOf(false) }
    val toastManager = LocalToastManager.current
    val uiState by viewModel.uiState.collectAsState()
    val matchings = uiState.matchings
    val selectedMatching = uiState.selectedMatching
    val pagerState = rememberPagerState(pageCount = { matchings.size })
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = isMainScreen && selectedMatching != null) {
        viewModel.hideDetails()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HomeUiEvent.ShowToast -> {
                    when (event.type) {
                        ToastType.INFO -> toastManager.info(event.message)
                        ToastType.SUCCESS -> toastManager.success(event.message)
                        ToastType.ERROR -> toastManager.error(event.message)
                    }
                }
            }
        }
    }

    LaunchedEffect(matchings.size) {
        if (matchings.isNotEmpty() && pagerState.currentPage >= matchings.size) {
            pagerState.scrollToPage(0)
        }
    }

    if (uiState.isLoading) {
        LoadingDialog()
    }

    Scaffold(
        containerColor = CustomColor.white,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    when {
                        isSettingScreen -> {
                            IconButton(onClick = { homeNavController.popBackStack() }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_back),
                                    contentDescription = "뒤로가기"
                                )
                            }
                        }
                        isMainScreen && selectedMatching != null -> {
                            IconButton(onClick = { viewModel.hideDetails() }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_back),
                                    contentDescription = "뒤로가기"
                                )
                            }
                        }
                    }
                },
                actions = {
                    if (!isSettingScreen) {
                        IconButton(onClick = { homeNavController.navigate(HomeRoute.Setting) }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = "설정"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomColor.white
                )
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = CustomColor.white
                ) {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                homeNavController.navigate(tab.route) {
                                    popUpTo(homeNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(tab.iconResId),
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                CustomText(
                                    text = tab.label,
                                    type = CustomTextType.label
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CustomColor.primary600,
                                selectedTextColor = CustomColor.primary600,
                                unselectedIconColor = CustomColor.gray400,
                                unselectedTextColor = CustomColor.gray400,
                                indicatorColor = CustomColor.white
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = homeNavController,
            startDestination = HomeRoute.Main,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(HomeRoute.Chat) {
                ChattingScreen()
            }
            composable(HomeRoute.Main) {
                HomeScreen(
                    uiState = uiState,
                    pagerState = pagerState,
                    onShowDetails = { viewModel.showDetails(it) },
                    onNoMatch = { viewModel.noMatchSelected() },
                    onConfirm = { viewModel.selectMatching(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(HomeRoute.MyPage) {
                MypageScreen()
            }
            composable(HomeRoute.Setting) {
                SettingScreen(
                    onLogoutClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                CustomText(
                    text = "로그아웃",
                    type = CustomTextType.title,
                    color = CustomColor.black
                )
            },
            text = {
                CustomText(
                    text = "정말 로그아웃 하시겠어요?",
                    type = CustomTextType.body,
                    color = CustomColor.gray400
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        TokenProvider.clearToken()
                        coroutineScope.launch {
                            dataStoreManager.clearUserInfo()
                        }
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) {
                    CustomText(
                        text = "로그아웃",
                        type = CustomTextType.body,
                        color = CustomColor.primary600
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    CustomText(
                        text = "취소",
                        type = CustomTextType.body,
                        color = CustomColor.gray400
                    )
                }
            }
        )
    }
}
