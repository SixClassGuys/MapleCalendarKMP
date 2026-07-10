package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BossPartyUpdateScheduleRequest(
    val availableSlots: String,
    val keepNextWeek: Boolean
)