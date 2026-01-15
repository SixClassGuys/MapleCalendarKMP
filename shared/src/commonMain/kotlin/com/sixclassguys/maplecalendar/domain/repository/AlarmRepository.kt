package com.sixclassguys.maplecalendar.domain.repository

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    suspend fun submitEventAlarm(
        apiKey: String,
        eventId: Long,
        isEnabled: Boolean,
        alarmTimes: List<String>
    ): Flow<ApiState<MapleEvent>>

    suspend fun toggleEventAlarm(apiKey: String, eventId: Long): Flow<ApiState<MapleEvent>>
}