package com.sixclassguys.maplecalendar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlarmRequest(
    val eventId: Long,
    val isEnabled: Boolean,
    val alarmTimes: List<String>
)