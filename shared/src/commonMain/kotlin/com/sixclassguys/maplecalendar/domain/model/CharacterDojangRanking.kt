package com.sixclassguys.maplecalendar.domain.model

data class CharacterDojangRanking(
    val characterClass: String,
    val worldName: String,
    val dojangBestFloor: String,
    val dateDojangRecord: String,
    val dojangBestTime: Long?,
)