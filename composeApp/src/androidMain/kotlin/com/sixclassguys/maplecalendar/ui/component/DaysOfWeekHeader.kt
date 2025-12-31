package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DaysOfWeekHeader(
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f), // 7칸을 균등하게 배분
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = when (day) {
                    "일" -> Color.Red
                    "토" -> Color.Blue
                    else -> Color.Black
                }
            )
        }
    }
}