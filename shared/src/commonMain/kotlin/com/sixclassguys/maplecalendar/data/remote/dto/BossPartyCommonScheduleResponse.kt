package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.BossPartyCommonSchedule
import kotlinx.serialization.Serializable

@Serializable
data class BossPartyCommonScheduleResponse(
    val selectedIndex: Int,
    val dayOfWeek: String,
    val timeRange: String
) {

    fun toDomain(): BossPartyCommonSchedule {
        return BossPartyCommonSchedule(
            selectedIndex = this.selectedIndex,
            dayOfWeek = this.dayOfWeek,
            timeRange = this.timeRange
        )
    }
}