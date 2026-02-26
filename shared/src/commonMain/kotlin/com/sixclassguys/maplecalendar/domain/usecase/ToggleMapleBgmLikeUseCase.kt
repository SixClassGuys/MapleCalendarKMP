package com.sixclassguys.maplecalendar.domain.usecase

import com.sixclassguys.maplecalendar.domain.model.ApiState
import com.sixclassguys.maplecalendar.domain.model.MapleBgm
import com.sixclassguys.maplecalendar.domain.repository.PlaylistRepository
import com.sixclassguys.maplecalendar.util.MapleBgmLikeStatus
import kotlinx.coroutines.flow.Flow

class ToggleMapleBgmLikeUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(
        bgmId: Long,
        status: MapleBgmLikeStatus
    ): Flow<ApiState<MapleBgm>> {
        return repository.toggleLike(bgmId, status)
    }
}