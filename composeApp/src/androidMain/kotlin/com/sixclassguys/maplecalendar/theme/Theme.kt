package com.sixclassguys.maplecalendar.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MapleColorPalette = lightColorScheme(
    primary = MapleOrange,
    onPrimary = MapleWhite,
    secondary = MapleLightOrange,
    surface = MapleWhite,
    background = MapleBgOrange,
    onSurface = MapleBrown,
    onBackground = MapleBrown
)

@Composable
fun MapleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MapleColorPalette,
        // typography = MapleTypography, // 기존 타이포그래피 유지
        content = content
    )
}