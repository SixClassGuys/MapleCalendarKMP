package com.sixclassguys.maplecalendar.ui.component

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sixclassguys.maplecalendar.domain.model.BossPartyAlarmTime
import com.sixclassguys.maplecalendar.theme.MapleStatBackground
import com.sixclassguys.maplecalendar.theme.MapleStatTitle
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.PretendardFamily
import com.sixclassguys.maplecalendar.theme.Typography
import com.sixclassguys.maplecalendar.util.RegistrationMode
import kotlinx.coroutines.launch

@Composable
fun BossPartyAlarmContent(
    alarms: List<BossPartyAlarmTime>,
    isAlarmOn: Boolean,
    isLeader: Boolean,
    snackbarHostState: SnackbarHostState,
    onToggleAlarm: () -> Unit,
    onAddAlarm: () -> Unit,
    onDeleteAlarm: (Long) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onToggleAlarm()
        } else {
            // 권한이 거부되었을 때
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "알림 권한을 허용하셔야 알림을 받을 수 있어요.",
                    actionLabel = "설정",
                    duration = SnackbarDuration.Long
                )

                // 사용자가 '설정' 버튼을 눌렀을 때 앱 정보 화면으로 이동
                if (result == SnackbarResult.ActionPerformed) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    // 배경 컨테이너 (어두운 회색)
    Column(
        modifier = modifier.fillMaxWidth() // fillMaxSize 대신 fillMaxWidth 사용
            .background(MapleStatBackground, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(16.dp)
    ) {
        // 상단 헤더 영역
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ALARM",
                color = MapleStatTitle,
                style = Typography.titleMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = isAlarmOn,
                    onCheckedChange = { isChecking ->
                        if (isChecking) {
                            // Android 13 이상 대응 (Tiramisu = 33)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                onToggleAlarm()
                            }
                        } else {
                            onToggleAlarm()
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MapleTheme.colors.surface,
                        checkedTrackColor = MapleTheme.colors.primary
                    )
                )
                if (isLeader) {
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(onClick = onAddAlarm) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = MapleTheme.colors.surface)
                    }
                }
            }
        }

        // 🚀 수정 포인트: LazyColumn을 삭제하고 Column + forEach 사용
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .background(MapleTheme.colors.surface, shape = RoundedCornerShape(24.dp))
                .padding(12.dp)
        ) {
            if (alarms.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxHeight(), // 부모 높이만큼 채움
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyEventScreen("예약된 알람이 없어요.")
                    }
                }
            } else {
                items(alarms) { alarm ->
                    BossPartyDetailAlarmItem(
                        isLeader = isLeader,
                        date = alarm.date,
                        time = alarm.time,
                        description = alarm.message,
                        registrationMode = alarm.registrationMode,
                        onDelete = { onDeleteAlarm(alarm.id) }
                    )
                }
            }
        }
    }
}

// 알림 아이템 컴포넌트
@Composable
fun BossPartyDetailAlarmItem(
    isLeader: Boolean,
    date: String,          // "2026년 1월 31일 토요일"
    time: String,          // "19:00"
    description: String,   // "5분 내로 안 오면 추방함"
    registrationMode: RegistrationMode,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp),
        shape = RoundedCornerShape(16.dp), // 조금 더 둥근 모서리
        colors = CardDefaults.cardColors(containerColor = MapleTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 시계 아이콘
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = MapleTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                // 날짜와 시간
                Text(
                    text = "$date $time",
                    fontFamily = PretendardFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MapleTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                // 상세 설명
                Text(
                    text = description,
                    fontFamily = PretendardFamily,
                    fontSize = 13.sp,
                    color = MapleTheme.colors.onSurface
                )
            }

            // 닫기 버튼
            if (registrationMode == RegistrationMode.PERIODIC) {
                // 주기 알람 아이콘 (삭제 불가)
                Icon(
                    imageVector = Icons.Default.Autorenew, // 회전 아이콘으로 '반복' 의미 전달
                    contentDescription = "주기 알람",
                    tint = MapleTheme.colors.outline,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 4.dp)
                )
            } else {
                // 일반 예약 알람은 삭제 가능
                if (isLeader) {
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "삭제",
                            tint = MapleTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}