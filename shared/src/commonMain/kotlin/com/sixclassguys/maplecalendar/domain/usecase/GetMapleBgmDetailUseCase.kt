package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleBgm
import com.sixclassguys.maplecalendar.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetMapleBgmDetailUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(bgmId: Long): Flow<ApiState<MapleBgm>> {
        return repository.getMapleBgmDetail(bgmId)
    }
}