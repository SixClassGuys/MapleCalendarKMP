package com.sixclassguys.maplecalendar.ui.component

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sixclassguys.maplecalendar.domain.model.BossPartyChat
import com.sixclassguys.maplecalendar.theme.MapleBlack
import com.sixclassguys.maplecalendar.theme.MapleGray
import com.sixclassguys.maplecalendar.theme.MapleOrange
import com.sixclassguys.maplecalendar.theme.MapleStatBackground
import com.sixclassguys.maplecalendar.theme.MapleStatTitle
import com.sixclassguys.maplecalendar.theme.MapleWhite
import com.sixclassguys.maplecalendar.theme.Typography
import com.sixclassguys.maplecalendar.util.BossPartyChatMessageType
import com.sixclassguys.maplecalendar.util.BossPartyChatUiItem
import com.sixclassguys.maplecalendar.utils.formatToYmd
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BossPartyChatContent(
    isAlarmOn: Boolean,
    chats: List<BossPartyChat>,
    chatUiItems: List<BossPartyChatUiItem>,
    snackbarHostState: SnackbarHostState,
    isLastPage: Boolean,            // 추가: 마지막 페이지 여부
    isLeader: Boolean,
    isLoading: Boolean,             // 추가: 로딩 상태 (상단 인디케이터용)
    onLoadMore: () -> Unit,         // 추가: 페이지 로드 콜백
    onToggleAlarm: () -> Unit,
    onHide: (Long) -> Unit,
    onReport: (BossPartyChat) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
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
    val internalScrollState = rememberLazyListState()

    // 1. 최상단 스크롤 감지 (페이징 호출)
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = internalScrollState.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            Napier.d("isLoading: $isLoading, isLastPage: $isLastPage, lastVisibleItemIndex: $lastVisibleItemIndex, layoutInfo.totalItemsCount: ${layoutInfo.totalItemsCount}")

            // reverseLayout = true이므로, lastVisibleItemIndex가 커질수록 "과거" 데이터입니다.
            // 즉, 전체 개수에 도달했을 때(리스트 최상단) 로드해야 합니다.
            !isLoading && !isLastPage && chats.isNotEmpty() &&
                    lastVisibleItemIndex >= layoutInfo.totalItemsCount - 5 // 여유있게 5개 전쯤 로드
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        Napier.d("LoadMore: ${shouldLoadMore.value}")
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    LaunchedEffect(chats.size) {
        if (chats.isNotEmpty() && internalScrollState.firstVisibleItemIndex <= 1) {
            internalScrollState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
            .background(MapleStatBackground, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "CHAT",
                color = MapleStatTitle,
                style = Typography.titleMedium
            )

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
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MapleOrange
                )
            )
        }

        // 흰색 채팅 영역
        LazyColumn(
            state = internalScrollState,
            reverseLayout = true, // 💡 리스트를 거꾸로 뒤집음
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(12.dp)
        ) {
            if (isLoading && !isLastPage) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(MapleBlack.copy(alpha = 0.7f)) // 화면 어둡게 처리
                            .pointerInput(Unit) {}, // 터치 이벤트 전파 방지 (클릭 막기)
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = MapleOrange,
                                strokeWidth = 4.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "채팅 내역을 불러오고 있습니다...",
                                color = MapleWhite,
                                style = Typography.bodyLarge
                            )
                        }
                    }
                }
            }

            if (chatUiItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxHeight(), // 부모 높이만큼 채움
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyEventScreen("아직 채팅이 없어요.")
                    }
                }
            } else {
                items(
                    items = chatUiItems,
                    key = { item ->
                        when (item) {
                            is BossPartyChatUiItem.Message -> "${item.chat.messageType}_${item.chat.id}"
                            is BossPartyChatUiItem.DateDivider -> "date_${item.date}"
                        }
                    }
                ) { item ->
                    Box(
                        modifier = Modifier.animateItem() // ✨ 삭제/추가 시 애니메이션 발생!
                    ) {
                        when (item) {
                            is BossPartyChatUiItem.Message -> ChatBubble(
                                chat = item.chat,
                                isLeader = isLeader,
                                showProfile = item.showProfile,
                                onHide = onHide,
                                onReport = onReport,
                                onDelete = onDelete
                            )

                            is BossPartyChatUiItem.DateDivider -> DateDivider(item.date)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ChatBubble(
    chat: BossPartyChat,
    isLeader: Boolean,
    showProfile: Boolean,
    onHide: (Long) -> Unit,
    onReport: (BossPartyChat) -> Unit,
    onDelete: (Long) -> Unit
) {
    when (chat.messageType) {
        BossPartyChatMessageType.JOINED, BossPartyChatMessageType.KICKED -> {
            SystemChatBubble(chat)
        }

        BossPartyChatMessageType.ENTER, BossPartyChatMessageType.LEAVE -> {}

        else -> {
            when {
                chat.isHidden -> SystemChatBubble(chat.copy(content = "가려진 메시지입니다."))

                chat.isDeleted -> SystemChatBubble(chat)

                else -> UserChatBubble(
                    chat = chat,
                    isLeader = isLeader,
                    showProfile = showProfile,
                    onHide = onHide,
                    onReport = onReport,
                    onDelete = onDelete
                )
            }
        }
    }
}

@Composable
fun SystemChatBubble(chat: BossPartyChat) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.05f), // 아주 연한 회색 배경
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = chat.content,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 11.sp,
                color = MapleGray, // 기존에 정의하신 회색
                style = Typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserChatBubble(
    chat: BossPartyChat,
    isLeader: Boolean,
    showProfile: Boolean,
    onHide: (Long) -> Unit,
    onReport: (BossPartyChat) -> Unit,
    onDelete: (Long) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current // 롱클릭 시 진동 효과용

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = if (showProfile) 8.dp else 2.dp), // 연속 메시지는 간격을 좁게
        horizontalArrangement = if (chat.isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom // 시간 표시를 아래쪽에 맞추기 위함
    ) {
        // 1. 상대방 프로필/이름 영역
        if (!chat.isMine) {
            if (showProfile) {
                CharacterProfileImage(imageUrl = chat.senderImage, size = 40.dp)
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Spacer(modifier = Modifier.width(48.dp)) // 프로필 이미지(40) + 간격(8)
            }
        }

        // 2. 내 메시지일 때 시간 표시 (버블 왼쪽)
        if (chat.isMine) {
            ChatTimeText(chat.createdAt)
        }

        // 3. 메시지 버블 및 팝업 메뉴
        Column(
            horizontalAlignment = if (chat.isMine) Alignment.End else Alignment.Start
        ) {
            if (!chat.isMine && showProfile) {
                Text(
                    text = chat.senderName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Box { // 🚀 메뉴 위치를 잡기 위한 Box
                Surface(
                    color = if (chat.isMine) MapleOrange else MapleGray,
                    shape = RoundedCornerShape(
                        topStart = if (chat.isMine) 16.dp else 4.dp,
                        topEnd = if (!chat.isMine) 16.dp else 4.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    modifier = Modifier.combinedClickable(
                        onClick = { /* 필요 시 구현 */ },
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isMenuExpanded = true
                        }
                    )
                ) {
                    Text(
                        text = chat.content,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 14.sp,
                        color = if (chat.isMine) MapleWhite else MapleBlack
                    )
                }

                // 🚀 삭제 메뉴
                DropdownMenu(
                    expanded = isMenuExpanded,
                    containerColor = MapleWhite,
                    onDismissRequest = { isMenuExpanded = false }
                ) {
                    if (isLeader) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "가리기",
                                    style = Typography.bodySmall
                                )
                            },
                            onClick = {
                                onHide(chat.id)
                                isMenuExpanded = false
                            }
                        )
                    }
                    if (chat.isMine) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "삭제하기",
                                    style = Typography.bodySmall
                                )
                            },
                            onClick = {
                                onDelete(chat.id)
                                isMenuExpanded = false
                            }
                        )
                    } else {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "신고하기",
                                    style = Typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            },
                            onClick = {
                                onReport(chat)
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // 4. 상대방 메시지일 때 시간 표시 (버블 오른쪽)
        if (!chat.isMine) {
            ChatTimeText(chat.createdAt)
        }
    }
}

@Composable
fun ChatTimeText(timeStr: String) {
    // yyyy-MM-dd'T'HH:mm:ss 형태에서 HH:mm만 추출 (간단하게 substring)
    val displayTime = if (timeStr.length >= 16) timeStr.substring(11, 16) else ""

    Text(
        text = displayTime,
        fontSize = 10.sp,
        color = MapleGray,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateDivider(date: String) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 12.dp), // 간격 조절
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.05f),
            shape = RoundedCornerShape(8.dp) // 약간 각진 느낌도 잘 어울립니다
        ) {
            Text(
                text = formatToYmd(date),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                style = Typography.bodySmall,
                fontSize = 13.sp, // 날짜는 조금 작게
                color = MapleGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}