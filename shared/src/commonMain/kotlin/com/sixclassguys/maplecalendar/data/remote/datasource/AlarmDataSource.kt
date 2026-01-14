package com.sixclassguys.maplecalendar.data.remote.datasource

import com.sixclassguys.maplecalendar.data.remote.dto.AlarmRequest
import com.sixclassguys.maplecalendar.data.remote.dto.EventResponse

interface AlarmDataSource {

    suspend fun submitEventAlarm(apiKey: String, request: AlarmRequest): EventResponse

    suspend fun toggleEventAlarm(apiKey: String, eventId: Long): EventResponse
}