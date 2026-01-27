package com.sixclassguys.maplecalendar.data.repository

import com.sixclassguys.maplecalendar.data.remote.datasource.AlarmDataSource
import com.sixclassguys.maplecalendar.data.remote.dto.AlarmRequest
import com.sixclassguys.maplecalendar.data.remote.dto.toDomain
import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import com.sixclassguys.maplecalendar.domain.repository.AlarmRepository
import com.sixclassguys.maplecalendar.util.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
    }.catch { e ->
        val errorState = when (e) {
            is ApiException -> ApiState.Error(e.message)
            else -> ApiState.Error("시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
        }
        emit(errorState)
    }

    override suspend fun toggleEventAlarm(
        apiKey: String,
        eventId: Long
    ): Flow<ApiState<MapleEvent>> = flow {
        emit(ApiState.Loading)

        val response = dataSource.toggleEventAlarm(apiKey = apiKey, eventId = eventId)
        val event = response.toDomain()

        emit(ApiState.Success(event))
    }.catch { e ->
        val errorState = when (e) {
            is ApiException -> ApiState.Error(e.message)
            else -> ApiState.Error("시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
        }
        emit(errorState)
    }
}