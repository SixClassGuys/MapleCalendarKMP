package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.Typography
import com.sixclassguys.maplecalendar.utils.daysInMonth
import com.sixclassguys.maplecalendar.utils.plusMonths
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.todayIn

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarPlaceholder(
    startDate: LocalDate,
    endDate: LocalDate,
    currentMonthDate: LocalDate, // 현재 외부에서 관리되는 기준 월
    selectedDates: Set<LocalDate>,
    onDateClick: (LocalDate) -> Unit,
    onMonthChange: (Int) -> Unit // 화살표 클릭 시 호출
) {
    val initialPage = 500
    val pagerState = rememberPagerState(initialPage = initialPage) { 1000 }
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    // 💡 외부 상태(currentMonthDate)가 바뀌면 페이저를 해당 페이지로 이동
    LaunchedEffect(currentMonthDate) {
        val startOfMonth = LocalDate(today.year, today.month, 1)
        val targetPage = initialPage + (
                (currentMonthDate.year - startOfMonth.year) * 12 +
                        (currentMonthDate.monthNumber - startOfMonth.monthNumber)
                )
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MapleTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        // 💡 HorizontalPager가 이제 헤더와 요일을 모두 포함합니다.
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) { page ->
            // 해당 페이지의 날짜 계산
            val monthOffset = page - initialPage
            val dateForPage = remember(monthOffset) {
                LocalDate(today.year, today.month, 1).plusMonths(monthOffset)
            }

            // 🚀 한 페이지의 컨텐츠 전체
            Column(modifier = Modifier.padding(16.dp)) {
                // 1. 연월 헤더 (이것도 같이 스와이프됨)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onMonthChange(-1) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = MapleTheme.colors.primary
                        )
                    }
                    Text(
                        text = "${dateForPage.year}년 ${dateForPage.monthNumber}월",
                        style = Typography.bodyLarge,
                        color = MapleTheme.colors.primary
                    )
                    IconButton(onClick = { onMonthChange(1) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MapleTheme.colors.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. 요일 표시 (이것도 같이 스와이프됨)
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                        Text(
                            text = it,
                            style = Typography.bodySmall,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = if (it == "일") Color.Red else if (it == "토") Color.Blue else MapleTheme.colors.outline
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 3. 날짜 그리드
                val daysInMonth = dateForPage.daysInMonth()
                val firstDayOfWeek = dateForPage.dayOfWeek.isoDayNumber % 7
                val totalCells = daysInMonth + firstDayOfWeek

                Column {
                    for (row in 0 until (totalCells + 6) / 7) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (col in 0 until 7) {
                                val cellIndex = row * 7 + col
                                val day = cellIndex - firstDayOfWeek + 1

                                if (day in 1..daysInMonth) {
                                    val date = LocalDate(dateForPage.year, dateForPage.month, day)
                                    val isEventDay = (date in startDate..endDate)
                                    val isSelected = selectedDates.contains(date)

                                    Box(
                                        modifier = Modifier.weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) MapleTheme.colors.primary else Color.Transparent)
                                            .then(if (isEventDay) Modifier.clickable { onDateClick(date) } else Modifier),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            day.toString(),
                                            style = Typography.bodySmall,
                                            color = if (isEventDay) (if (isSelected) MapleTheme.colors.surface else MapleTheme.colors.onSurface) else MapleTheme.colors.outline
                                        )
                                    }
                                } else {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}