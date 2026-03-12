package com.sixclassguys.maplecalendar.ui.setting

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sixclassguys.maplecalendar.presentation.home.HomeIntent
import com.sixclassguys.maplecalendar.presentation.home.HomeViewModel
import com.sixclassguys.maplecalendar.presentation.playlist.PlaylistIntent
import com.sixclassguys.maplecalendar.presentation.playlist.PlaylistViewModel
import com.sixclassguys.maplecalendar.presentation.setting.SettingIntent
import com.sixclassguys.maplecalendar.presentation.setting.SettingViewModel
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    homeViewModel: HomeViewModel,
    playlistViewModel: PlaylistViewModel,
    snackbarHostState: SnackbarHostState,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            homeViewModel.onIntent(HomeIntent.ToggleGlobalAlarmStatus)
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

    LaunchedEffect(homeUiState.isLoginSuccess) {
        if (!homeUiState.isLoginSuccess) {
            playlistViewModel.onIntent(PlaylistIntent.ClosePlayer)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message = message)
            viewModel.onIntent(SettingIntent.InitErrorMessage)
        }
    }

    LaunchedEffect(homeUiState.errorMessage) {
        val message = homeUiState.errorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message = message)
            homeViewModel.onIntent(HomeIntent.InitErrorMessage)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MapleTheme.colors.surface)
            .padding(24.dp)
    ) {
        Text(
            text = "환경설정",
            style = Typography.titleLarge,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // 2. 다크모드 섹션
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "다크모드 설정",
                style = Typography.labelLarge
            )
            Switch(
                checked = uiState.isDarkModeEnabled,
                onCheckedChange = { isChecking ->
                    viewModel.onIntent(SettingIntent.ToggleDarkModeStatus(isChecking))
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MapleTheme.colors.primary,
                    checkedTrackColor = MapleTheme.colors.primary.copy(alpha = 0.5f)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 💡 로그인 상태에 따른 UI 분기
        if (!homeUiState.isLoginSuccess) {
            MapleButton(
                text = "로그인",
                onClick = onNavigateToLogin,
                containerColor = Color(0xFFF29F38)
            )
        } else {
            // 1. 알림 설정 섹션
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "이벤트 알림 수신",
                    style = Typography.labelLarge
                )
                Switch(
                    checked = homeUiState.isGlobalAlarmEnabled,
                    onCheckedChange = { isChecking ->
                        if (isChecking) {
                            // Android 13 이상 대응 (Tiramisu = 33)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                homeViewModel.onIntent(HomeIntent.ToggleGlobalAlarmStatus)
                            }
                        } else {
                            // OFF로 바꿀 때는 권한 요청 필요 없음
                            homeViewModel.onIntent(HomeIntent.ToggleGlobalAlarmStatus)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MapleTheme.colors.primary, // 메이플 주황색
                        checkedTrackColor = MapleTheme.colors.primary.copy(alpha = 0.5f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 3. 로그아웃 버튼
            MapleButton(
                text = "로그아웃",
                onClick = {
                    viewModel.onIntent(SettingIntent.Logout)
                    homeViewModel.onIntent(HomeIntent.Logout)
                    Toast.makeText(context, "로그아웃에 성공했어요.", Toast.LENGTH_SHORT).show()
                },
                containerColor = Color(0xFFFF7E7E) // 로그아웃용 붉은 계열
            )
        }

        Spacer(modifier = Modifier.weight(1.2f))
    }
}

@Composable
fun MapleButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        Text(
            text = text,
            style = Typography.bodyLarge,
            color = MapleTheme.colors.surface
        )
    }
}