package com.sixclassguys.maplecalendar.ui.calendar

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sixclassguys.maplecalendar.presentation.calendar.CalendarIntent
import com.sixclassguys.maplecalendar.presentation.calendar.CalendarViewModel
import com.sixclassguys.maplecalendar.theme.MapleGray
import com.sixclassguys.maplecalendar.theme.Typography
import com.sixclassguys.maplecalendar.ui.component.AlarmSettingDialog
import com.sixclassguys.maplecalendar.ui.component.EventCollapsingHeader
import com.sixclassguys.maplecalendar.ui.component.EventDetailHeader
import com.sixclassguys.maplecalendar.ui.component.EventWebView
import com.sixclassguys.maplecalendar.ui.component.NotificationSection
import kotlinx.coroutines.launch

// 상단 바의 높이 설정 (dp 단위)
val IMAGE_HEIGHT = 200.dp
val COLLAPSED_TOP_BAR_HEIGHT = 48.dp // 제목+날짜 2줄을 위해 기존보다 조금 높임
val EXPANDED_TOP_BAR_HEIGHT = 280.dp // 이미지(200) + 간격(16) + 제목/날짜 영역

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapleEventDetailScreen(
    viewModel: CalendarViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onIntent(CalendarIntent.ToggleNotification)
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

    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.selectedEvent) {
        if (uiState.selectedEvent == null) {
            onBack()
        }
    }

    val event = uiState.selectedEvent ?: return // 이벤트가 없으면 표시 안함
    val currentEvent by rememberUpdatedState(event)

    val density = LocalDensity.current
    val collapsedHeightPx = with(density) { COLLAPSED_TOP_BAR_HEIGHT.toPx() }
    val expandedHeightPx = with(density) { EXPANDED_TOP_BAR_HEIGHT.toPx() }
    val maxScrollOffsetPx = expandedHeightPx - collapsedHeightPx

    // 전체 스크롤 오프셋 (0 ~ -maxScrollOffsetPx)
    var toolbarOffsetHeightPx by remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx + delta

                // 💡 핵심: 헤더가 움직이는 동안에는 스크롤을 소비하여
                // 헤더와 바디가 한 몸처럼 움직이게 합니다.
                val oldOffset = toolbarOffsetHeightPx
                toolbarOffsetHeightPx = newOffset.coerceIn(-maxScrollOffsetPx, 0f)
                val consumed = toolbarOffsetHeightPx - oldOffset

                return Offset(0f, consumed)
            }
        }
    }
    val scrollPercentage = -toolbarOffsetHeightPx / maxScrollOffsetPx

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(currentEvent.id) { // 👈 key를 event.id로 설정하여 id가 바뀌면 effect 재실행
        val observer = LifecycleEventObserver { _, lifecycleEvent ->
            if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
                // 💡 항상 최신 ID로 요청
                viewModel.onIntent(CalendarIntent.SelectEvent(currentEvent.id))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message = message)
            viewModel.onIntent(CalendarIntent.InitErrorMessage)
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
                .nestedScroll(nestedScrollConnection) // 👈 핵심: 스크롤 연결
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .offset { IntOffset(0, (expandedHeightPx + toolbarOffsetHeightPx).toInt()) }
                    .verticalScroll(scrollState) // 전체 스크롤 허용
            ) {
                // 2. 헤더 정보 (제목, 공유, 날짜, 태그)
                EventDetailHeader(
                    event = event
                )

                HorizontalDivider(thickness = 1.dp, color = MapleGray)

                // 3. 알림 설정 섹션
                NotificationSection(
                    isEnabled = uiState.isNotificationEnabled,
                    onClick = { viewModel.onIntent(CalendarIntent.ShowAlarmDialog(true)) },
                    onToggle = {
                        if (uiState.isGlobalAlarmEnabled) {
                            if (uiState.isNotificationEnabled) {
                                // Android 13 이상 대응 (Tiramisu = 33)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    viewModel.onIntent(CalendarIntent.ToggleNotification)
                                }
                            } else {
                                // OFF로 바꿀 때는 권한 요청 필요 없음
                                viewModel.onIntent(CalendarIntent.ToggleNotification)
                            }
                        } else {
                            Toast.makeText(context, "전체 알림을 먼저 허용해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    notificationTimes = uiState.scheduledNotifications
                )

                HorizontalDivider(thickness = 8.dp, color = MapleGray)

                // 4. 홈페이지 상세 (WebView)
                Text(
                    text = "홈페이지",
                    style = Typography.titleSmall,
                    modifier = Modifier.padding(16.dp)
                )

                // WebView를 Compose에서 사용하기 위해 AndroidView 활용
                EventWebView(
                    url = event.url,
                    parentScrollState = scrollState
                )// 💡 하단 여백 추가 (웹뷰 끝까지 내리기 편하게)
                Spacer(modifier = Modifier.height(50.dp))
            }

            EventCollapsingHeader(
                event = event,
                currentHeightPx = expandedHeightPx + toolbarOffsetHeightPx,
                scrollPercentage = scrollPercentage,
                onBack = onBack,
                onShare = {
                    Toast.makeText(context, "준비중인 기능이에요.", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    if (uiState.showAlarmDialog) {
        AlarmSettingDialog(
            viewModel = viewModel,
            event = uiState.selectedEvent!!,
            onDismiss = {
                viewModel.onIntent(CalendarIntent.ShowAlarmDialog(false))
            },
            onSubmit = { alarmList ->
                // 앞서 만든 TreeMap 기반 제출 로직 실행
                viewModel.onIntent(
                    CalendarIntent.SubmitNotificationTimes(
                        uiState.selectedEvent!!.id,
                        alarmList
                    )
                )
                viewModel.onIntent(CalendarIntent.ShowAlarmDialog(false))
            }
        )
    }
}