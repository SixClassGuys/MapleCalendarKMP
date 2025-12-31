package com.sixclassguys.maplecalendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sixclassguys.maplecalendar.presentation.NotificationViewModel
import com.sixclassguys.maplecalendar.ui.calendar.MapleCalendarScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App(
    notificationViewModel: NotificationViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        notificationViewModel.initNotification()
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
                .statusBarsPadding(),
            // 배경색을 지정하거나 아까 만든 그라데이션을 여기에 넣어도 됩니다.
            color = Color.White
        ) {
            // 2. 방금 만든 캘린더 스크린을 호출합니다.
            MapleCalendarScreen()
        }
    }
}