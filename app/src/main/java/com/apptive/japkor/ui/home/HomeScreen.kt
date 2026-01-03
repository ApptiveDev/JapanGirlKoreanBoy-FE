package com.apptive.japkor.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.apptive.japkor.R
import com.apptive.japkor.ui.components.CustomText
import com.apptive.japkor.ui.components.CustomTextType
import com.apptive.japkor.ui.theme.CustomColor

private data class HomeTab(
    val label: String,
    val iconResId: Int
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(1) }
    val tabs = listOf(
        HomeTab("채팅", R.drawable.ic_chat),
        HomeTab("홈", R.drawable.ic_n),
        HomeTab("내정보", R.drawable.ic_user)
    )

    Scaffold(
        containerColor = CustomColor.white,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = "설정"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomColor.white
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CustomColor.white
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            CustomText(
                text = "매칭 진행 중입니다..",
                type = CustomTextType.body,
                color = CustomColor.gray400
            )
        }
    }
}
