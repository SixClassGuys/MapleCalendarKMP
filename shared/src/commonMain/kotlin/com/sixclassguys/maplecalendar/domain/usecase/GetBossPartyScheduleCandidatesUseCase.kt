package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.BossPartyCommonSchedule
import com.sixclassguys.maplecalendar.domain.repository.BossRepository
import kotlinx.coroutines.flow.Flow

class GetBossPartyScheduleCandidatesUseCase(
    private val repository: BossRepository
) {

    suspend operator fun invoke(bossPartyId: Long): Flow<ApiState<List<BossPartyCommonSchedule>>> {
        return repository.getScheduleCandidates(bossPartyId)
    }
}