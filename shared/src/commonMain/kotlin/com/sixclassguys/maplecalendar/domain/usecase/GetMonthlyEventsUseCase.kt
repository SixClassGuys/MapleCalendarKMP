package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleEvent
import com.sixclassguys.maplecalendar.domain.repository.EventRepository
import com.sixclassguys.maplecalendar.util.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMonthlyEventsUseCase(
    private val repository: EventRepository
) {

    operator fun invoke(year: Int, month: Int): Flow<ApiState<List<MapleEvent>>> = flow {
        emit(ApiState.Loading) // 로딩 시작

        try {
            val data = repository.getEvents(year, month)
            emit(ApiState.Success(data)) // 성공
        }catch (e: ApiException) {
            if (e.code == 401) {
                // TODO: 특별하게 401 에러일 때는 로그인 화면으로 보내는 로직 추가
            }
            emit(ApiState.Error(e.message))
        }
    }
}