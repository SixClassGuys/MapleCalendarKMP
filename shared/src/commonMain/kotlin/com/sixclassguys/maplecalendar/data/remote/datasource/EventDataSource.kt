package com.sixclassguys.maplecalendar.data.remote.datasource

import com.sixclassguys.maplecalendar.data.remote.dto.EventResponse

interface EventDataSource {

    suspend fun fetchEventDetail(apiKey: String, eventId: Long): EventResponse?

    suspend fun fetchTodayEvents(
        year: Int,
        month: Int,
        day: Int,
        apiKey: String
    ): List<EventResponse>

    suspend fun fetchMonthlyEvents(year: Int, month: Int): List<EventResponse>
}