package com.sixclassguys.maplecalendar.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sixclassguys.maplecalendar.presentation.calendar.CalendarUiState
import com.sixclassguys.maplecalendar.theme.MapleOrange
import com.sixclassguys.maplecalendar.theme.MapleWhite
import com.sixclassguys.maplecalendar.theme.Typography
import com.sixclassguys.maplecalendar.utils.generateDaysForMonth
import com.sixclassguys.maplecalendar.utils.plusMonths
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarCard(
    uiState: CalendarUiState,
    onMonthChanged: (Int) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    today: LocalDate
) {
    val pageCount = 2000
    val initialPage = pageCount / 2
    val pagerState =
        rememberPagerState(initialPage = initialPage + uiState.monthOffset) { pageCount }
    val scope = rememberCoroutineScope()

    // 1. [동기화] 사용자가 스와이프하거나 버튼을 눌러 페이지가 안착(Settled)했을 때만 뷰모델 업데이트
    LaunchedEffect(pagerState.settledPage) {
        val newOffset = pagerState.settledPage - initialPage
        if (uiState.monthOffset != newOffset) {
            onMonthChanged(newOffset)
        }
    }

    // 2. [초기화/외부변경 대응] 뷰모델의 offset이 변경되면 Pager를 해당 위치로 이동 (ex: 초기 로딩 시)
    LaunchedEffect(uiState.monthOffset) {
        val targetPage = initialPage + uiState.monthOffset
        if (pagerState.currentPage != targetPage) {
            pagerState.scrollToPage(targetPage) // 즉시 이동하여 반응성 확보
        }
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MapleWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            // 스와이프 감도 조절 (너무 휙휙 넘어가지 않게)
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
        ) { page ->
            // 💡 [핵심] 각 페이지는 uiState가 아닌 자신의 page 인덱스로 날짜를 스스로 계산합니다.
            // 이렇게 하면 스와이프 중에 연/월 텍스트가 uiState를 기다리지 않고 즉시 보입니다.
            val monthOffset = page - initialPage
            val displayDate = remember(monthOffset) {
                // Reducer의 로직을 활용하여 해당 페이지의 날짜 객체 생성
                getLocalDateByPageOffset(today, monthOffset)
            }
            val daysForThisPage = remember(displayDate) {
                generateDaysForMonth(displayDate.year, displayDate.month)
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 월 선택기
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = MapleOrange
                        )
                    }
                    Text(
                        text = "${displayDate.year}년 ${displayDate.month.value}월",
                        style = Typography.titleMedium,
                        color = MapleOrange
                    )
                    IconButton(
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MapleOrange
                        )
                    }
                }

                // 요일 표시
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
                    daysOfWeek.forEach { day ->
                        Text(
                            text = day,
                            style = Typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = if (day == "일") Color.Red else if (day == "토") Color.Blue else Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 날짜 그리드 (Reducer에서 생성한 42개 혹은 35개 리스트 활용)
                val chunkedDays = daysForThisPage.chunked(7)
                chunkedDays.forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { date ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (date == uiState.selectedDate) MapleOrange else Color.Transparent
                                    )
                                    .clickable(enabled = date != null) { date?.let { onDateClick(it) } },
                                contentAlignment = Alignment.Center
                            ) {
                                if (date != null) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        color = when (date) {
                                            uiState.selectedDate -> Color.White

                                            today -> MapleOrange

                                            else -> Color.Black
                                        },
                                        style = Typography.bodySmall,
                                        fontWeight = if (date == uiState.selectedDate || date == today) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 헬퍼 함수: 페이지 오프셋 기준 LocalDate 계산
fun getLocalDateByPageOffset(today: LocalDate, offset: Int): LocalDate {
    var targetMonth = today.monthNumber + offset
    var targetYear = today.year
    while (targetMonth > 12) {
        targetMonth -= 12; targetYear++
    }
    while (targetMonth < 1) {
        targetMonth += 12; targetYear--
    }
    return LocalDate(targetYear, targetMonth, 1)
}