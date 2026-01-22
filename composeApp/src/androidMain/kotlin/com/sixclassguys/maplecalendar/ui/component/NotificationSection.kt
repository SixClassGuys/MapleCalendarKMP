package com.sixclassguys.maplecalendar.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sixclassguys.maplecalendar.theme.MapleBlack
import com.sixclassguys.maplecalendar.theme.MapleGray
import com.sixclassguys.maplecalendar.theme.MapleOrange
import com.sixclassguys.maplecalendar.theme.PretendardFamily
import com.sixclassguys.maplecalendar.theme.Typography
import com.sixclassguys.maplecalendar.utils.toMapleNotificationString
import kotlinx.datetime.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationSection(
    isEnabled: Boolean,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit,
    notificationTimes: List<LocalDateTime>
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "알림 설정",
                fontFamily = PretendardFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isEnabled) {
                    IconButton(
                        onClick = { if (isEnabled) onClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = if (isEnabled) MapleOrange else MapleGray
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onToggle,
                    modifier = Modifier.scale(0.8f), // 💡 80% 크기로 줄여서 더 컴팩트하게
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MapleOrange,
                        checkedTrackColor = MapleGray,
                        uncheckedThumbColor = MapleBlack,
                        uncheckedTrackColor = MapleGray
                    )
                )
            }
        }

        Text(
            text = "다음 알림 시간",
            fontFamily = PretendardFamily,
            color = MapleBlack,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (notificationTimes.isEmpty()) {
            Text(
                text = "예약된 알림이 없어요.",
                style = Typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                textAlign = TextAlign.Center,
                color = MapleGray
            )
        } else {
            notificationTimes.sorted().take(3).forEach { time ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTimeFilled,
                        contentDescription = null,
                        tint = MapleOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = time.toMapleNotificationString(),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}