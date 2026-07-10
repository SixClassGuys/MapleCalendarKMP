package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.BossPartyScheduleCancellation
import kotlinx.serialization.Serializable

@Serializable
data class BossPartyScheduleCancellationWebsocketResponse(
    val triggerMemberName: String,
    val canceledDayOfWeek: String?,
    val canceledTimeRange: String?
) {

    fun toDomain(): BossPartyScheduleCancellation {
        return BossPartyScheduleCancellation(
            triggerMemberName = this.triggerMemberName,
            canceledDayOfWeek = this.canceledDayOfWeek ?: "",
            canceledTimeRange = this.canceledTimeRange ?: ""
        )
    }
}