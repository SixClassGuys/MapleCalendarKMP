package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sixclassguys.maplecalendar.domain.model.MapleBgm
import kotlin.math.roundToInt

@Composable
fun DraggableMiniPlayerOverlay(
    bgm: MapleBgm,
    isPlaying: Boolean,
    onTogglePlay: (Boolean) -> Unit,
    onClose: () -> Unit,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // 네비게이션 바 및 하단 바 높이 계산
    val systemBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val bottomBarHeightPx = with(density) { (120.dp + systemBottomPadding).toPx() }
    val statusBarHeightPx = with(density) { WindowInsets.statusBars.asPaddingValues().calculateTopPadding().toPx() }

    // 화면 크기 (px)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }

    // [수정] 세로형 플레이어 예상 크기 (디자인에서 width(140.dp), wrapContentHeight였으므로 넉넉히 설정)
    val playerWidth = with(density) { 140.dp.toPx() }
    val playerHeight = with(density) { 200.dp.toPx() } // 세로형이라 높이가 늘어남

    // 위치 상태 (초기 위치: 우측 하단 구석)
    var offsetX by remember { mutableStateOf(screenWidth - playerWidth - 40f) }
    var offsetY by remember {
        mutableStateOf(screenHeight - playerHeight - bottomBarHeightPx - 40f)
    }

    val animatedX by animateFloatAsState(targetValue = offsetX, label = "x")
    val animatedY by animateFloatAsState(targetValue = offsetY, label = "y")

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.offset { IntOffset(animatedX.roundToInt(), animatedY.roundToInt()) }
                .width(140.dp) // 세로형 디자인 너비에 맞춤
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            // 1. 화면 좌우 밖으로 일정 수준 이상 던지면 종료
                            if (offsetX < -playerWidth / 1.5f || offsetX > screenWidth - playerWidth / 3f) {
                                onClose()
                            } else {
                                // 2. 자석 효과: 화면의 4개 모서리 중 가장 가까운 곳으로 이동

                                // X축 결정 (왼쪽 20f vs 오른쪽 끝)
                                offsetX = if (offsetX + playerWidth / 2 < screenWidth / 2) 40f
                                else screenWidth - playerWidth - 40f

                                // Y축 결정 (상단 50f vs 하단바 위)
                                offsetY = if (offsetY + playerHeight / 2 < screenHeight / 2) {
                                    statusBarHeightPx + 40f // 상단 상태표시줄 아래
                                } else {
                                    screenHeight - playerHeight - bottomBarHeightPx - 40f
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    )
                }
        ) {
            // [세로형 디자인 적용]
            MiniMapleBgmPlayer(
                bgm = bgm,
                isPlaying = isPlaying,
                onTogglePlay = onTogglePlay,
                onClose = onClose,
                onClick = onClick
            )
        }
    }
}