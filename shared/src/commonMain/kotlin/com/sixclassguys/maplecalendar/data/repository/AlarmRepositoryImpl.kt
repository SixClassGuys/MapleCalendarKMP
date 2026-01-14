package com.sixclassguys.maplecalendar.data.repository

import com.sixclassguys.maplecalendar.data.remote.datasource.AlarmDataSource
import com.sixclassguys.maplecalendar.data.remote.dto.AlarmRequest
import com.sixclassguys.maplecalendar.data.remote.dto.toDomain
import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import com.sixclassguys.maplecalendar.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlarmRepositoryImpl(
    private val dataSource: AlarmDataSource
) : AlarmRepository {

    override suspend fun submitEventAlarm(
        apiKey: String,
        eventId: Long,
        isEnabled: Boolean,
        alarmTimes: List<String>
    ): Flow<ApiState<MapleEvent>> = flow {
        emit(ApiState.Loading)

        val response = dataSource.submitEventAlarm(
            apiKey = apiKey,
            request = AlarmRequest(
                eventId = eventId,
                isEnabled = isEnabled,
                alarmTimes = alarmTimes
            )
        )
        val event = response.toDomain()

        emit(ApiState.Success(event))
    }

    override suspend fun toggleEventAlarm(
        apiKey: String,
        eventId: Long
    ): Flow<ApiState<MapleEvent>> = flow {
        emit(ApiState.Loading)

        val response = dataSource.toggleEventAlarm(apiKey = apiKey, eventId = eventId)
        val event = response.toDomain()

        emit(ApiState.Success(event))
    }
}