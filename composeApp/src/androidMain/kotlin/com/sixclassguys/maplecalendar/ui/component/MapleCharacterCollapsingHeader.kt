package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sixclassguys.maplecalendar.presentation.character.MapleCharacterUiState
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.Typography

@Composable
fun MapleCharacterCollapsingHeader(
    uiState: MapleCharacterUiState,
    currentHeightPx: Float,
    maxHeaderHeightPx: Float,
    minHeaderHeightPx: Float,
    onBack: () -> Unit,
    onWorldClick: () -> Unit
) {
    val scrollPercentage = ((maxHeaderHeightPx - currentHeightPx) / (maxHeaderHeightPx - minHeaderHeightPx)).coerceIn(0f, 1f)
    val density = LocalDensity.current
    val currentHeightDp = with(density) { currentHeightPx.toDp() }

    Surface(
        modifier = Modifier.fillMaxWidth().height(currentHeightDp),
        color = MapleTheme.colors.surface,
        shadowElevation = if (scrollPercentage > 0.95f) 4.dp else 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 🚀 핵심: 뒤로가기 + 타이틀 + 월드아이콘을 하나의 Row로 관리 (수평 일치)
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(if (scrollPercentage > 0.8f) 72.dp else 80.dp)
                    .align(Alignment.TopCenter), // 무조건 상단에 붙임
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. 뒤로가기 버튼 (항상 왼쪽 고정)
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(32.dp)
                        .offset(x = (-4).dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // 2. 캐릭터 등록하기 타이틀
                Text(
                    text = "캐릭터 등록하기",
                    fontSize = lerp(24f, 18f, scrollPercentage).sp,
                    fontWeight = FontWeight.Bold,
                    color = MapleTheme.colors.onSurface,
                    maxLines = 1, // 타이틀 잘림 방지
                    softWrap = false // 텍스트가 절대 다음 줄로 넘어가지 않게 강제
                )

                // 3. 월드 선택기 (우상단 수평 정렬)
                // 확장 시에는 보이지 않다가 0.8 이상일 때만 나타나거나,
                // 혹은 위치를 lerp로 조절하여 Row 안에 안착시킵니다.
                if (scrollPercentage > 0.8f) {
                    WorldSelector(
                        selectedWorld = uiState.selectedFetchWorld,
                        onWorldClick = onWorldClick,
                        isIconOnly = true // 축소 모드
                    )
                }
            }

            // 4. 확장 시에만 보이는 요소들 (인디케이터, 안내문구, 확장용 월드선택기)
            if (scrollPercentage < 0.6f) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 85.dp, start = 16.dp, end = 16.dp)
                        .graphicsLayer { alpha = 1f - (scrollPercentage * 2.5f) }
                ) {
                    CharacterStepIndicator(
                        currentStep = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "계정 내에서 Maplendar에 등록을 원하는\n" +
                                "캐릭터를 선택해주세요!\n" +
                                "등록을 원하지 않으시면 체크를 해제해주시면 돼요!",
                        style = Typography.bodyMedium,
                        color = MapleTheme.colors.outline
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 확장된 상태에서의 월드 선택기 (가운데 정렬)
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        WorldSelector(
                            selectedWorld = uiState.selectedFetchWorld,
                            onWorldClick = onWorldClick,
                            isIconOnly = false // 텍스트 포함 모드
                        )
                    }
                }
            }
        }
    }
}