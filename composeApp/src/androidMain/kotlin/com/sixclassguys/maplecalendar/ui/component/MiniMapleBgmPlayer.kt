package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sixclassguys.maplecalendar.domain.model.MapleBgm
import com.sixclassguys.maplecalendar.theme.MapleGray
import com.sixclassguys.maplecalendar.theme.MapleOrange
import com.sixclassguys.maplecalendar.theme.MapleWhite
import com.sixclassguys.maplecalendar.utils.RegionCategory

@Composable
fun MiniMapleBgmPlayer(
    modifier: Modifier = Modifier,
    bgm: MapleBgm,
    isPlaying: Boolean,
    onTogglePlay: (Boolean) -> Unit,
    onClose: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.width(140.dp) // 가로 폭을 줄여 중앙 버튼을 피함
            .wrapContentHeight()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MapleWhite,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, MapleOrange.copy(alpha = 0.5f)) // 테두리 추가로 시인성 확보
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally // 중앙 정렬
        ) {
            // 1. 상단 닫기 버튼 (작게 배치)
            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.TopEnd)
                        .clickable { onClose() },
                    tint = MapleGray
                )
            }

            // 2. 이미지 (앨범 아트)
            Image(
                painter = painterResource(RegionCategory.fromCode(bgm.region).iconRes),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            // 3. 곡 정보 (텍스트)
            Text(
                text = bgm.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = bgm.mapName,
                color = MapleGray,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            // 4. 재생 컨트롤 버튼
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onTogglePlay(isPlaying) },
                shape = CircleShape,
                color = MapleOrange
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = MapleWhite
                )
            }
        }
    }
}