package com.sixclassguys.maplecalendar.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MapleEvent(
    val id: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isRegistered: Boolean,
    val notificationTimes: List<LocalDateTime>
)