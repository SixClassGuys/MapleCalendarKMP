package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BossPartyUpdateScheduleResponse(
    val newAvailableSlots: String,
    val newKeepNextWeek: Boolean
)