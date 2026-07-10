package com.sixclassguys.maplecalendar.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sixclassguys.maplecalendar.R
import com.sixclassguys.maplecalendar.presentation.boss.BossIntent
import com.sixclassguys.maplecalendar.presentation.boss.BossViewModel
import com.sixclassguys.maplecalendar.theme.MapleBlack
import com.sixclassguys.maplecalendar.theme.MapleStatBackground
import com.sixclassguys.maplecalendar.theme.MapleStatTitle
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.MapleWhite
import com.sixclassguys.maplecalendar.theme.Typography

@SuppressLint("DefaultLocale")
@Composable
fun BossPartyTimeSelectDialog(
    viewModel: BossViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showMenu by remember { mutableStateOf(false) }

    val days = listOf("일", "월", "화", "수", "목", "금", "토")
    val totalTimeRows = 72

    // 틀 고정을 위한 핵심 스크롤 구조 설계
    val globalHorizontalScrollState = rememberScrollState()
    val globalVerticalScrollState = rememberScrollState()

    Dialog(onDismissRequest = {
        if (!uiState.isScheduleUpdating) {
            onDismiss()
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            color = MapleStatBackground
        ) {
            when {
                uiState.isScheduleUpdating -> Box(
                    modifier = Modifier.fillMaxSize()
                        .background(MapleBlack.copy(alpha = 0.7f))
                        .pointerInput(Unit) {},
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = R.drawable.ic_loading, // 주황버섯 GIF
                            contentDescription = "로딩 중",
                            modifier = Modifier.size(120.dp)
                                .graphicsLayer {
                                    scaleX = -1f // 좌우대칭
                                },
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "가능한 시간대를 변경하는 중이에요...",
                            color = MapleWhite,
                            style = Typography.bodyLarge
                        )
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // 1. 헤더 영역 (타이틀 + 대량 작업 메뉴 버튼)
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TIME SELECT",
                                style = Typography.titleMedium,
                                color = MapleStatTitle,
                                fontWeight = FontWeight.Bold
                            )

                            // 💡 상단 컨텍스트 드롭다운 메뉴 (전체 체크/해제)
                            Box {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        contentDescription = "메뉴",
                                        tint = MapleTheme.colors.onSurface
                                    )
                                }
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("전체 선택") },
                                        onClick = {
                                            viewModel.onIntent(BossIntent.UpdateBossPartyAbleSchedule("1".repeat(504)))
                                            showMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("전체 해제") },
                                        onClick = {
                                            viewModel.onIntent(BossIntent.UpdateBossPartyAbleSchedule("0".repeat(504)))
                                            showMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        // 2. 틀 고정 메인 레이아웃 (테이블 구조)
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MapleTheme.colors.background)
                        ) {
                            // [행 고정 섹션] 최상단 요일 라인 헤더 (세로 스크롤 시 고정, 가로만 동기화)
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .background(MapleTheme.colors.primary)
                                    .border(0.5.dp, MapleTheme.colors.outline),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "시간",
                                    modifier = Modifier.width(65.dp).padding(vertical = 12.dp),
                                    textAlign = TextAlign.Center,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MapleTheme.colors.outline
                                )

                                Row(
                                    modifier = Modifier.weight(1f)
                                        .horizontalScroll(globalHorizontalScrollState) // 가로축 공유
                                ) {
                                    days.forEachIndexed { dayIdx, day ->
                                        Text(
                                            text = day,
                                            modifier = Modifier.width(55.dp)
                                                .padding(vertical = 12.dp)
                                                .clickable {
                                                    val startIdx = dayIdx * 72
                                                    val endIdx = startIdx + 72
                                                    val targetSection = uiState.newAvailableSlots.substring(startIdx, endIdx)
                                                    val nextChar = if (targetSection.contains('0')) '1' else '0'
                                                    val sb = StringBuilder(uiState.newAvailableSlots)
                                                    for (i in startIdx until endIdx) { sb.setCharAt(i, nextChar) }
                                                    viewModel.onIntent(BossIntent.UpdateBossPartyAbleSchedule(sb.toString()))
                                                },
                                            textAlign = TextAlign.Center,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (day == "일") Color.Red else if (day == "토") Color.Blue else MapleTheme.colors.surface
                                        )
                                    }
                                }
                            }

                            // 🔄 [양방향 대각선 스크롤 본문 바디]
                            Row(
                                modifier = Modifier.fillMaxWidth().weight(1f)
                            ) {
                                // [열 고정 섹션] 좌측 시간 타임 라벨 리스트 (가로 스크롤 시 고정, 세로만 본문과 동기화)
                                Column(
                                    modifier = Modifier.width(65.dp)
                                        .verticalScroll(globalVerticalScrollState) // 세로축 공유
                                ) {
                                    for (timeRowIndex in 0 until totalTimeRows) {
                                        val hour = timeRowIndex / 3
                                        val minute = (timeRowIndex % 3) * 20
                                        val timeLabel = String.format("%02d:%02d", hour, minute)

                                        Text(
                                            text = timeLabel,
                                            modifier = Modifier.fillMaxWidth()
                                                .height(45.dp) // 본문 셀 높이와 일치
                                                .background(MapleTheme.colors.primary)
                                                .border(0.25.dp, MapleTheme.colors.outline)
                                                .wrapContentHeight(Alignment.CenterVertically)
                                                .clickable {
                                                    val targetIndexes = (0..6).map { (it * 72) + timeRowIndex }
                                                    val isAllChecked = targetIndexes.all { uiState.newAvailableSlots[it] == '1' }
                                                    val nextChar = if (isAllChecked) '0' else '1'
                                                    val sb = StringBuilder(uiState.newAvailableSlots)
                                                    targetIndexes.forEach { sb.setCharAt(it, nextChar) }
                                                    viewModel.onIntent(BossIntent.UpdateBossPartyAbleSchedule(sb.toString()))
                                                },
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MapleTheme.colors.surface
                                        )
                                    }
                                }

                                // 💡 [대각선 스크롤 핵심] 본문 데이터 격자창
                                // 가로 세로 스크롤 상태가 모두 주입되어 대각선으로 부드럽게 밀립니다.
                                Box(
                                    modifier = Modifier.weight(1f)
                                        .fillMaxSize()
                                        .horizontalScroll(globalHorizontalScrollState)
                                        .verticalScroll(globalVerticalScrollState)
                                ) {
                                    Column {
                                        for (timeRowIndex in 0 until totalTimeRows) {
                                            Row {
                                                for (dayIdx in 0..6) {
                                                    val slotGlobalIndex = (dayIdx * 72) + timeRowIndex
                                                    val isChecked = uiState.newAvailableSlots.getOrNull(slotGlobalIndex) == '1'

                                                    Box(
                                                        modifier = Modifier.width(55.dp).height(45.dp)
                                                            .border(0.25.dp, MapleTheme.colors.outline)
                                                            .clickable {
                                                                val newChar = if (isChecked) '0' else '1'
                                                                val sb = StringBuilder(uiState.newAvailableSlots)
                                                                sb.setCharAt(slotGlobalIndex, newChar)
                                                                viewModel.onIntent(BossIntent.UpdateBossPartyAbleSchedule(sb.toString()))
                                                            },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        if (isChecked) {
                                                            Box(
                                                                modifier = Modifier.size(18.dp)
                                                                    .background(MapleTheme.colors.primary, RoundedCornerShape(50))
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

                        Spacer(modifier = Modifier.height(12.dp))

                        // 3. 하단 옵션: 선택한 시간 정보 기억하기
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .background(MapleTheme.colors.surface, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "선택한 시간 정보 기억하기",
                                color = MapleTheme.colors.onSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Checkbox(
                                checked = uiState.newKeepNextWeek,
                                onCheckedChange = {
                                    viewModel.onIntent(BossIntent.UpdateBossPartyScheduleKeep(!uiState.newKeepNextWeek))
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF7941D))
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 4. 액션 버튼 바
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { viewModel.onIntent(BossIntent.DismissBossPartyTimeSelectDialog) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text(
                                    "취소",
                                    color = MapleTheme.colors.surface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.onIntent(BossIntent.SubmitBossPartyAbleSchedule) },
                                colors = ButtonDefaults.buttonColors(containerColor = MapleTheme.colors.primary)
                            ) {
                                Text(
                                    "저장",
                                    color = MapleTheme.colors.surface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}