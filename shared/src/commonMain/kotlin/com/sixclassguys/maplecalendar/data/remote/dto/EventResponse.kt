package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val id: Long,
    val title: String,
    val url: String,

    @SerialName("thumbnail_url")
    val thumbnailUrl: String?,
    val startDate: String,
    val endDate: String,
    val eventTypes: List<String>,
    val isRegistered: Boolean = false,
    val alarmTimes: List<String> = emptyList()
)

fun EventResponse.toDomain(): MapleEvent {
    return MapleEvent(
        id = this.id,
        title = this.title,
        url = this.url,
        thumbnailUrl = this.thumbnailUrl,
        startDate = LocalDate.parse(startDate.substringBefore("T")),
        endDate = LocalDate.parse(endDate.substringBefore("T")),
        eventTypes = this.eventTypes,
        isRegistered = this.isRegistered,
        notificationTimes = this.alarmTimes.map { LocalDateTime.parse(it) }
    )
}