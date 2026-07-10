package com.sixclassguys.maplecalendar.domain.model

data class BossPartyScheduleCancellation(
    val triggerMemberName: String,
    val canceledDayOfWeek: String,
    val canceledTimeRange: String
)