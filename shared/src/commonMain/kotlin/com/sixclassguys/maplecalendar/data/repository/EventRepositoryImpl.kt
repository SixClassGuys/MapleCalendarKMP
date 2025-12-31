package com.sixclassguys.maplecalendar.data.repository

import com.sixclassguys.maplecalendar.data.remote.datasource.EventDataSource
import com.sixclassguys.maplecalendar.data.remote.dto.EventResponse
import com.sixclassguys.maplecalendar.data.remote.dto.toDomain
import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import com.sixclassguys.maplecalendar.domain.repository.EventRepository

class EventRepositoryImpl(
    private val dataSource: EventDataSource
) : EventRepository {

    override suspend fun getEvents(year: Int, month: Int): List<MapleEvent> {
        // DataSource에서 DTO 리스트를 가져옴
        val responses: List<EventResponse> = dataSource.fetchMonthlyEvents(year, month)

        // 각 DTO를 도메인 모델로 변환 (Mapping)
        return responses.map { it.toDomain() }
    }
}