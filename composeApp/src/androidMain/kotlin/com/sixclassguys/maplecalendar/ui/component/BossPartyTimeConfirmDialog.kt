package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BossPartyTimeConfirmDialog(
    viewModel: BossViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Dialog(onDismissRequest = {
        if (!uiState.isScheduleUpdating) {
            onDismiss()
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            color = MapleStatBackground
        ) {
            when {
                uiState.isLoadingCandidates -> Box(
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
                            text = "가능한 시간대를 불러오는 중이에요...",
                            color = MapleWhite,
                            style = Typography.bodyLarge
                        )
                    }
                }

                uiState.isScheduleConfirming -> Box(
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
                            text = "시간대를 확정하는 중이에요...",
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
                        // 1. 타이틀 상단 헤더
                        Text(
                            text = "TIME CONFIRM",
                            style = Typography.titleMedium,
                            color = MapleStatTitle,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // 2. 🔄 가능한 시간대 후보 리스트 (선택형 래핑 카드 구조)
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MapleTheme.colors.surface) // 하얀 내부 보드 배경
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (uiState.scheduleCandidates.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillParentMaxHeight(), // 부모 높이만큼 채움
                                        contentAlignment = Alignment.Center
                                    ) {
                                        EmptyEventScreen("가능한 시간대가 없어요.")
                                    }
                                }
                            }
                            else {

                                itemsIndexed(uiState.scheduleCandidates) { _, item ->
                                    val isSelected = uiState.selectedScheduleCandidate == item

                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            // 와이어프레임 특유의 입체감과 선택 피드백 테두리
                                            .background(MapleTheme.colors.surface)
                                            .border(
                                                width = if (isSelected) 2.dp else 0.5.dp,
                                                color = if (isSelected) MapleTheme.colors.primary else Color(0xFFE0E0E0),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .clickable { viewModel.onIntent(BossIntent.SelectBossPartyScheduleCandidate(item)) }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // 시계 아이콘 영역
                                        Box(
                                            modifier = Modifier.size(36.dp)
                                                .background(
                                                    color = if (isSelected) MapleTheme.colors.primary else Color(0xFFF5F5F5),
                                                    shape = RoundedCornerShape(50)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LockClock,
                                                contentDescription = "시간 아이콘",
                                                tint = if (isSelected) Color.White else MapleTheme.colors.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        // 요일 및 시간 정보 텍스트
                                        Column {
                                            Text(
                                                text = item.dayOfWeek,
                                                color = MapleTheme.colors.onSurface,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = item.timeRange,
                                                color = Color.Gray,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 3. 💬 알람 메시지 입력 섹션
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MapleTheme.colors.surface, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "알람 메시지",
                                color = MapleTheme.colors.onSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )

                            OutlinedTextField(
                                value = uiState.confirmAlarmMessage,
                                onValueChange = { viewModel.onIntent(BossIntent.UpdateBossPartyConfirmMessage(it)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        if (uiState.selectedScheduleCandidate != null) {
                                            viewModel.onIntent(BossIntent.ConfirmBossPartySchedule)
                                        }
                                    }
                                ),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 4. 하단 버튼 컨트롤러
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = onDismiss
                            ) {
                                Text("취소", color = MapleTheme.colors.surface, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (uiState.selectedScheduleCandidate != null) {
                                        viewModel.onIntent(BossIntent.ConfirmBossPartySchedule)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MapleTheme.colors.primary),
                                enabled = uiState.selectedScheduleCandidate != null
                            ) {
                                Text("최종 확정", color = MapleTheme.colors.surface, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}