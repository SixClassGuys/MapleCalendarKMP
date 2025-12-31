package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import com.sixclassguys.maplecalendar.theme.MapleDarkOrange
import com.sixclassguys.maplecalendar.theme.MapleLightOrange
import kotlinx.datetime.LocalDate

@Composable
fun CalendarDayCell(
    date: LocalDate,
    events: List<MapleEvent>, // 이벤트 목록을 받도록 유지
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .aspectRatio(0.5f)
            .padding(2.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(6.dp),
        color = MapleLightOrange
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. 상단 날짜 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MapleDarkOrange)
                    .padding(vertical = 6.dp), // 패딩을 늘려 더 시원하게 보임
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 15.sp, // 폰트 크기 확대
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // 2. 하단 이벤트 영역 (시작/종료 표시)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp) // 막대 간 간격 확대
            ) {
                events.take(3).forEach { event ->
                    // 오늘이 시작일인지 종료일인지 판단
                    val isStart = event.startDate == date
                    val isEnd = event.endDate == date

                    val tagColor = if (isStart) Color(0xFF4CAF50) else Color(0xFFF44336) // 시작은 초록, 종료는 빨강

                    EventBar(title = event.title, tagColor = tagColor)
                }
            }
        }
    }
}