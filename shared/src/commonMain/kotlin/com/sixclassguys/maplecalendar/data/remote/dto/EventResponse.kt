package com.sixclassguys.maplecalendar.data.remote.dto

import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val id: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String?,
    val startDate: String, // "2024-12-30T10:00:00" 형식
    val endDate: String
)

fun EventResponse.toDomain(): MapleEvent {
    return MapleEvent(
        id = this.id,
        title = this.title,
        url = this.url,
        thumbnailUrl = this.thumbnailUrl,
        startDate = LocalDate.parse(startDate.substringBefore("T")),
        endDate = LocalDate.parse(endDate.substringBefore("T"))
    )
}