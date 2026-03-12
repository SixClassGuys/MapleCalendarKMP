package com.sixclassguys.maplecalendar.ui.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sixclassguys.maplecalendar.R
import com.sixclassguys.maplecalendar.domain.model.BossPartyMember
import com.sixclassguys.maplecalendar.theme.MapleStatBackground
import com.sixclassguys.maplecalendar.theme.MapleStatTitle
import com.sixclassguys.maplecalendar.theme.MapleTheme
import com.sixclassguys.maplecalendar.theme.PretendardFamily
import com.sixclassguys.maplecalendar.theme.Typography
import com.sixclassguys.maplecalendar.util.BossPartyRole
import com.sixclassguys.maplecalendar.utils.MapleClass
import com.sixclassguys.maplecalendar.utils.MapleWorld

@Composable
fun BossPartyMemberContent(
    isLeader: Boolean,
    members: List<BossPartyMember>,
    onAddMember: () -> Unit,
    onTransferLeader: (Long) -> Unit,
    onRemoveMember: (Long) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val chunkedMembers = remember(members) { members.chunked(2) }

    // 🚀 LazyColumn을 일반 Column으로 변경 (부모가 이미 LazyColumn임)
    Column(
        modifier = modifier.fillMaxWidth()
            .background(MapleStatBackground, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // 1. 상단 헤더 (이제 StickyHeader 기능을 쓸 수 없으므로 일반 Row로 처리)
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "MEMBER",
                color = MapleStatTitle,
                style = Typography.titleMedium
            )
            IconButton(onClick = onAddMember) {
                Icon(Icons.Default.Add, contentDescription = null, tint = MapleTheme.colors.surface)
            }
        }

        // 2. 캐릭터 그리드 영역
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .background(MapleTheme.colors.surface, shape = RoundedCornerShape(24.dp))
                .padding(12.dp)
        ) {
            if (members.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("파티원이 없습니다.", color = MapleTheme.colors.outline)
                    }
                }
            } else {
                items(chunkedMembers) { members ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        members.forEach { member ->
                            Box(modifier = Modifier.weight(1f)) {
                                PartyMemberItem(isLeader, member, context, onTransferLeader, onRemoveMember)
                            }
                        }
                        if (members.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun PartyMemberItem(
    isLeader: Boolean,
    member: BossPartyMember,
    context: Context,
    onTransferLeader: (Long) -> Unit,
    onRemoveMember: (Long) -> Unit
) {
    val worldMark = MapleWorld.getWorld(member.worldName)?.iconRes ?: R.drawable.ic_world_scania
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(0.75f), // 🚀 카드의 세로 비율을 고정해서 정갈하게 만듭니다.
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MapleTheme.colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center, // 🚀 중앙 정렬
                    modifier = Modifier.fillMaxSize()
                        .padding(8.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(member.characterImage.trim())
                            .crossfade(true)
                            .build(),
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

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = member.characterName,
                            fontFamily = PretendardFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = worldMark),
                            contentDescription = "월드 이름",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Text(
                        text = "Lv.${member.characterLevel}",
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
                        val mapleClass = MapleClass.fromString(member.characterClass)
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
                            text = member.characterClass,
                            fontFamily = PretendardFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 🚀 와이어프레임의 우측 상단 X 버튼 (삭제 기능) 추가
        // 방장(별표)이 아닐 때만 노출하거나, 권한에 따라 노출
        if (member.role == BossPartyRole.LEADER) {
            IconButton(
                enabled = false,
                onClick = { },
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "파티장",
                    tint = MapleTheme.colors.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else if (isLeader) {
            IconButton(
                onClick = { onTransferLeader(member.characterId) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "파티장 양도",
                    tint = MapleTheme.colors.primary, // 금색 계열
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = { onRemoveMember(member.characterId) },
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "파티원 추방",
                    tint = MapleTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}