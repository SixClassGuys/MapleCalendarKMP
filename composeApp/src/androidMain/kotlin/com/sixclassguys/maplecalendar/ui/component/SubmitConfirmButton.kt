package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.Typography

@Composable
fun SubmitConfirmButton(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val buttonColor = if (isSelected) MapleTheme.colors.primary else MapleTheme.colors.outline
    val textColor = if (isSelected) MapleTheme.colors.surface else MapleTheme.colors.onSurface

    Box(
        modifier = Modifier.fillMaxWidth()
            .navigationBarsPadding() // 시스템 네비게이션 바와 겹침 방지
            .background(buttonColor)
            .clickable(enabled = isSelected) { onClick() }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "캐릭터 등록",
            style = Typography.titleMedium,
            color = textColor
        )
    }
}