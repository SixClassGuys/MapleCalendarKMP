package com.sixclassguys.maplecalendar.ui.component

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sixclassguys.maplecalendar.R
import com.sixclassguys.maplecalendar.navigation.Navigation
import com.sixclassguys.maplecalendar.theme.MapleOrange
import com.sixclassguys.maplecalendar.theme.MapleTheme

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    isLoginSuccess: Boolean,
    onFetchEvent: () -> Unit
) {
    val context = LocalContext.current
    val navItems = listOf(
        Navigation.Home, Navigation.Playlist, Navigation.Board, Navigation.Setting
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val systemBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // 1. 상태 추가: 메뉴가 열려있는지 여부
    var isExpanded by remember { mutableStateOf(false) }

    val animProgress by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Box(
        modifier = modifier.fillMaxWidth()
            .height(90.dp + systemBottomPadding)
            .background(Color.Transparent)
            .pointerInput(Unit) {} // 하단 터치 전파 방지
    ) {
        // 1. 배경 Canvas (와이어프레임처럼 주황색 적용)
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            val fabRadius = 38.dp.toPx()
            val fabSpacing = 8.dp.toPx()
            val cutoutRadius = fabRadius + fabSpacing
            val centerX = width / 2

            // 와이어프레임의 부드러운 곡선 느낌 재현
            val curveWidth = cutoutRadius * 1.5f
            val curveHeight = cutoutRadius * 0.8f

            val basePath = Path().apply {
                moveTo(0f, 20.dp.toPx()) // 상단 시작점
                lineTo(centerX - curveWidth, 20.dp.toPx())
                cubicTo(
                    x1 = centerX - curveWidth / 2f, y1 = 20.dp.toPx(),
                    x2 = centerX - cutoutRadius, y2 = 20.dp.toPx() + curveHeight,
                    x3 = centerX, y3 = 20.dp.toPx() + curveHeight
                )
                cubicTo(
                    x1 = centerX + cutoutRadius, y1 = 20.dp.toPx() + curveHeight,
                    x2 = centerX + curveWidth / 2f, y2 = 20.dp.toPx(),
                    x3 = centerX + curveWidth, y3 = 20.dp.toPx()
                )
                lineTo(width, 20.dp.toPx())
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = basePath,
                color = MapleOrange,
                style = Fill
            )
        }

        // 2. 아이콘 영역
        Row(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = systemBottomPadding + 8.dp) // 시스템 바 위로 배치
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEachIndexed { index, item ->
                if (index == 2) Spacer(modifier = Modifier.width(70.dp))

                MapleBottomNavItem(
                    item = item,
                    isSelected = currentRoute == item.destination,
                    onClick = {
                        // BGM 플레이리스트와 게시판 기능은 준비중
                        if (item == Navigation.Board) {
                            Toast.makeText(context, "준비중이에요.", Toast.LENGTH_SHORT).show()
                        } else if (item == Navigation.Playlist && !isLoginSuccess) {
                            Toast.makeText(context, "로그인이 필요해요.", Toast.LENGTH_SHORT).show()
                            navController.navigate("login_flow")
                        } else {
//                            navController.navigate(item.destination) {
//                                popUpTo(navController.graph.startDestinationId)
//                                launchSingleTop = true
//                            }
                            if (currentRoute != item.destination) {
                                navController.navigate(item.destination) {
                                    // 💡 핵심: 현재 스택에 있는 모든 화면을 제거하고 이동합니다.
                                    // 이렇게 하면 항상 스택에는 현재 화면 '딱 하나'만 남게 됩니다.
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                )
            }
        }

        if (animProgress > 0f) {
            Box(
                modifier = Modifier.align(Alignment.TopCenter)
                    .offset(y = (-10).dp) // FAB 중심점 맞춤
            ) {
                // (아이콘, 라벨, 경로, 각도)
                val menuItems = listOf(
                    Triple(R.drawable.ic_calendar, "calendar_flow", 150f), // 왼쪽 위
                    Triple(R.drawable.ic_character, "character_flow", 90f),  // 정중앙 위
                    Triple(R.drawable.ic_boss, "boss_flow", 30f)      // 오른쪽 위
                )

                menuItems.forEach { (icon, destination, angle) ->
                    val radius = 80.dp // 퍼지는 반지름 거리

                    // 각도를 라디안으로 변환하여 좌표 계산
                    val angleRad = Math.toRadians(angle.toDouble())
                    val xOffset = (radius.value * kotlin.math.cos(angleRad)).dp * animProgress
                    val yOffset = -(radius.value * kotlin.math.sin(angleRad)).dp * animProgress

                    SmallFloatingActionButton(
                        onClick = {
                            isExpanded = false
                            if (destination == "calendar_flow") {
                                onFetchEvent()
                            }
                            navController.navigate(destination)
                        },
                        modifier = Modifier.offset(x = xOffset, y = yOffset)
                            .size(48.dp)
                            .alpha(animProgress), // 나타날 때 서서히 밝아짐
                        containerColor = MapleTheme.colors.primary, // 와이어프레임의 주황색 버튼
                        contentColor = MapleTheme.colors.surface,
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = destination,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                if (isLoginSuccess) {
                    isExpanded = !isExpanded
                } else {
                    navController.navigate("login_flow")
                }
            }, // 클릭 시 메뉴 토글
            containerColor = MapleTheme.colors.primary,
            contentColor = MapleTheme.colors.surface,
            shape = CircleShape,
            modifier = Modifier.size(60.dp)
                .align(Alignment.TopCenter)
                // FAB 위치는 고정 (시스템 바 패딩에 영향받지 않도록)
                .offset(y = (-10).dp)
                .zIndex(1F)
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(R.drawable.bottomnav_calendar),
                contentDescription = "중앙 FAB",
                tint = Color.Unspecified
            )
        }
    }
}

// 개별 네비게이션 아이템 컴포저블
@Composable
fun MapleBottomNavItem(
    item: Navigation,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 아이콘 색상을 흰색/연한주황색으로 대비 조정
        Icon(
            painter = painterResource(
                id = if (isSelected) item.selectedIconRes!! else item.unselectedIconRes!!
            ),
            contentDescription = item.label,
            tint = if (isSelected) MapleTheme.colors.surface else MapleTheme.colors.surface.copy(alpha = 0.6f),
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // [핵심] 선택 표시 인디케이터 (밑줄 모양)
        Box(
            modifier = Modifier.width(20.dp)
                .height(3.dp)
                .background(
                    color = if (isSelected) MapleTheme.colors.surface else Color.Transparent,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}