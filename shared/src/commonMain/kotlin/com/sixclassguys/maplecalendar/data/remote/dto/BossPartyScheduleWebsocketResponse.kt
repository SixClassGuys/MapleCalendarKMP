package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BossPartyScheduleWebsocketResponse(
    val type: String,
    val candidates: List<BossPartyCommonScheduleResponse>
)