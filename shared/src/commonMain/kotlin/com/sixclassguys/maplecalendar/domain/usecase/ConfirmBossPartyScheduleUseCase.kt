package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.repository.BossRepository
import kotlinx.coroutines.flow.Flow

class ConfirmBossPartyScheduleUseCase(
    private val repository: BossRepository
) {

    suspend operator fun invoke(
        bossPartyId: Long,
        selectedIndex: Int,
        message: String
    ): Flow<ApiState<Unit>> {
        return repository.confirmBossTime(bossPartyId, selectedIndex, message)
    }
}