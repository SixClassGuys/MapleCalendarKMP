package com.sixclassguys.maplecalendar.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.sixclassguys.maplecalendar.domain.model.CharacterSummary
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.PretendardFamily
import com.sixclassguys.maplecalendar.utils.MapleClass

@Composable
fun MapleCharacterGrid(
    characters: List<CharacterSummary>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (characters.isEmpty()) {
            // 캐릭터가 없을 때 표시할 문구
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "등록된 캐릭터가 없습니다.",
                    fontFamily = PretendardFamily,
                    color = MapleTheme.colors.outline,
                    fontSize = 14.sp
                )
                Text(
                    text = "우측 상단의 + 버튼을 눌러 캐릭터를 추가해보세요!",
                    fontFamily = PretendardFamily,
                    color = MapleTheme.colors.outline.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        } else {
            // 캐릭터가 있을 때 그리드 표시
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = characters,
                    key = { it.ocid } // 성능 최적화를 위한 key 지정
                ) { character ->
                    CharacterCard(character = character)
                }
            }
        }
    }
}

@Composable
fun CharacterCard(character: CharacterSummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f), // 🚀 카드의 세로 비율을 고정해서 정갈하게 만듭니다.
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MapleTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 대표 캐릭터 별표
            if (character.isRepresentativeCharacter) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MapleTheme.colors.primary,
                    modifier = Modifier.padding(12.dp)
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center, // 🚀 중앙 정렬
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = character.characterImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                        .weight(1.2f) // 텍스트 영역보다 이미지 영역 비중을 더 높임
                        .graphicsLayer(
                            scaleX = 2.8f, // 1.5배 확대
                            scaleY = 2.8f,
                            translationY = -15f // 캐릭터 발 위치 조정 필요 시 사용
                        ),
                    contentScale = ContentScale.Fit, // Crop보다는 Fit 상태에서 확대하는 게 위치 잡기 편합니다.
                )

                Text(
                    text = character.characterName,
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Lv.${character.characterLevel}",
                    fontFamily = PretendardFamily,
                    color = MapleTheme.colors.outline,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 🚀 1. 캐릭터 직업 정보 매핑
                    val mapleClass = MapleClass.fromString(character.characterClass)
                    val classGroup = mapleClass.group

                    // 🚀 2. 직업군 뱃지 아이콘 배치
                    Icon(
                        painter = painterResource(id = classGroup.badge),
                        contentDescription = classGroup.groupName,
                        tint = Color.Unspecified, // 원본 이미지 색상을 그대로 사용하려면 Unspecified
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = character.characterClass,
                        fontFamily = PretendardFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}