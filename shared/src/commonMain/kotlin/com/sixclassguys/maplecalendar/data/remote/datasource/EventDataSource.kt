package com.sixclassguys.maplecalendar.data.remote.datasource

import com.sixclassguys.maplecalendar.data.remote.dto.EventResponse

interface EventDataSource {

    suspend fun fetchMonthlyEvents(year: Int, month: Int): List<EventResponse>
}