package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sixclassguys.maplecalendar.presentation.playlist.PlaylistIntent
import com.sixclassguys.maplecalendar.presentation.playlist.PlaylistViewModel
import com.sixclassguys.maplecalendar.theme.MapleStatBackground
import com.sixclassguys.maplecalendar.theme.MapleStatTitle
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.Typography

@Composable
fun AddMyPlaylistDialog(
    viewModel: PlaylistViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current // 💡 현재 다이얼로그의 Native View
    val dummyFocusRequester = remember { FocusRequester() }
    val descriptionFocusRequester = remember { FocusRequester() }
    val clearFocusAll = {
        // Compose 레벨에서 포커스 해제
        focusManager.clearFocus(force = true)
        // 가짜 타겟으로 포커스 강제 이동 (TextField에서 포커스 뺏기)
        dummyFocusRequester.requestFocus()
        // 네이티브 뷰 레벨에서 포커스 해제 (이게 핵심)
        view.clearFocus()
        // 키보드 숨기기
        keyboardController?.hide()
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier.size(0.dp)
                .focusRequester(dummyFocusRequester)
                .focusable()
        )

        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MapleStatBackground)
                .padding(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { clearFocusAll() },
        ) {
            Text(
                text = "NEW PLAYLIST",
                color = MapleStatTitle,
                style = Typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MapleTheme.colors.surface
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .background(MapleTheme.colors.onSurface.copy(alpha = 0.7f)) // 화면 어둡게 처리
                                .pointerInput(Unit) {}, // 터치 이벤트 전파 방지 (클릭 막기)
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(
                                    color = MapleTheme.colors.primary,
                                    strokeWidth = 4.dp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "플레이리스트를 추가하는 중이에요...",
                                    color = MapleTheme.colors.surface,
                                    style = Typography.bodyLarge
                                )
                            }
                        }
                    }

                    else -> {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // 제목 입력
                            PlaylistInputField(
                                label = "제목",
                                value = uiState.newPlaylistName,
                                onValueChange = { viewModel.onIntent(PlaylistIntent.UpdateNewMapleBgmPlaylistName(it)) },
                                placeholder = "플레이리스트 제목을 입력하세요"
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 설명 입력
                            PlaylistInputField(
                                label = "설명",
                                value = uiState.newPlaylistDescription,
                                onValueChange = { viewModel.onIntent(PlaylistIntent.UpdateNewMapleBgmPlaylistDescription(it)) },
                                placeholder = "플레이리스트 내용을 입력하세요"
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 공개 범위 설정
                            Text("공개 범위", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .clickable { viewModel.onIntent(PlaylistIntent.UpdateNewMapleBgmPlaylistPublicStatus) }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (uiState.isNewPlaylistPublic) Icons.Default.Public else Icons.Default.Lock,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(if (uiState.isNewPlaylistPublic) "공개" else "비공개", fontSize = 15.sp)
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 만들기 버튼
                            val isEnabled = uiState.newPlaylistName.isNotBlank() && uiState.newPlaylistDescription.isNotBlank()
                            Button(
                                enabled = isEnabled,
                                onClick = {
                                    if (isEnabled) {
                                        viewModel.onIntent(PlaylistIntent.CreateMapleBgmPlaylist)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MapleTheme.colors.primary) // 주황색 버튼
                            ) {
                                Text("만들기", color = if (isEnabled) MapleTheme.colors.surface else MapleTheme.colors.onSurface, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            textStyle = TextStyle(fontSize = 15.sp),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(text = placeholder, color = MapleTheme.colors.outline, fontSize = 15.sp)
                }
                innerTextField()
            }
        )
    }
}