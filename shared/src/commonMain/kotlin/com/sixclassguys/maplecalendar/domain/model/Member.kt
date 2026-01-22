package com.sixclassguys.maplecalendar.domain.model

data class Member(
    val isGlobalAlarmEnabled: Boolean = true,
    val characterBasic: CharacterBasic?,
    val characterPopularity: Int,
    val characterOverallRanking: CharacterRanking?,
    val characterServerRanking: CharacterRanking?,
    val characterUnionLevel: CharacterUnion?,
    val characterDojang: CharacterDojangRanking?
)