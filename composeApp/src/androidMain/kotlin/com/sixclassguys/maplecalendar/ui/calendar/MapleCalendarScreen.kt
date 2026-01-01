package com.sixclassguys.maplecalendar.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sixclassguys.maplecalendar.presentation.notification.NotificationViewModel
import com.sixclassguys.maplecalendar.presentation.calendar.CalendarIntent
import com.sixclassguys.maplecalendar.presentation.calendar.CalendarViewModel
import com.sixclassguys.maplecalendar.presentation.notification.NotificationIntent
import com.sixclassguys.maplecalendar.theme.MapleOrange
import com.sixclassguys.maplecalendar.ui.component.CalendarDayCell
import com.sixclassguys.maplecalendar.ui.component.DaysOfWeekHeader
import com.sixclassguys.maplecalendar.ui.component.EventDetailBottomSheet
import kotlinx.coroutines.launch
import kotlinx.datetime.number
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapleCalendarScreen(
    viewModel: CalendarViewModel = koinViewModel(),
    notificationViewModel: NotificationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val notificationUiState by notificationViewModel.uiState.collectAsState()

    // 전체 스크롤 상태 및 새로고침 상태 관리
    val scrollState = rememberScrollState()
    val isRefreshing = uiState.isLoading

    val startIndex = 5000
    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { 10000 }
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        val pageDiff = pagerState.currentPage - startIndex
        viewModel.onIntent(CalendarIntent.ChangeMonth(pageDiff))
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.onIntent(CalendarIntent.Refresh) },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "이전 달",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "${uiState.year}년 ${uiState.month.number}월",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                IconButton(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "다음 달",
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DaysOfWeekHeader()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                verticalAlignment = Alignment.Top,
                key = { it } // 각 페이지의 고유 키
            ) { page ->
                // 현재 페이지에 해당하는 날짜 리스트 생성 (각 페이지마다 독립적인 날짜 계산)
                val pageDiff = page - startIndex
                // 날짜 계산은 ViewModel의 헬퍼 함수 활용
                val displayDate = remember(page) { viewModel.getLocalDateByOffset(pageDiff) }
                val days = remember(displayDate) {
                    // 리팩토링된 ViewModel의 generateDays는 이제 Reducer가 관리하므로
                    // 필요 시 ViewModel을 통해 호출하거나 Reducer의 로직을 재사용합니다.
                    viewModel.generateDays(displayDate.year, displayDate.month)
                }

                // 이 페이지가 보여줘야 할 데이터 키 (예: "2025-12")
                val pageKey = "${displayDate.year}-${displayDate.monthNumber}"

                // 전체 맵에서 이 페이지(달)에 해당하는 리스트만 추출
                val monthlyEvents = uiState.eventsMap[pageKey]
                val isPageLoading = monthlyEvents == null && uiState.isLoading

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (isPageLoading) {
                        // 해당 월의 데이터가 아직 없고 로딩 중일 때만 표시
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MapleOrange // 메이플 주황색
                        )
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(if (isPageLoading) 0.3f else 1f),
                        userScrollEnabled = false
                    ) {
                        items(days) { date ->
                            if (date != null) {
                                val dateEvents = monthlyEvents?.filter {
                                    date == it.startDate || date == it.endDate
                                } ?: emptyList()

                                CalendarDayCell(
                                    date = date,
                                    events = dateEvents,
                                    onClick = {
                                        if (!uiState.isLoading) {
                                            viewModel.onIntent(CalendarIntent.SelectDate(date))
                                        }
                                    }
                                )
                            } else {
                                Box(modifier = Modifier.aspectRatio(0.55f))
                            }
                        }
                    }
                }
            }

            // 알림 ON/OFF 토글 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("종료 예정 이벤트 알림 받기")

                Switch(
                    checked = notificationUiState.isNotificationEnabled,
                    colors = SwitchDefaults.colors(
                        // 1. 활성화(ON) 상태
                        checkedThumbColor = Color.White,          // ON일 때 동그라미: 흰색
                        checkedTrackColor = MapleOrange,          // ON일 때 바탕: 메이플 주황색
                        checkedBorderColor = MapleOrange,         // ON일 때 테두리: 메이플 주황색

                        // 2. 비활성화(OFF) 상태
                        uncheckedThumbColor = Color.LightGray,    // OFF일 때 동그라미: 연회색
                        uncheckedTrackColor = Color(0xFFEEEEEE),  // OFF일 때 바탕: 아주 연한 회색
                        uncheckedBorderColor = Color.LightGray,   // OFF일 때 테두리: 연회색
                    ),
                    onCheckedChange = {
                        notificationViewModel.onIntent(NotificationIntent.ToggleNotification(it))
                    }
                )
            }
        }
    }

    // 이벤트 상세 바텀시트
    if (uiState.showBottomSheet && (uiState.selectedDate != null)) {
        val selectedDate = uiState.selectedDate!!
        val selectedKey = "${selectedDate.year}-${selectedDate.monthNumber}"
        val dailyEvents = uiState.eventsMap[selectedKey]?.filter { event ->
            selectedDate in event.startDate..event.endDate
        } ?: emptyList()

        EventDetailBottomSheet(
            selectedDate = selectedDate,
            events = dailyEvents,
            onDismiss = {
                viewModel.onIntent(CalendarIntent.DismissBottomSheet)
            }
        )
    }
}