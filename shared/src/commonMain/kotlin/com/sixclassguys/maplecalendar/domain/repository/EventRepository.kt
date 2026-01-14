package com.sixclassguys.maplecalendar.domain.repository

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    suspend fun getEventDetail(apiKey: String, eventId: Long): Flow<ApiState<MapleEvent?>>

    suspend fun getTodayEvents(
        year: Int,
        month: Int,
        day: Int,
        apiKey: String
    ): Flow<ApiState<List<MapleEvent>>>

    suspend fun getEvents(year: Int, month: Int): List<MapleEvent>
}