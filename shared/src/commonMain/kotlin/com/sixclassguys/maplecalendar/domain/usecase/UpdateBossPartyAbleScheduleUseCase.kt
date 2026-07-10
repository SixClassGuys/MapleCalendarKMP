package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.repository.BossRepository
import kotlinx.coroutines.flow.Flow

class UpdateBossPartyAbleScheduleUseCase(
    private val repository: BossRepository
) {

    suspend operator fun invoke(
        bossPartyId: Long,
        availableSlots: String,
        keepNextWeek: Boolean
    ): Flow<ApiState<Pair<String, Boolean>>> {
        return repository.updateSchedule(bossPartyId, availableSlots, keepNextWeek)
    }
}