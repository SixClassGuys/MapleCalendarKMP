package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sixclassguys.maplecalendar.domain.model.MapleEvent

@Composable
fun EventListItem(
    event: MapleEvent
) {
    val uriHandler = LocalUriHandler.current // URL 오픈을 위한 핸들러

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { uriHandler.openUri(event.url) }, // 클릭 시 URL 연결
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            // 1. 섬네일 (이미지 로딩 라이브러리 사용 권장: Coil-Multiplatform 등)
            if (event.thumbnailUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(event.thumbnailUrl)
                        .crossfade(true) // 부드러운 이미지 전환
                        .build(),
                    // placeholder = painterResource(Res.drawable.placeholder_image), // 로딩 중 이미지
                    // error = painterResource(Res.drawable.error_image), // 실패 시 이미지
                    contentDescription = null,
                    modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 2. 제목 및 기간
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 1
                )
                Text(
                    "${event.startDate} ~ ${event.endDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}