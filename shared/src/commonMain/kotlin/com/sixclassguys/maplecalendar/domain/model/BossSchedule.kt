package com.sixclassguys.maplecalendar.domain.model

data class BossSchedule(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val partyCount: Int,
    val time: String
)