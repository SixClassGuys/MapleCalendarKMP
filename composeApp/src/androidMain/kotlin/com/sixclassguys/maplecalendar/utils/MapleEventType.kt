package com.sixclassguys.maplecalendar.utils

import androidx.compose.ui.graphics.Color

enum class MapleEventType(val displayName: String, val color: Color) {

    PUNCH_KING("펀치킹", Color(0xFFF44336)),
    COIN_SHOP("코인샵", Color(0xFFFF9800)),
    SUNDAY_MAPLE("썬데이메이플", Color(0xFFE91E63)),
    BOSS("보스", Color(0xFF9C27B0)),
    PC_ROOM("프리미엄PC방", Color(0xFF2196F3)),
    CHALLENGERS("챌린저스", Color(0xFF009688)),
    ITEM_BURNING("아이템버닝", Color(0xFF4CAF50)),
    HYPER_BURNING("하이퍼버닝", Color(0xFFFF5722)),
    SAUNA("VIP사우나", Color(0xFF795548)),
    NEW_NAME("뉴네임옥션", Color(0xFF607D8B)),
    SPECIAL_WORLD("스페셜월드", Color(0xFF3F51B5)),
    ATTENDANCE("출석이벤트", Color(0xFFFFC107)),
    REMASTER("리마스터", Color(0xFF00BCD4)),
    ETC("기타", Color(0xFF9E9E9E)),
    PASS("패스", Color(0xFF00897B)),
    COORDINATE("코디", Color(0xFFBA68C8));

    companion object {

        fun fromString(name: String): MapleEventType {
            return entries.find { it.displayName == name } ?: ETC
        }
    }
}